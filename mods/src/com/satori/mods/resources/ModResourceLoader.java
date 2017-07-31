package com.satori.mods.resources;


import com.satori.mods.core.config.*;

import java.io.*;
import java.nio.charset.*;

import com.fasterxml.jackson.databind.*;

public class ModResourceLoader {
  
  public static InputStream tryLoadAsStream(String resourceName) {
    
    if (resourceName == null || resourceName.trim().isEmpty()) {
      return null;
    }
    
    // first try to get specified resource using file system
    
    File file = new File(resourceName);
    if (file.exists()) {
      try {
        return new FileInputStream(file);
      } catch (FileNotFoundException cause) {
        // swallow exception
      }
    }
    
    // then try to get specified resource using class loader
    
    return ModResourceLoader.class.getResourceAsStream(resourceName);
  }
  
  public static String tryLoadAsString(String resourceName) throws Exception {
    try (final InputStream inputStream = loadAsStream(resourceName)) {
      if (inputStream == null) {
        return null;
      }
      StringWriter writer = new StringWriter();
      char[] cbuf = new char[1024];
      try (Reader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8)) {
        int bytesRead;
        while ((bytesRead = reader.read(cbuf, 0, cbuf.length)) > 0) {
          writer.write(cbuf, 0, bytesRead);
        }
      }
      return writer.toString();
    }
  }
  
  public static JsonNode tryLoadAsJson(String resourceName) throws Exception {
    try (final InputStream inputStream = tryLoadAsStream(resourceName)) {
      if (inputStream == null) {
        return null;
      }
      return Config.mapper.readTree(inputStream);
    }
  }
  
  public static <T extends Config> T tryLoadAsConfig(String resourceName, Class<T> cls) throws Exception {
    try (final InputStream inputStream = loadAsStream(resourceName)) {
      if (inputStream == null) {
        return null;
      }
      return Config.parseAndValidate(inputStream, cls);
    }
  }
  
  public static InputStream loadAsStream(String resourceName) throws ModResourceNotFoundException {
    final InputStream res = tryLoadAsStream(resourceName);
    if (res == null) {
      throw new ModResourceNotFoundException(resourceName);
    }
    return res;
  }
  
  public static String loadAsString(String resourceName) throws Exception {
    final String res = tryLoadAsString(resourceName);
    if (res == null) {
      throw new ModResourceNotFoundException(resourceName);
    }
    return res;
  }
  
  
  public static JsonNode loadAsJson(String resourceName) throws Exception {
    final JsonNode res = tryLoadAsJson(resourceName);
    if (res == null) {
      throw new ModResourceNotFoundException(resourceName);
    }
    return res;
  }
  
  public static <T extends Config> T loadAsConfig(String resourceName, Class<T> cls) throws Exception {
    final T res = tryLoadAsConfig(resourceName, cls);
    if (res == null) {
      throw new ModResourceNotFoundException(resourceName);
    }
    return res;
  }
  
  public static <T extends Config> T loadAsConfigAndValidate(String resourceName, Class<T> cls) throws Exception {
    final T res = loadAsConfig(resourceName, cls);
    res.validate();
    return res;
  }
}