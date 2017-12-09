package com.satori.composer.rtm;

import com.satori.composer.rtm.core.*;
import com.satori.mods.core.config.*;

import java.util.*;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;

@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class RtmDriverConfig extends RtmBaseConfig {
  
  @JsonProperty("channel")
  public String channel = null;
  
  @JsonProperty("prefix")
  public boolean prefix = false;
  
  @JsonProperty("filter")
  public String filter = null;
  
  @JsonProperty("history")
  public Map history = null;
  
  @JsonProperty("user-data")
  public JsonNode userData = null;
  
  public RtmDriverConfig() {
  }
  
  public RtmDriverConfig(RtmDriverConfig cfg) {
    this(cfg, cfg.channel, cfg.userData);
  }
  
  public RtmDriverConfig(RtmBaseConfig cfg, String channel, JsonNode userData) {
    super(cfg);
    this.channel = channel;
    this.userData = userData;
  }
  
  public void validate() throws InvalidConfigException {
    super.validate();
    if (host == null || host.isEmpty()) {
      throw new InvalidConfigException("host not specified");
    }
    if (channel == null || channel.isEmpty()) {
      throw new InvalidConfigException("channel not specified");
    }
    if (filter != null && filter.isEmpty()) {
      filter = null;
    }
  }
}
