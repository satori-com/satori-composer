package com.satori.codegen.pbuf2jschema.schema;

import com.fasterxml.jackson.annotation.*;

@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class Unit extends Entity {
  public String syntax;
  
  public void addDefinition(Field f) {
    definitions.put(f.name, f.definition);
  }
}

