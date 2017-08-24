package com.satori.mods.suite;

import com.satori.composer.config.*;
import com.satori.mods.core.config.*;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;

@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class HttpPostModSettings extends HttpRequestConfig {
  
  public static final String defaultMethod = "post";
  
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
  
  @JsonProperty("method")
  public String method = null;
  
  @JsonProperty("template")
  public JsonNode template = null;
  
  public HttpPostModSettings() {
  }
  
  @Override
  public void validate() throws InvalidConfigException {
    if (method == null || method.isEmpty()) {
      method = defaultMethod;
    }
    super.validate();
  }
}
