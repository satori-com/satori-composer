package com.satori.codegen.groovy;

import java.util.*;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.*;
import org.slf4j.*;

public class JacksonToGroovy {
  public static final Logger log = LoggerFactory.getLogger(JacksonToGroovy.class);
  
  public static HashMap<String, Object> convert(ObjectNode node) {
    if (node == null) {
      return null;
    }
    HashMap<String, Object> result = new HashMap<>();
    node.fields().forEachRemaining(e -> {
      result.put(e.getKey(), convert(e.getValue()));
    });
    return result;
  }
  
  public static Object[] convert(ArrayNode node) {
    if (node == null) {
      return null;
    }
    Object[] result = new Object[node.size()];
    for (int i = 0; i < node.size(); i += 1) {
      result[i] = convert(node.get(i));
    }
    return result;
  }
  
  public static Object convert(JsonNode node) {
    if (node == null) {
      return null;
    }
    switch (node.getNodeType()) {
      case ARRAY:
        return convert((ArrayNode) node);
      case NULL:
      case MISSING:
        return null;
      case BOOLEAN:
        return node.asBoolean();
      case NUMBER:
        return node.numberValue();
      case OBJECT:
        return convert((ObjectNode) node);
      case STRING:
        return node.asText();
      case BINARY:
        return ((BinaryNode) node).binaryValue();
      case POJO:
        return ((POJONode) node).getPojo();
      default:
        log.error("unexpected json element '{}', conversion skipped", node);
        return node;
    }
  }
}
