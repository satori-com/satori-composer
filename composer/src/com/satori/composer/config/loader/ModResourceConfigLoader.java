package com.satori.composer.config.loader;

import com.satori.mods.resources.*;

import java.util.function.*;

import com.fasterxml.jackson.databind.*;

/**
 * Get Mod configuration from a resource file bundled in the JAR.
 */
public class ModResourceConfigLoader implements ConfigLoader {
  
  private final String resourceName;
  
  public ModResourceConfigLoader(String resourceName) {
    this.resourceName = resourceName;
  }
  
  public ModResourceConfigLoader(Supplier<String> resourceName) {
    this.resourceName = resourceName != null ? resourceName.get() : null;
  }
  
  @Override
  public JsonNode tryLoad() throws ConfigParsingException {
    if(resourceName == null || resourceName.isEmpty()){
      return null;
    }
    try {
      return ModResourceLoader.tryLoadAsJson(resourceName);
    } catch (Exception e) {
      throw new ConfigParsingException(e);
    }
  }
}
