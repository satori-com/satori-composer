package com.satori.composer.rtm.core;

import java.io.*;
import java.nio.charset.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.type.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ser.*;
import io.netty.buffer.*;
import io.vertx.core.http.*;
import io.vertx.core.http.impl.*;
import io.vertx.core.http.impl.ws.*;

public class RtmParser implements IRtmPduController, IWsFrameHandler {
  public static final TypeReference pduTypeRef = new TypeReference<RtmPdu<JsonNode>>() {
  };
  
  private IRtmPduHandler slave;
  private IWsFrameController master;
  private final IRtmParserContext ctx;
  private final JsonFactory factory;
  private final JsonSerializer<Object> serializer;
  private final SerializerProvider serializerProvider;
  
  public RtmParser(IRtmParserContext ctx, IRtmPduHandler slave) {
    this.ctx = ctx;
    this.slave = slave;
    ObjectMapper mapper = ctx.mapper();
    factory = mapper.getFactory();
    serializerProvider = new SerializerProviderImpl(
      mapper.getSerializerProvider(), mapper.getSerializationConfig(), mapper.getSerializerFactory()
    );
    try {
      serializer = serializerProvider.findTypedValueSerializer(RtmPdu.class, true, null);
    } catch (JsonMappingException e) {
      throw new RuntimeException(e);
    }
    
  }
  
  // IRtmPduController implementation
  
  @Override
  public <T> void send(RtmPdu<T> pdu) {
    final WebSocketFrame frame;
    try {
      ByteBuf binaryData = Unpooled.buffer();
      JsonGenerator gen = factory.createGenerator(
        (OutputStream) new ByteBufOutputStream(binaryData), JsonEncoding.UTF8
      );
      serializer.serialize(pdu, gen, serializerProvider);
      gen.close();
      frame = new WebSocketFrameImpl(FrameType.TEXT, binaryData, true);
    } catch (Throwable cause) {
      ctx.log().error("failed to serialize pdu({})", pdu, cause);
      fail(cause);
      return;
    }
    master.send(frame);
  }
  
  @Override
  public void fail(Throwable cause) {
    master.fail(cause);
  }
  
  @Override
  public void close() {
    master.close();
  }
  
  @Override
  public boolean isWritable() {
    return master.isWritable();
  }
  
  // IWsFrameHandler implementation
  
  @Override
  public void onStart(IWsFrameController master) throws Throwable {
    this.master = master;
    slave.onStart(this);
  }
  
  @Override
  public void onStop() throws Throwable {
    slave.onStop();
    this.master = null;
  }
  
  @Override
  public void onPulse(long timestamp) throws Throwable {
    slave.onPulse(timestamp);
  }
  
  @Override
  public void onRecv(WebSocketFrame frame) throws Throwable {
    final RtmPdu<JsonNode> pdu;
    try {
      pdu = ctx.mapper().readValue(frame.textData(), pduTypeRef);
      if (pdu == null || pdu.action == null) {
        fail(new Exception("protocol error"));
        return;
      }
      if (pdu.action.equals("error") || pdu.action.equals("/error")) {
        fail(new Exception(String.format(
          "error pdu received: %s",
          new String(frame.binaryData().getBytes(), StandardCharsets.UTF_8)
        )));
        return;
      }
    } catch (Throwable cause) {
      fail(cause);
      return;
    }
    slave.onRecv(pdu);
  }
  
  @Override
  public void onWritableChanged(boolean isWritable) throws Throwable {
    slave.onWritableChanged(isWritable);
  }
  
  private static class SerializerProviderImpl extends DefaultSerializerProvider {
    public SerializerProviderImpl(SerializerProvider src, SerializationConfig config, SerializerFactory f) {
      super(src, config, f);
    }
    
    @Override
    public DefaultSerializerProvider createInstance(SerializationConfig config, SerializerFactory jsf) {
      return new SerializerProviderImpl(this, config, jsf);
    }
  }
}
    