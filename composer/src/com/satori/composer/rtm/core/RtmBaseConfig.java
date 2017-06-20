package com.satori.composer.rtm.core;

import com.satori.composer.config.*;
import com.satori.mods.core.config.*;

import com.fasterxml.jackson.annotation.*;

@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class RtmBaseConfig extends HttpRequestConfig {
  public final static long defaultReconnectDelay = 1000; // in ms, 1 sec.
  public final static int defaultMaxFrameSize = 10 * 1024 * 1024; // in bytes, 10 mb.
  public final static long defaultIdleTimeout = 10_000; // in ms, 10 sec.
  public final static long defaultConnectTimeout = 10_000; // in ms, 10 sec.
  public final static String defaultPath = "/v2";
  
  public RtmBaseConfig() {
  }
  
  public RtmBaseConfig(RtmBaseConfig cfg) {
    super(cfg);
    
    this.reconnectDelay = cfg.reconnectDelay;
    this.maxFrameSize = cfg.maxFrameSize;
    this.idleTimeout = cfg.idleTimeout;
    this.connectTimeout = cfg.connectTimeout;
    this.auth = cfg.auth;
  }
  
  @JsonProperty("reconnect-delay")
  public long reconnectDelay = defaultReconnectDelay;
  
  @JsonProperty("max-frame-size")
  public int maxFrameSize = defaultMaxFrameSize;
  
  @JsonProperty("idle-timeout")
  public long idleTimeout = defaultIdleTimeout;
  
  @JsonProperty("auth")
  public RtmCredentialsConfig auth = null;
  
  @JsonProperty("connect-timeout")
  public long connectTimeout = defaultConnectTimeout;
  
  
  public void validate() throws InvalidConfigException {
    if (path == null) {
      path = defaultPath;
    }
    
    super.validate();
    
    if (reconnectDelay <= 0) {
      throw new InvalidConfigException(this.getClass() + ": invalid reconnect-delay value");
    }
    
    if (maxFrameSize <= 0) {
      throw new InvalidConfigException(this.getClass() + ": invalid max-frame-size value");
    }
    
    if (auth != null) {
      auth.validate();
    }
  }
}