package com.satori.composer.config.loader;

import com.satori.mods.core.config.*;

import java.io.*;
import java.nio.file.*;

import com.fasterxml.jackson.databind.*;

public interface ConfigLoader {
  
  JsonNode tryLoad() throws ConfigParsingException;
  
  default JsonNode load() throws Exception {
    JsonNode result = tryLoad();
    if (result != null) {
      return result;
    }
    throw new Exception("not found");
  }
  
  static ConfigLoader fromFile(Path path) {
    return () -> {
      if (path == null) {
        return null;
      }
      try (final Reader reader = Files.newBufferedReader(path)) {
        return Config.mapper.readTree(reader);
      } catch (FileNotFoundException e) {
        return null;
      } catch (Exception e) {
        throw new ConfigParsingException(e);
      }
    };
  }
  
  
}
