package com.satori.composer.runtime;

import com.satori.composer.stats.*;
import com.satori.mods.core.config.*;
import com.satori.mods.suite.*;

import java.util.*;

import com.fasterxml.jackson.annotation.*;

@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class ComposerRuntimeConfig extends Config {
  public final static int defaultPulseInterval = 1000;
  
  @JsonProperty("pulse")
  public int pulse = defaultPulseInterval;
  
  @JsonProperty("stats")
  public StatsModuleConfig stats = null;
  
  @JsonProperty("mods")
  public HashMap<String, CompositionNodeConfig> mods = null;
  
  @Override
  public void validate() throws InvalidConfigException {
    if (pulse <= 0) {
      throw new InvalidConfigException("invalid pulse interval");
    }
    
    if (stats == null) {
      stats = new StatsModuleConfig();
    }
    stats.validate();
  }
  
}