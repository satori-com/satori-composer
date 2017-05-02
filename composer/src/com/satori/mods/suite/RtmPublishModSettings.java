package com.satori.mods.suite;

import com.satori.composer.rtm.core.*;
import com.satori.mods.core.config.*;

import com.fasterxml.jackson.annotation.*;

@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class RtmPublishModSettings extends RtmBaseConfig {
  
  @JsonProperty("channel")
  public String channel = null;
  
  public RtmPublishModSettings() {
  }
  
  @Override
  public void validate() throws InvalidConfigException {
    super.validate();
    if (channel == null || channel.isEmpty()) {
      throw new InvalidConfigException("'channel' not specified");
    }
  }
}
