package com.satori.composer.rtm.core;

import java.util.*;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.*;

public class RtmJsonExt {
  
  @JsonIgnore
  HashMap<String, JsonNode> ext = null;
  
  
  @JsonAnyGetter
  public Map<String, JsonNode> ext() {
    return ext;
  }
  
  @JsonAnySetter
  public void ext(String name, JsonNode value) {
    if (ext == null) {
      ext = new HashMap<>();
    }
    ext.put(name, value);
  }
  
  
  public JsonNode ext(String name) {
    if (ext == null) {
      return null;
    }
    return ext.getOrDefault(name, null);
  }
  
  public void ext(String name, String value) {
    ext(name, JsonNodeFactory.instance.textNode(value));
  }
  
  public void ext(String name, Float value) {
    ext(name, JsonNodeFactory.instance.numberNode(value));
  }
  
}