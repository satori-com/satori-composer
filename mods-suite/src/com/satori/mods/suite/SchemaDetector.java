package com.satori.mods.suite;

import java.util.*;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.module.jsonSchema.*;
import com.fasterxml.jackson.module.jsonSchema.types.*;
import org.slf4j.*;

public class SchemaDetector {
  public static Logger log = LoggerFactory.getLogger(SchemaDetector.class);
  public JsonSchema schema = null;
  
  public boolean process(JsonNode data) {
    JsonSchema res = process(schema, data);
    if (res == null) {
      return false;
    }
    schema = res;
    return true;
  }
  
  public static JsonSchema processObject(JsonSchema schema, JsonNode data) {
    ObjectSchema result = null;
    
    final ObjectSchema objectSchema;
    if (schema == null) {
      objectSchema = new ObjectSchema();
    } else if (!(schema instanceof ObjectSchema)) {
      log.warn("union type detected...");
      return new AnySchema();
    } else {
      objectSchema = (ObjectSchema) schema;
    }
    
    for (Map.Entry<String, JsonNode> e : (Iterable<Map.Entry<String, JsonNode>>) data::fields) {
      String k = e.getKey();
      JsonNode v = e.getValue();
      JsonSchema res = process(objectSchema.getProperties().get(k), v);
      if (res != null) {
        objectSchema.putOptionalProperty(k, res);
        result = objectSchema;
      }
    }
    
    return result;
  }
  
  public static JsonSchema processTextual(JsonSchema schema, JsonNode data) {
    if (schema == null) {
      return new StringSchema();
    }
    if (schema instanceof StringSchema) {
      return null;
    }
    log.warn("union type detected...");
    return new AnySchema();
  }
  
  public static JsonSchema processNumber(JsonSchema schema, JsonNode data) {
    if (schema == null) {
      return data.isIntegralNumber() ? new IntegerSchema() : new NumberSchema();
    }
    if (!(schema instanceof NumberSchema)) {
      log.warn("union type detected...");
      return new AnySchema();
    }
    
    if(data.isIntegralNumber()){
      if(schema instanceof IntegerSchema){
        return null;
      }
      return new NumberSchema();
    }
  
    if(schema instanceof IntegerSchema){
      return new NumberSchema();
    }
  
    return null;
  }
  
  public static JsonSchema processBoolean(JsonSchema schema, JsonNode data) {
    if (schema == null) {
      return new BooleanSchema();
    }
    if (schema instanceof BooleanSchema) {
      return null;
    }
    log.warn("union type detected...");
    return new AnySchema();
  }
  
  public static JsonSchema process(JsonSchema schema, JsonNode data) {
    if (schema instanceof AnySchema) {
      return schema;
    }
    
    if (data.isObject()) {
      return processObject(schema, data);
    }
    
    if (data.isTextual()) {
      return processTextual(schema, data);
    }
    
    if (data.isNumber()) {
      return processNumber(schema, data);
    }
    
    if (data.isBoolean()) {
      return processBoolean(schema, data);
    }
    
    
    log.warn("unsupported type detected...");
    return new AnySchema();
  }
  
}
