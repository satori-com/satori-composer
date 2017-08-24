package com.satori.mods.suite;

import com.satori.composer.config.*;
import com.satori.mods.core.config.*;

import com.fasterxml.jackson.annotation.*;

@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class HttpPollModSettings extends HttpRequestConfig {
  
  @JsonProperty("connect-timeout")
  public int connectTimeout = 60_000; //1 min., in ms.
  
  @JsonProperty("compression")
  public boolean compression = false;
  
  @JsonProperty("max-pool-size")
  public int maxPoolSize = 1;
  
  @JsonProperty("idle-timeout")
  public int idleTimeout = 600; // 10 min., in sec.
  
  @JsonProperty("max-wait-queue-size")
  public int maxWaitQueueSize = -1; //unbound
  
  @JsonProperty("delay")
  public long delay = 1000;
  
  @JsonProperty("error-delay")
  public long errorDelay = Long.MIN_VALUE;
  
  @JsonProperty("format")
  public String format = null;
  
  @JsonProperty("disable-etag")
  public boolean disableEtag = false;
  
  @JsonProperty("disable-last-modified")
  public boolean disableLastModified = false;
  
  
  @JsonProperty("keep-alive")
  public boolean keepAlive = true;
  
  @JsonProperty("pipelining")
  public boolean pipelining = true;
  
  public HttpPollModSettings() {
  }
  
  @Override
  public void validate() throws InvalidConfigException {
    if (delay <= 0) {
      throw new InvalidConfigException("invalid delay");
    }
    if (errorDelay == Long.MIN_VALUE) {
      errorDelay = delay;
    } else if (errorDelay <= 0) {
      throw new InvalidConfigException("invalid error-delay");
    }
    super.validate();
  }
}
