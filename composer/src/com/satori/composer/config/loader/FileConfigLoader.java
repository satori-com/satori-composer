package com.satori.composer.config.loader;

import com.satori.mods.core.config.*;

import java.io.*;
import java.nio.file.*;

import com.fasterxml.jackson.databind.*;

public class FileConfigLoader implements ConfigLoader {
  
  private final Path path;
  
  public FileConfigLoader(Path path) {
    this.path = path;
  }
  
  @Override
  public JsonNode tryLoad() throws ConfigParsingException {
    if (path == null) {
      return null;
    }
    File file = path.toFile();
    if(!file.exists() || !file.isFile()){
      return null;
    }
    try (final Reader reader = Files.newBufferedReader(path)) {
      return Config.mapper.readTree(reader);
    } catch (FileNotFoundException e) {
      return null;
    } catch (Exception e) {
      throw new ConfigParsingException(e);
    }
  }
}
