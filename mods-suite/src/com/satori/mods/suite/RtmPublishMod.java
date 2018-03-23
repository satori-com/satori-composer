package com.satori.mods.suite;

import com.satori.libs.async.api.*;
import com.satori.mods.core.config.*;

import com.fasterxml.jackson.databind.*;
import org.slf4j.*;

public class RtmPublishMod extends RtmPublishBaseMod {
  public static final Logger log = LoggerFactory.getLogger(RtmPublishMod.class);
  private final RtmPublishModSettings config;
  
  public RtmPublishMod(JsonNode userData) throws Exception {
    this(Config.parseAndValidate(userData, RtmPublishModSettings.class));
  }
  
  public RtmPublishMod(RtmPublishModSettings config) throws Exception {
    this.config = config;
    log.info("created");
  }
  
  @Override
  public Logger log() {
    return log;
  }
  
  @Override
  public RtmPublishModSettings config() {
    return config;
  }
  
  @Override
  public String label() {
    return config.channel;
  }
  
  @Override
  public void processInput(String inputName, JsonNode msg, IAsyncHandler cont) throws Exception {
    publish(config.channel, msg, cont);
  }
}
