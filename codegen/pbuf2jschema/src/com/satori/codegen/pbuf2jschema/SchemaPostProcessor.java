package com.satori.codegen.pbuf2jschema;

import com.satori.codegen.pbuf2jschema.schema.*;

import java.util.*;


public class SchemaPostProcessor {
  
  public final SchemaPostProcessor prev;
  public final HashMap<String, Definition> definitions;
  public final String path;
  
  public SchemaPostProcessor(SchemaPostProcessor prev, HashMap<String, Definition> definitions, String path) {
    this.prev = prev;
    this.definitions = definitions;
    this.path = path;
  }
  
  public SchemaPostProcessor createChild(HashMap<String, Definition> definitions, String path) {
    return new SchemaPostProcessor(
      this, definitions, path
    );
  }
  
  public String resolveRef(String ref) {
    if (definitions == null) {
      return prev.resolveRef(ref);
    }
    Definition def = definitions.get(ref);
    if (def == null) {
      return prev.resolveRef(ref);
    }
    return path + "/" + ref;
  }
  
  public static void processDefinition(String name, Definition def, SchemaPostProcessor ctx) {
    if (def == null) {
      return;
    }
    String path = ctx.path;
    if (name != null && !name.isEmpty()) {
      path = path + "/" + name;
    }
    
    SchemaPostProcessor defsCtx = ctx.createChild(def.definitions, path + "/definitions");
    def.definitions.forEach((n, d) -> {
      processDefinition(n, d, defsCtx);
    });
    
    processDefinition(
      null, def.items, defsCtx.createChild(null, path + "/items")
    );
    
    SchemaPostProcessor propsCtx = defsCtx.createChild(def.properties, path + "/properties");
    def.properties.forEach((n, d) -> {
      processDefinition(n, d, propsCtx);
    });
    
    SchemaPostProcessor oneOfCtx = defsCtx.createChild(null, path + "/oneOf");
    for (int i = 0, n = def.oneOf.size(); i < n; i += 1) {
      processDefinition(null, def.oneOf.get(i), oneOfCtx);
    }
    
    if (def.ref != null) {
      def.ref = ctx.resolveRef(def.ref);
    }
  }
  
  public static Unit process(Unit unit) {
    SchemaPostProcessor rootCtx = new SchemaPostProcessor(null, unit.definitions, "#/definitions");
    unit.definitions.forEach((n, d) -> {
      processDefinition(n, d, rootCtx);
    });
    return unit;
  }
}
