package com.satori.codegen.groovy;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.*;
import groovy.lang.*;

public class GroovyToJacksonDelegate extends GroovyObjectSupport {
  public final ObjectNode objectNode = GroovyToJackson.nf.objectNode();
  
  @Override
  public Object invokeMethod(String name, Object args) {
    
    if (args == null) {
      throw new RuntimeException("invalid instruction");
    }
    
    if (!Object[].class.isAssignableFrom(args.getClass())) {
      throw new RuntimeException("invalid instruction");
    }
    
    Object[] argArray = (Object[]) args;
    if (argArray.length != 1) {
      throw new RuntimeException("invalid instruction");
    }
    JsonNode result = GroovyToJackson.convert(argArray[0]);
    objectNode.set(name, result);
    return result;
  }
}