package com.satori.mods.suite;

import com.satori.libs.async.api.*;
import com.satori.mods.core.stats.*;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.module.afterburner.*;
import com.fasterxml.jackson.module.jsonSchema.*;
import org.slf4j.*;

public class SchemaDetectorMod extends Mod {
  public static final Logger log = LoggerFactory.getLogger(SchemaDetectorMod.class);
  public static final ObjectMapper mapper = new ObjectMapper()
    .enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    .registerModule(new AfterburnerModule());
  
  public final SchemaDetectorModStats stats = new SchemaDetectorModStats();
  
  JsonSchema schema = null;
  
  @Override
  public void onStats(StatsCycle cycle, IStatsCollector collector) {
    log.debug("collecting statistic...");
    stats.drain(collector);
  }
  
  @Override
  public void onInput(String inputName, JsonNode data, IAsyncHandler cont) throws Exception {
    if (data == null) {
      cont.succeed();
      return;
    }
    stats.recv += 1;
    JsonSchema res = SchemaDetector.process(schema, data);
    if (res == null) {
      cont.succeed();
      return;
    }
    log.info("schema updated");
    schema = res;
    yield(mapper.valueToTree(res), cont);
  }
}
