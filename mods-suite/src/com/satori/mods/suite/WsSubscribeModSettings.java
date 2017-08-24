package com.satori.mods.suite;

import com.satori.composer.config.*;
import com.satori.mods.core.config.*;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;

@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class WsSubscribeModSettings extends HttpRequestConfig {
  
  @JsonProperty("connectTimeout")
  public int connectTimeout = 60_000; //1 min., in ms.
  
  @JsonProperty("compression")
  public boolean compression = false;
  
  @JsonProperty("idle-timeout")
  public int idleTimeout = 600; // 10 min., in sec.
  
  @JsonProperty("error-delay")
  public long errorDelay = 1000; // in ms., 1 sec.
  
  @JsonProperty("format")
  public String format = null;
  
  @JsonProperty("user-data")
  public JsonNode userData;
  
  public WsSubscribeModSettings() {
  }
  
  @Override
  public void validate() throws InvalidConfigException {
    if (errorDelay <= 0) {
      throw new InvalidConfigException("invalid error-delay");
    }
    super.validate();
  }
}
