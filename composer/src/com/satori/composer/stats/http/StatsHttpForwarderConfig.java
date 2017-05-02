package com.satori.composer.stats.http;

import com.satori.composer.config.*;
import com.satori.mods.core.config.*;

import java.util.*;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;

@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class StatsHttpForwarderConfig extends HttpRequestConfig {
  public static final long defaultPeriod = 0; // in ms
  public static final String defaultMethod = "POST";
  public static final HashSet<String> allowedMethods = new HashSet<String>() {{
    add("POST");
    add("PUT");
  }};
  
  @JsonProperty("period")
  public long period = defaultPeriod;
  
  @JsonProperty("prefix")
  public String prefix = null;
  
  @JsonProperty("method")
  public String method = defaultMethod;
  
  @JsonProperty("ext")
  public HashMap<String, JsonNode> ext = null;
  
  public void validate() throws InvalidConfigException {
    super.validate();
    if (period < 0) {
      throw new InvalidConfigException("invalid period");
    }
    if (method == null || method.isEmpty()) {
      method = defaultMethod;
    }
    method = method.toUpperCase();
    if (!allowedMethods.contains(method)) {
      throw new InvalidConfigException("invalid method");
    }
  }
}
