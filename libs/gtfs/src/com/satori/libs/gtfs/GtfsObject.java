package com.satori.libs.gtfs;

import java.util.*;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.*;

// TODO: doesn't support deep copy
public class GtfsObject {
  
  @JsonIgnore
  protected HashMap<String, JsonNode> ext = null;
  
  
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
  
  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    
    GtfsObject that = (GtfsObject) o;
    
    return ext != null ? ext.equals(that.ext) : that.ext == null;
  }
  
  @Override
  public int hashCode() {
    return ext != null ? ext.hashCode() : 0;
  }
}