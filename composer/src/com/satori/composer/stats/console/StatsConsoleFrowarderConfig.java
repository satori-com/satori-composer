package com.satori.composer.stats.console;

import com.satori.mods.core.config.*;

import com.fasterxml.jackson.annotation.*;

@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class StatsConsoleFrowarderConfig extends Config {
  public static final long defaultPeriod = 0; // in ms
  
  @JsonProperty("prefix")
  public String prefix = null;
  
  @JsonProperty("period")
  public long period = defaultPeriod;
  
  @Override
  public void validate() throws InvalidConfigException {
    if (period < 0) {
      throw new InvalidConfigException("invalid period");
    }
  }
}