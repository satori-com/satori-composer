package com.satori.composer.templates;

import java.util.*;
import java.util.function.*;

import com.fasterxml.jackson.databind.*;
import freemarker.template.*;

public class JtModel implements TemplateScalarModel, TemplateHashModelEx2 {
  final JsonNode node;
  
  public JtModel(JsonNode node) {
    this.node = node;
  }
  
  public static TemplateModel wrap(JsonNode node) {
    if (node == null) {
      return null;
    }
    if (node.isTextual()) {
      return new JtString(node.asText());
    } else if (node.isNumber()) {
      final Number val = node.numberValue();
      return (TemplateNumberModel) () -> val;
    } else if (node.isBoolean()) {
      final boolean val = node.booleanValue();
      return (TemplateBooleanModel) () -> val;
    } else if (node.isArray()) {
      return new TemplateSequenceModel() {
        @Override
        public TemplateModel get(int index) throws TemplateModelException {
          return wrap(node.get(index));
        }
        
        @Override
        public int size() throws TemplateModelException {
          return node.size();
        }
      };
    }
    return new JtModel(node);
  }
  
  @Override
  public TemplateModel get(String key) throws TemplateModelException {
    JsonNode childNode = node.get(key.toString());
    if (childNode == null) {
      return null;
    }
    return wrap(childNode);
  }
  
  @Override
  public boolean isEmpty() throws TemplateModelException {
    return node.size() == 0;
  }
  
  @Override
  public int size() throws TemplateModelException {
    return node.size();
  }
  
  @Override
  public TemplateCollectionModel keys() throws TemplateModelException {
    return () -> new TemplateModelIterator() {
      Iterator<String> itor = node.fieldNames();
      
      @Override
      public TemplateModel next() throws TemplateModelException {
        return new JtString(itor.next());
      }
      
      @Override
      public boolean hasNext() throws TemplateModelException {
        return itor.hasNext();
      }
    };
  }
  
  @Override
  public TemplateCollectionModel values() throws TemplateModelException {
    return () -> new TemplateModelIterator() {
      Iterator<JsonNode> itor = node.elements();
      
      @Override
      public TemplateModel next() throws TemplateModelException {
        return wrap(itor.next());
      }
      
      @Override
      public boolean hasNext() throws TemplateModelException {
        return itor.hasNext();
      }
    };
  }
  
  @Override
  public KeyValuePairIterator keyValuePairIterator() throws TemplateModelException {
    return new KeyValuePairIterator() {
      Iterator<Map.Entry<String, JsonNode>> itor = node.fields();
      
      @Override
      public KeyValuePair next() throws TemplateModelException {
        Map.Entry<String, JsonNode> entry = itor.next();
        return new KeyValuePair() {
          String key = entry.getKey();
          JsonNode value = entry.getValue();
          
          @Override
          public TemplateModel getKey() throws TemplateModelException {
            return new JtString(key);
          }
          
          @Override
          public TemplateModel getValue() throws TemplateModelException {
            return wrap(value);
          }
        };
      }
      
      @Override
      public boolean hasNext() throws TemplateModelException {
        return itor.hasNext();
      }
    };
  }
  
  @Override
  public String getAsString() throws TemplateModelException {
    return toString();
  }
  
  @Override
  public String toString() {
    return node.asText();
  }
  
  public void forEach(BiConsumer<String, TemplateModel> body) {
    for (Map.Entry<String, JsonNode> entry : (Iterable<? extends Map.Entry<String, JsonNode>>) node::fields) {
      body.accept(entry.getKey(), wrap(entry.getValue()));
    }
  }
}
