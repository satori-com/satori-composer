package com.satori.mods.suite;

import com.satori.composer.rtm.*;
import com.satori.composer.rtm.core.*;
import com.satori.mods.core.config.*;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;

@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class RtmSubscribeModSettings extends RtmDriverConfig {
  public static final int defaultWindowMaxSize = 64 * 1024;
  
  @JsonProperty("windows-max-size")
  public int windowMaxSize = defaultWindowMaxSize;
  
  public RtmSubscribeModSettings() {
  }
  
  public void validate() throws InvalidConfigException {
    super.validate();
    if (windowMaxSize <= 0) {
      throw new InvalidConfigException("invalid windows-max-size");
    }
  }
}
