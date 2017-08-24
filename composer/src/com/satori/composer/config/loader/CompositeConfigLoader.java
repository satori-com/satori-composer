package com.satori.composer.config.loader;

import java.util.*;

import com.fasterxml.jackson.databind.*;

/**
 * Load Mod configuration, looking at a few places.
 */
public class CompositeConfigLoader implements ConfigLoader {
  
  private final Iterable<ConfigLoader> loaders;
  
  public CompositeConfigLoader(Iterable<ConfigLoader> loaders) {
    this.loaders = loaders;
  }
  
  public CompositeConfigLoader(ConfigLoader... loaders) {
    this.loaders = Arrays.stream(loaders)::iterator;
  }
  
  @Override
  public JsonNode tryLoad() throws ConfigParsingException {
    return tryLoad(loaders);
  }
  
  /**
   * Load configuration trying all supplied configuration sources in turn.
   */
  public static JsonNode tryLoad(ConfigLoader... loaders) throws ConfigParsingException {
    if (loaders == null) {
      return null;
    }
    return tryLoad(Arrays.stream(loaders)::iterator);
  }
  
  public static JsonNode tryLoad(Iterable<ConfigLoader> loaders) throws ConfigParsingException {
    if (loaders == null) {
      return null;
    }
    for (ConfigLoader l : loaders) {
      if (l == null) {
        continue;
      }
      JsonNode result = l.tryLoad();
      if (result != null) {
        return result;
      }
    }
    return null;
  }
  
  public static JsonNode load(ConfigLoader... loaders) throws Exception {
    JsonNode result = tryLoad(loaders);
    if (result != null) {
      return result;
    }
    throw new Exception("not found");
  }
  
  public static JsonNode load(Iterable<ConfigLoader> loaders) throws Exception {
    JsonNode result = tryLoad(loaders);
    if (result != null) {
      return result;
    }
    throw new Exception("not found");
  }
}
