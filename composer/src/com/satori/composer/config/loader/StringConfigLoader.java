package com.satori.composer.config.loader;

import com.satori.mods.core.config.*;

import java.util.function.*;

import com.fasterxml.jackson.databind.*;

public class StringConfigLoader implements ConfigLoader {
  
  private final String string;
  
  public StringConfigLoader(String string) {
    this.string = string;
  }
  
  public StringConfigLoader(Supplier<String> string) {
    this.string = string != null ? string.get() : null;
  }
  
  @Override
  public JsonNode tryLoad() throws ConfigParsingException {
    return tryLoad(string);
  }
  
  public static JsonNode tryLoad(String string) throws ConfigParsingException {
    if (string == null || string.isEmpty()) {
      return null;
    }
    try {
      return Config.mapper.readTree(string);
    } catch (Exception e) {
      throw new ConfigParsingException(e);
    }
  }
  
  public static JsonNode load(String string) throws Exception {
    JsonNode result = tryLoad(string);
    if (result != null) {
      return result;
    }
    throw new Exception("not found");
  }
  
}
