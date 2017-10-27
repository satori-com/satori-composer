package com.satori.codegen.pbuf2jschema.schema;

import java.util.*;

import com.fasterxml.jackson.annotation.*;

@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public final class Definition extends Entity {
  
  @JsonProperty("description")
  public String description;
  
  @JsonProperty("title")
  public String title;
  
  @JsonProperty("default")
  public Object def;
  
  @JsonProperty("type")
  public String type;
  
  @JsonProperty("format")
  public String format;
  
  @JsonProperty("$ref")
  public String ref;
  
  @JsonProperty("items")
  public Definition items;
  
  @JsonProperty("enum")
  public ArrayList<Object> enumeration = new ArrayList<>();
  
  @JsonProperty("oneOf")
  public ArrayList<Definition> oneOf = new ArrayList<>();
  
  @JsonProperty("readOnly")
  public Boolean readOnly;
  
  @JsonProperty("properties")
  public HashMap<String, Definition> properties = new HashMap<>();
  
  @JsonProperty("required")
  public ArrayList<String> required = new ArrayList<>();
  
  public Definition() {
  }
  
  public Definition(String type) {
    this.type = type;
  }
  
  public Definition(String type, String format) {
    this.type = type;
    this.format = format;
  }
  
  public void addProperty(String name, Definition definition) {
    properties.put(name, definition);
  }
  
  public void addProperty(Field field) {
    addProperty(field.name, field.definition);
    if (field.required) {
      addRequired(field.name);
    }
  }
  
  public void addEnum(Object val) {
    enumeration.add(val);
  }
  
  public void addRequired(String prop) {
    required.add(prop);
  }
  
  public void addEnum(Object... vals) {
    for (Object val : vals) {
      enumeration.add(val);
    }
  }
  
  public void addOneOf(Definition val) {
    oneOf.add(val);
  }
  
  public void addOneOf(Definition... vals) {
    for (Definition val : vals) {
      oneOf.add(val);
    }
  }
  
  public static Definition ref(String ref) {
    Definition res = new Definition();
    res.ref = ref;
    return res;
  }
}