package com.satori.mods.suite;

import com.satori.mods.core.config.*;

import java.util.*;

import com.fasterxml.jackson.annotation.*;

@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class CompositionSettings extends Config {
  
  @JsonProperty("outputs")
  public ArrayList<String> outputs = null;
  
  @JsonProperty("mods")
  public HashMap<String, CompositionNodeConfig> mods = null;
  
  public CompositionSettings() {
  }
  
  @Override
  public void validate() throws InvalidConfigException {
    if (outputs == null) {
      outputs = new ArrayList<>();
    }
    if (mods == null) {
      mods = new HashMap<>();
    }
  }
}
