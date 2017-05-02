package com.satori.composer.ddog;

import com.satori.composer.config.*;
import com.satori.mods.core.config.*;

import com.fasterxml.jackson.annotation.*;

@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class DdogClientConfig extends HttpRequestConfig {
  public static final String defaultHost = "app.datadoghq.com";
  public static final boolean defaultSsl = true;
  public static final String defaultPath = "/api/v1/";
  
  @JsonProperty("connectTimeout")
  public int connectTimeout = 60_000; //1 min., in ms.
  
  @JsonProperty("compression")
  public boolean compression = false;
  
  @JsonProperty("max-pool-size")
  public int maxPoolSize = 100;
  
  @JsonProperty("idle-timeout")
  public int idleTimeout = 600; // 10 min., in sec.
  
  @JsonProperty("max-wait-queue-size")
  public int maxWaitQueueSize = -1; //unbound
  
  public DdogClientConfig() {
    ssl = defaultSsl;
  }
  
  @Override
  public void validate() throws InvalidConfigException {
    if (host == null || host.isEmpty()) {
      host = defaultHost;
    }
    if (path == null || path.isEmpty()) {
      path = defaultPath;
    }
    super.validate();
  }
}
