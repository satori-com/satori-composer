package com.satori.libs.gtfs;

import java.util.*;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.*;

public class GtfsObject {
  
  @JsonIgnore
  protected final HashMap<String, JsonNode> ext = new HashMap<>();
  
  
  @JsonAnyGetter
  public Map<String, JsonNode> ext() {
    return ext;
  }
  
  @JsonAnySetter
  public void ext(String name, JsonNode value) {
    if (value != null) {
      ext.put(name, value);
    } else {
      ext.remove(name);
    }
  }
  
  public JsonNode ext(String name) {
    return ext.get(name);
  }
  
  public void ext(String name, String value) {
    ext(name, value != null ? TextNode.valueOf(value) : null);
  }
  
  public void ext(String name, boolean value) {
    ext(name, BooleanNode.valueOf(value));
  }
  
  public void ext(String name, int value) {
    ext(name, IntNode.valueOf(value));
  }
  
  public void ext(String name, long value) {
    ext(name, LongNode.valueOf(value));
  }
  
  public void ext(String name, float value) {
    ext(name, FloatNode.valueOf(value));
  }
  
  public void ext(String name, double value) {
    ext(name, DoubleNode.valueOf(value));
  }
  
  public void ext(String name, Boolean value) {
    ext(name, value != null ? BooleanNode.valueOf(value) : null);
  }
  
  public void ext(String name, Integer value) {
    ext(name, value != null ? IntNode.valueOf(value) : null);
  }
  
  public void ext(String name, Long value) {
    ext(name, value != null ? LongNode.valueOf(value) : null);
  }
  
  public void ext(String name, Float value) {
    ext(name, value != null ? FloatNode.valueOf(value) : null);
  }
  
  public void ext(String name, Double value) {
    ext(name, value != null ? DoubleNode.valueOf(value) : null);
  }
  
  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    
    GtfsObject that = (GtfsObject) o;
    
    return ext.equals(that.ext);
  }
  
  @Override
  public int hashCode() {
    return ext.hashCode();
  }
}