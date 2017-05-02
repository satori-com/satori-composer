package com.satori.mods.core.config;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.*;

public abstract class ConfigExt extends Config {
  
  @JsonIgnore
  ObjectNode ext = null;
  
  @JsonAnyGetter
  public ObjectNode ext() {
    return ext;
  }
  
  @JsonAnySetter
  public void ext(String name, JsonNode value) {
    if (ext == null) {
      ext = JsonNodeFactory.instance.objectNode();
    }
    ext.replace(name, value);
  }
  
  
  public JsonNode ext(String name) {
    if (ext == null) {
      return null;
    }
    return ext.get(name);
  }
  
  public void ext(String name, String value) {
    ext(name, JsonNodeFactory.instance.textNode(value));
  }
  
  public void ext(String name, Float value) {
    ext(name, JsonNodeFactory.instance.numberNode(value));
  }
}