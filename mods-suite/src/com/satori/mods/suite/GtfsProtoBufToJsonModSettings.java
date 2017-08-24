package com.satori.mods.suite;

import com.satori.mods.core.config.*;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;

@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class GtfsProtoBufToJsonModSettings extends Config {
  
  @JsonProperty("user-data")
  public JsonNode userData;
  
  public GtfsProtoBufToJsonModSettings() {
  }
  
  @Override
  public void validate() throws InvalidConfigException {
  }
  
}
