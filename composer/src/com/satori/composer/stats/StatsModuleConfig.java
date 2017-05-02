package com.satori.composer.stats;

import com.satori.composer.stats.console.*;
import com.satori.composer.stats.ddog.*;
import com.satori.composer.stats.http.*;
import com.satori.composer.stats.rtm.*;
import com.satori.composer.stats.statsd.*;
import com.satori.mods.core.config.*;

import com.fasterxml.jackson.annotation.*;

@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class StatsModuleConfig extends Config {
  public static final long defaultPeriod = 1000; // 1 sec., in ms
  
  @JsonProperty("period")
  public long period = defaultPeriod;
  
  @JsonProperty("console")
  public StatsConsoleFrowarderConfig[] console = null;
  
  @JsonProperty("rtm")
  public StatsRtmForwarderConfig[] rtm = null;
  
  @JsonProperty("statsd")
  public StatsdForwarderConfig[] statsd = null;
  
  @JsonProperty("http")
  public StatsHttpForwarderConfig[] http = null;
  
  @JsonProperty("ddog")
  public StatsDdogForwarderConfig[] ddog = null;
  
  @Override
  public void validate() throws InvalidConfigException {
    if (period < 0) {
      throw new InvalidConfigException("invalid period");
    }
    if (console != null) {
      for (StatsConsoleFrowarderConfig c : console) {
        c.validate();
      }
    }
    if (rtm != null) {
      for (StatsRtmForwarderConfig c : rtm) {
        c.validate();
      }
    }
    if (statsd != null) {
      for (StatsdForwarderConfig c : statsd) {
        c.validate();
      }
    }
    if (http != null) {
      for (StatsHttpForwarderConfig c : http) {
        c.validate();
      }
    }
    if (ddog != null) {
      for (StatsDdogForwarderConfig c : ddog) {
        c.validate();
      }
    }
  }
}