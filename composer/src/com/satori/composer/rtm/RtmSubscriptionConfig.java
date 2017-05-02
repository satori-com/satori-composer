package com.satori.composer.rtm;

import com.satori.mods.core.config.*;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;

@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class RtmSubscriptionConfig extends ConfigExt {
  @JsonProperty("channel")
  public String channel = null;
  
  @JsonProperty("alias")
  public String alias = null;
  
  @JsonProperty("mode")
  public String mode = null;
  
  @JsonProperty("user-data")
  public JsonNode userData = null;
  
  @Override
  public void validate() throws InvalidConfigException {
    
  }
}