package com.satori.codegen.pbuf2jschema.schema;

import java.util.*;

import com.fasterxml.jackson.annotation.*;

@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class Entity {
  public HashMap<String, Definition> definitions = new HashMap<>();
  
  public void addDefinition(Field f) {
    definitions.put(f.name, f.definition);
  }
  
  public void addDefinition(String name, Definition definition) {
    definitions.put(name, definition);
  }
}

