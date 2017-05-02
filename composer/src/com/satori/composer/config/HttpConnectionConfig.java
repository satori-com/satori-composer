package com.satori.composer.config;

import com.satori.mods.core.config.*;

import com.fasterxml.jackson.annotation.*;

@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class HttpConnectionConfig extends Config {
  public static final int INVALID_PORT = -1;
  public static final boolean defaultVerifyHost = true;
  public static final boolean defaultTrustAll = true;
  
  @JsonProperty("host")
  public String host = null;
  
  @JsonProperty("port")
  public int port = INVALID_PORT;
  
  @JsonProperty("ssl")
  public boolean ssl = false;
  
  @JsonProperty("verify-host")
  public boolean verifyHost = defaultVerifyHost;
  
  @JsonProperty("trust-all")
  public boolean trustAll = defaultTrustAll;
  
  
  public HttpConnectionConfig() {
  }
  
  public HttpConnectionConfig(String host) {
    this.host = host;
  }
  
  public HttpConnectionConfig(HttpConnectionConfig cfg) {
    this.host = cfg.host;
    this.port = cfg.port;
    this.ssl = cfg.ssl;
  }
  
  @Override
  public void validate() throws InvalidConfigException {
    if (host == null || host.isEmpty()) {
      throw new InvalidConfigException("host is not specified");
    }
    if (port == INVALID_PORT) {
      port = ssl ? 443 : 80;
    } else if (port < 0) {
      throw new InvalidConfigException("invalid port");
    }
  }
}
