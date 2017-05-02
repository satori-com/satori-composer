package com.satori.composer.templates;

import java.io.*;
import java.util.*;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.*;
import freemarker.template.*;

public abstract class JtTransform {
  protected static final JsonNodeFactory nf = JsonNodeFactory.instance;
  protected static final Configuration cfg;
  
  static {
    cfg = new Configuration(Configuration.VERSION_2_3_25);
    cfg.setDefaultEncoding("UTF-8");
    cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
    cfg.setLogTemplateExceptions(false);
    cfg.setTagSyntax(Configuration.SQUARE_BRACKET_TAG_SYNTAX);
  }
  
  // static public methods
  
  public static IJtTransform<JsonNode> from(JsonNode node) throws Exception {
    if (node == null) {
      return ctx -> null;
    } else if (node.isArray()) {
      return fromJsonArray(node);
    } else if (node.isObject()) {
      return fromJsonObject(node);
    } else if (node.isTextual()) {
      return fromJsonText(node);
    }
    return ctx -> node;
  }
  
  public static IJtTransform<String> from(String text) throws Exception {
    Template template = new Template(null, new StringReader(text), cfg);
    return ctx -> {
      StringWriter sw = new StringWriter();
      template.process(ctx, sw);
      return sw.toString();
    };
  }
  
  // static private methods
  
  @SuppressWarnings("unchecked")
  private static IJtTransform<JsonNode> fromJsonArray(JsonNode node) throws Exception {
    IJtTransform<JsonNode>[] array = new IJtTransform[node.size()];
    for (int i = 0; i < array.length; i += 1) {
      array[i] = from(node.get(i));
    }
    return ctx -> {
      final ArrayNode result = nf.arrayNode();
      for (IJtTransform<JsonNode> template : array) {
        result.add(template == null ? null : template.transform(ctx));
      }
      return result;
    };
  }
  
  private static IJtTransform<JsonNode> fromJsonObject(JsonNode node) throws Exception {
    JsonField[] fields = new JsonField[node.size()];
    Iterator<Map.Entry<String, JsonNode>> itor = node.fields();
    int i = 0;
    while (itor.hasNext()) {
      fields[i++] = new JsonField(itor.next());
    }
    return ctx -> {
      final ObjectNode result = nf.objectNode();
      for (JsonField filed : fields) {
        result.replace(
          filed.key, filed.transform(ctx)
        );
      }
      return result;
    };
  }
  
  private static IJtTransform<JsonNode> fromJsonText(JsonNode node) throws Exception {
    Template template = new Template(null, new StringReader(node.asText()), cfg);
    return ctx -> {
      StringWriter sw = new StringWriter();
      template.process(ctx, sw);
      return new TextNode(sw.toString());
    };
  }
  
  private static class JsonField implements IJtTransform<JsonNode> {
    public final String key;
    public final IJtTransform<JsonNode> value;
    
    public JsonField(String key, IJtTransform<JsonNode> value) {
      this.key = key;
      this.value = value;
    }
    
    public JsonField(Map.Entry<String, JsonNode> entry) throws Exception {
      this(entry.getKey(), from(entry.getValue()));
    }
    
    @Override
    public JsonNode transform(TemplateModel ctx) throws Exception {
      return value == null ? null : value.transform(ctx);
    }
  }
}