package com.satori.composer.stats.rtm;

import com.satori.mods.core.config.*;

import java.util.*;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;

@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class StatsRtmRefForwarderConfig extends Config {
  
  public static final long defaultPeriod = 0; // in ms
  
  @JsonProperty("ref-id")
  public String refId = null;
  
  @JsonProperty("period")
  public long period = defaultPeriod;
  
  @JsonProperty("prefix")
  public String prefix = null;
  
  @JsonProperty("channel")
  public String channel = null;
  
  @JsonProperty("ext")
  public HashMap<String, JsonNode> ext = null;
  
  @Override
  public void validate() throws InvalidConfigException {
    if (refId == null || refId.isEmpty()) {
      throw new InvalidConfigException("missing 'ref-id' parameter");
    }
    if (channel == null || channel.isEmpty()) {
      throw new InvalidConfigException("missing 'channel' parameter");
    }
    if (period < 0) {
      throw new InvalidConfigException("invalid period");
    }
    
  }
}