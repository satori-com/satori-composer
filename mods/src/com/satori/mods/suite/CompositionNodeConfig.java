package com.satori.mods.suite;

import com.satori.mods.core.config.*;

import java.util.*;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.*;

@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class CompositionNodeConfig extends ModConfig {
  
  @JsonProperty("connectors")
  @JsonDeserialize(using = ConnectorsConfigDeserializer.class)
  public HashMap<String, ArrayList<String>> connectors = null;
  
  public CompositionNodeConfig() {
  }
  
  @Override
  public void validate() throws InvalidConfigException {
    super.validate();
    
    if (connectors == null) {
      throw new InvalidConfigException("invalid connectors");
    }
  }
}

