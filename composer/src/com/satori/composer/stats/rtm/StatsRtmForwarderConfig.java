package com.satori.composer.stats.rtm;

import com.satori.composer.rtm.core.*;
import com.satori.mods.core.config.*;

import java.util.*;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;

@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class StatsRtmForwarderConfig extends RtmBaseConfig {
  
  public static final long defaultPeriod = 0; // in ms
  
  @JsonProperty("period")
  public long period = defaultPeriod;
  
  @JsonProperty("prefix")
  public String prefix = null;
  
  @JsonProperty("tags")
  public HashMap<String, String> tags = null;
  
  @JsonProperty("channel")
  public String channel = null;
  
  @JsonProperty("ext")
  public HashMap<String, JsonNode> ext = null;
  
  @Override
  public void validate() throws InvalidConfigException {
    super.validate();
    if (channel == null || channel.isEmpty()) {
      throw new InvalidConfigException("missing channel parameter");
    }
    if (period < 0) {
      throw new InvalidConfigException("invalid period");
    }
  }
}