package com.satori.codegen.groovy;

import java.util.*;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.*;
import groovy.lang.*;
import org.slf4j.*;

public class GroovyToJackson {
  public static final Logger log = LoggerFactory.getLogger(GroovyToJackson.class);
  public static final JsonNodeFactory nf = JsonNodeFactory.instance;
  
  public static JsonNode convert(Object val) {
    if (val == null) {
      return NullNode.getInstance();
    }
    
    if (val instanceof JsonNode) {
      return (JsonNode) val;
    }
    
    if (val instanceof Closure<?>) {
      Closure<?> closure = (Closure<?>) val;
      GroovyToJacksonDelegate d = new GroovyToJacksonDelegate();
      Closure<?> cloned = (Closure<?>) closure.clone();
      cloned.setDelegate(d);
      cloned.setResolveStrategy(Closure.DELEGATE_FIRST);
      cloned.call();
      return d.objectNode;
    }
    
    if (val instanceof String) {
      String text = (String) val;
      return TextNode.valueOf(text);
    }
    
    if (val instanceof GString) {
      GString text = (GString) val;
      return TextNode.valueOf(text.toString());
    }
    
    if (val instanceof Long) {
      Long num = (Long) val;
      return LongNode.valueOf(num);
    }
    
    if (val instanceof Integer) {
      Integer num = (Integer) val;
      return IntNode.valueOf(num);
    }
    
    if (val instanceof Double) {
      Double num = (Double) val;
      return DoubleNode.valueOf(num);
    }
    
    if (val instanceof Float) {
      Float num = (Float) val;
      return FloatNode.valueOf(num);
    }
    
    if (val instanceof Boolean) {
      Boolean num = (Boolean) val;
      return BooleanNode.valueOf(num);
    }
    
    if (val instanceof Map<?,?>) {
      Map<?,?> map = (Map<?,?>) val;
      ObjectNode result = nf.objectNode();
      map.forEach((k,v)->{
        if(k.equals("enum") ){
          result.set(k.toString(), convert(v));
        }else {
          result.set(k.toString(), convert(v));
        }
        
      });
      return result;
    }
  
    if (val instanceof Object[]) {
      Object[] array = (Object[])val;
      ArrayNode result = nf.arrayNode(array.length);
      for(Object v: array){
        result.add(convert(v));
      }
      return result;
    }
  
    if (val instanceof List) {
      List array = (List)val;
      ArrayNode result = nf.arrayNode(array.size());
      for(Object v: array){
        result.add(convert(v));
      }
      return result;
    }
    
    throw new RuntimeException("invalid instruction");
  }
  
}
