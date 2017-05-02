package com.satori.composer.runtime;

import java.util.*;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.*;

public abstract class JsonExt {
  
  @JsonIgnore
  HashMap<String, JsonNode> ext = null;
  
  @JsonAnyGetter
  public HashMap<String, JsonNode> ext() {
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
    return ext.get(name);
  }
  
  public void ext(String name, String value) {
    ext(name, toJson(value));
  }
  
  public void ext(String name, String[] value) {
    ext(name, toJson(value));
  }
  
  public TextNode toJson(String value) {
    if (value == null) {
      return null;
    }
    return JsonNodeFactory.instance.textNode(value);
  }
  
  public ArrayNode toJson(String[] value) {
    if (value == null) {
      return null;
    }
    ArrayNode result = JsonNodeFactory.instance.arrayNode(value.length);
    for (int i = 0; i < value.length; i += 1) {
      result.set(i, toJson(value[i]));
    }
    return result;
  }
  
  // ext typed setters
  
  public void ext(String name, long value) {
    ext(name, JsonNodeFactory.instance.numberNode(value));
  }
  
  public void ext(String name, Long value) {
    ext(name, JsonNodeFactory.instance.numberNode(value));
  }
  
  public void ext(String name, int value) {
    ext(name, JsonNodeFactory.instance.numberNode(value));
  }
  
  public void ext(String name, Integer value) {
    ext(name, JsonNodeFactory.instance.numberNode(value));
  }
  
  public void ext(String name, double value) {
    ext(name, JsonNodeFactory.instance.numberNode(value));
  }
  
  public void ext(String name, Double value) {
    ext(name, JsonNodeFactory.instance.numberNode(value));
  }
  
  public void ext(String name, float value) {
    ext(name, JsonNodeFactory.instance.numberNode(value));
  }
  
  public void ext(String name, Float value) {
    ext(name, JsonNodeFactory.instance.numberNode(value));
  }
  
  public void ext(String name, boolean value) {
    ext(name, JsonNodeFactory.instance.booleanNode(value));
  }
  
  public void ext(String name, Boolean value) {
    ext(name, JsonNodeFactory.instance.booleanNode(value));
  }
  
}