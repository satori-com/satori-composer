package com.satori.codegen.pbuf2jschema.schema;

import com.fasterxml.jackson.annotation.*;

@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class Field {
  public final String name;
  public final Definition definition;
  public final boolean required;
  
  public Field(String name, Definition definition, boolean required) {
    this.name = name;
    this.definition = definition;
    this.required = required;
  }
  
  public Field(String name, Definition definition) {
    this(name, definition, false);
  }
}