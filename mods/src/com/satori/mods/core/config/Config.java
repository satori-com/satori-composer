package com.satori.mods.core.config;

import java.io.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.datatype.jdk8.*;
import com.fasterxml.jackson.module.afterburner.*;

public abstract class Config {
  public static final ObjectMapper mapper = new ObjectMapper()
    .configure(JsonParser.Feature.ALLOW_COMMENTS, true)
    .configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true)
    .configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true)
    .configure(JsonParser.Feature.ALLOW_TRAILING_COMMA, true)
    .disable(JsonGenerator.Feature.AUTO_CLOSE_TARGET)
    .enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    .registerModule(new AfterburnerModule())
    .registerModule(new Jdk8Module());
  
  public abstract void validate() throws InvalidConfigException;
  
  public static <T extends Config> T parse(InputStream stream, Class<T> cls) throws Exception {
    return mapper.readValue(stream, cls);
  }
  
  public static <T extends Config> T parse(JsonNode tree, Class<T> cls) throws Exception {
    return mapper.treeToValue(tree, cls);
  }
  
  public static <T extends Config> T parseAndValidate(InputStream stream, Class<T> cls) throws Exception {
    if (stream == null) {
      return null;
    }
    T result = parse(stream, cls);
    result.validate();
    return result;
  }
  
  public static <T extends Config> T parseAndValidate(JsonNode tree, Class<T> cls) throws Exception {
    if (tree == null) {
      return null;
    }
    T result = parse(tree, cls);
    result.validate();
    return result;
  }
  
  
  @Override
  public String toString() {
    try {
      return mapper.writeValueAsString(this);
    } catch (JsonProcessingException e) {
      return e.getMessage();
    }
  }
}