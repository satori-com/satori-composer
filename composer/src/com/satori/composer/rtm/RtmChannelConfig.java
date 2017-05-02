package com.satori.composer.rtm;

import com.satori.composer.rtm.core.*;
import com.satori.mods.core.config.*;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;

@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class RtmChannelConfig extends RtmBaseConfig {
  
  @JsonProperty("subscriptions")
  public RtmSubscriptionConfig subscriptions = null;
  
  @JsonProperty("user-data")
  public JsonNode userData = null;
  
  public RtmChannelConfig() {
  }
  
  public RtmChannelConfig(RtmChannelConfig cfg) {
    super(cfg);
    subscriptions = cfg.subscriptions;
    userData = cfg.userData;
  }
  
  public RtmChannelConfig(RtmBaseConfig cfg, RtmSubscriptionConfig subscriptions) {
    super(cfg);
    this.subscriptions = subscriptions;
  }
  
  public void validate() throws InvalidConfigException {
    super.validate();
    if (host == null || host.isEmpty()) {
      throw new InvalidConfigException(this.getClass() + ": host is not specified");
    }
    if (subscriptions != null) {
      subscriptions.validate();
    }
  }
}
