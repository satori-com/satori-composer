package com.satori.codegen.pbuf2jschema;

import com.satori.codegen.pbuf2jschema.schema.*;

import java.util.*;


public class FlattenProcessor {
  
  Map<String, String> refMap = new HashMap<>();
  Unit unit = new Unit();
  
  public static Unit process(Unit u) {
    FlattenProcessor ctx = new FlattenProcessor();
    u.definitions.forEach((n, d) -> {
      ctx.process(n, d, "#/definitions/" + n);
    });
    ctx.unit.definitions.forEach((n, d) -> {
      ctx.fixRefs(d);
    });
    ctx.unit.syntax = u.syntax;
    return ctx.unit;
  }
  
  public void process(String name, Definition def, String path) {
    Definition newDef = new Definition(def.type, def.format);
    newDef.ref = def.ref;
    newDef.properties = def.properties;
    newDef.items = def.items;
    newDef.description = def.description;
    newDef.def = def.def;
    newDef.title = def.title;
    newDef.enumeration = def.enumeration;
    newDef.oneOf = def.oneOf;
    
    unit.addDefinition(name, newDef);
    refMap.put(path, name);
    def.definitions.forEach((n, d) -> {
      process(name + "." + n, d, path + "/definitions/" + n);
    });
  }
  
  public void fixRefs(Definition def) {
    if (def == null) {
      return;
    }
    if (def.ref != null) {
      String ref = refMap.get(def.ref);
      if (ref == null) {
        throw new NullPointerException();
      }
      def.ref = "#/definitions/" + ref;
    }
    def.definitions.forEach((n, d) -> {
      fixRefs(d);
    });
    fixRefs(def.items);
    def.properties.forEach((n, d) -> {
      fixRefs(d);
    });
  }
}
