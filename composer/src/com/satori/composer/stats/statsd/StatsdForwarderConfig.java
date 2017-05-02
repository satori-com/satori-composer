package com.satori.composer.stats.statsd;

import com.satori.mods.core.config.*;

import com.fasterxml.jackson.annotation.*;

@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class StatsdForwarderConfig extends Config {
  public static final long defaultPeriod = 0; // in ms
  public static final String defaultHost = "localhost";
  public static final int defaultPort = 8125;
  
  @JsonProperty("period")
  public long period = defaultPeriod;
  
  @JsonProperty("prefix")
  public String prefix = null;
  
  @JsonProperty("tags")
  public String[] tags = null;
  
  @JsonProperty("host")
  public String host = defaultHost;
  
  @JsonProperty("port")
  public int port = defaultPort;
  
  @Override
  public void validate() throws InvalidConfigException {
    if (host == null || host.isEmpty()) {
      host = defaultHost;
    }
    if (port <= 0) {
      throw new InvalidConfigException("invalid port");
    }
  }
}
