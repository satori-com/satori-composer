package com.satori.mods.suite;

import com.satori.mods.core.config.*;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;

@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class ModConfig extends Config {
  
  @JsonProperty("type")
  public String type = null;
  
  @JsonProperty("settings")
  public JsonNode settings = null;
  
  public ModConfig() {
  }
  
  @Override
  public void validate() throws InvalidConfigException {
    if (type == null || type.isEmpty()) {
      throw new InvalidConfigException("invalid class");
    }
  }
}
