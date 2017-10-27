package com.satori.mods.suite;

import com.satori.libs.async.api.*;
import com.satori.mods.api.*;
import com.satori.mods.core.config.*;
import com.satori.mods.core.stats.*;
import com.satori.mods.resources.*;

import java.io.*;
import java.util.*;
import javax.xml.transform.stream.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.dataformat.xml.*;
import net.sf.saxon.s9api.*;
import org.slf4j.*;

public class XsltMod extends Mod {
  public static final Logger log = LoggerFactory.getLogger(XsltMod.class);
  public static final ObjectMapper mapper = Config.mapper;
  
  private final XsltModStats stats = new XsltModStats();
  private final IBufferParser bufferParser;
  
  public XsltMod(JsonNode userData) throws Exception {
    this(Config.parseAndValidate(userData, XsltModSettings.class));
  }
  
  public XsltMod(XsltModSettings config) throws Exception {
    String xslt = config.xslt;
    if (xslt != null && !xslt.isEmpty()) {
      bufferParser = new XsltBufferParser(xslt);
    } else {
      bufferParser = this::processXmlSimple;
    }
    log.info("created");
  }
  
  // IMod implementation
  
  @Override
  public void init(IModContext context) throws Exception {
    super.init(context);
    stats.reset();
    log.info("initialized");
  }
  
  @Override
  public void dispose() throws Exception {
    super.dispose();
    stats.reset();
    log.info("terminated");
  }
  
  @Override
  public void onStats(StatsCycle cycle, IStatsCollector collector) {
    log.debug("collecting statistic...");
    stats.drain(collector);
  }
  
  @Override
  public void onInput(String inputName, JsonNode data, IAsyncHandler cont) throws Exception {
    ArrayDeque<JsonNode> entities = bufferParser.parse(data.binaryValue());
    stats.received += entities.size();
    if (entities.size() <= 0) {
      cont.succeed();
      return;
    }
    yieldLoop(entities, cont);
  }
  
  
  // private methods
  
  public void yieldLoop(ArrayDeque<JsonNode> entities, IAsyncHandler cont) throws Exception {
    while (true) {
      JsonNode msg = entities.pollFirst();
      stats.sent += 1;
      if (entities.size() == 0) {
        // this the last message
        yield(msg, cont);
        return;
      }
      IAsyncFuture<?> future = yield(msg);
      if (!future.isCompleted()) {
        // operation still in progress, set continuation and exit
        future.onCompleted(ar -> {
          if (!ar.isSucceeded()) {
            cont.fail(ar.getError());
            return;
          }
          try {
            yieldLoop(entities, cont);
          } catch (Exception e) {
            cont.fail(e);
          }
        });
        return;
      }
      
      // operation was completed immediately
      IAsyncResult<?> ar = future.getResult();
      if (!ar.isSucceeded()) {
        // abort loop with failure
        cont.fail(ar.getError());
        return;
      }
    }
  }
  
  private ArrayDeque<JsonNode> processXmlSimple(byte[] bytes) throws Exception {
    XmlMapper xmlMapper = new XmlMapper();
    ArrayDeque<JsonNode> result = new ArrayDeque<>(1);
    result.addLast(xmlMapper.readTree(bytes));
    return result;
  }
  
  public interface IBufferParser {
    ArrayDeque<JsonNode> parse(byte[] bytes) throws Exception;
  }
  
  private class XsltBufferParser implements IBufferParser {
    private final Processor proc = new Processor(false);
    private final XsltExecutable exp;
    
    public XsltBufferParser(String resource) throws Exception {
      try (InputStream is = ModResourceLoader.loadAsStream(resource)) {
        XsltCompiler comp = proc.newXsltCompiler();
        exp = comp.compile(new StreamSource(is));
      }
    }
    
    @Override
    public ArrayDeque<JsonNode> parse(byte[] bytes) throws Exception {
      
      ByteArrayOutputStream jsonStream = new ByteArrayOutputStream();
      Serializer out = proc.newSerializer();
      out.setOutputStream(jsonStream);
      out.setOutputProperty(Serializer.Property.METHOD, "text");
      out.setOutputProperty(Serializer.Property.INDENT, "no");
      XsltTransformer trans = exp.load();
      trans.setSource(new StreamSource(new ByteArrayInputStream(bytes)));
      trans.setDestination(out);
      trans.transform();
      
      ArrayDeque<JsonNode> result = new ArrayDeque<>();
      try (InputStream is = new ByteArrayInputStream(jsonStream.toByteArray())) {
        JsonParser parser = mapper.getFactory().createParser(is);
        for (Iterator<JsonNode> it = mapper.readValues(parser, JsonNode.class); it.hasNext(); ) {
          result.addLast(it.next());
        }
      }
      return result;
    }
  }
  
}
