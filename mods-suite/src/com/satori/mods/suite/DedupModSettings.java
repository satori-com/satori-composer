package com.satori.mods.suite;

import com.satori.mods.core.config.*;

import com.fasterxml.jackson.annotation.*;

@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class DedupModSettings extends Config {
  public static final long defaultExpirationInterval = 60_000; // in ms., 1 min
  public static final boolean defaultOverride = false;
  
  @JsonProperty("expiration-interval")
  public long expirationInterval = defaultExpirationInterval;
  
  @JsonProperty("key-selector")
  public String keySelector = null;
  
  @JsonProperty("override")
  public boolean override = defaultOverride;
  
  public DedupModSettings() {
  }
  
  @Override
  public void validate() throws InvalidConfigException {
    if (expirationInterval <= 0) {
      throw new InvalidConfigException("invalid expiration-interval");
    }
  }
}
