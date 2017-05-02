package com.satori.composer.stats.ddog;

import com.satori.composer.ddog.*;
import com.satori.mods.core.config.*;

import java.net.*;

import com.fasterxml.jackson.annotation.*;

public class StatsDdogForwarderConfig extends DdogClientConfig {
  public static final int defaultPeriod = 10_000; // 10 sec., in ms.
  
  @JsonProperty("period")
  public int period = defaultPeriod;
  
  @JsonProperty("prefix")
  public String prefix = null;
  
  @JsonProperty("tags")
  public String[] tags = null;
  
  @JsonProperty("hostname")
  public String hostname = null;
  
  public StatsDdogForwarderConfig() {
    maxPoolSize = 1;
    maxWaitQueueSize = -1;
  }
  
  @Override
  public void validate() throws InvalidConfigException {
    super.validate();
    if (hostname == null || hostname.isEmpty()) {
      try {
        InetAddress ip = InetAddress.getLocalHost();
        //hostname = ip.toString();
        hostname = ip.getHostName();
      } catch (Throwable cause) {
        throw new InvalidConfigException("can't resolve hostname");
      }
    }
    if (period < 0) {
      throw new InvalidConfigException("invalid period");
    }
  }
  
}