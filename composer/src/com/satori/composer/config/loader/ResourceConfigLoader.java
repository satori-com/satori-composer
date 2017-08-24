package com.satori.composer.config.loader;

import com.satori.mods.core.config.*;
import com.satori.mods.resources.*;

import java.io.*;
import java.util.function.*;

import com.fasterxml.jackson.databind.*;

/**
 * Get Mod configuration from a resource file bundled in the JAR.
 */
public class ResourceConfigLoader implements ConfigLoader {
  
  private final String resourceName;
  private final ClassLoader classLoader;
  
  public ResourceConfigLoader(String resourceName) {
    this(ModResourceLoader.class.getClassLoader(), resourceName);
  }
  
  public ResourceConfigLoader(Supplier<String> resourceName) {
    this(resourceName.get());
  }
  
  public ResourceConfigLoader(ClassLoader classLoader, String resourceName) {
    this.resourceName = resourceName;
    this.classLoader = classLoader;
  }
  
  public ResourceConfigLoader(ClassLoader classLoader, Supplier<String> resourceName) {
    this(classLoader, resourceName.get());
  }
  
  @Override
  public JsonNode tryLoad() throws ConfigParsingException {
    if (resourceName == null || resourceName.isEmpty()) {
      return null;
    }
    
    try (final InputStream inputStream = classLoader.getResourceAsStream(resourceName)) {
      if (inputStream == null) {
        return null;
      }
      return Config.mapper.readTree(inputStream);
    } catch (Exception e) {
      throw new ConfigParsingException(e);
    }
  }
}
