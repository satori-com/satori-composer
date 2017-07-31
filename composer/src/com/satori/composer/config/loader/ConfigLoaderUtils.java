package com.satori.composer.config.loader;

import com.satori.mods.core.config.*;

import java.io.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;

public class ConfigLoaderUtils {
  
  public static JsonNode parseConfigString(String config) throws ConfigParsingException {
    try {
      return Config.mapper.readTree(config);
    } catch (JsonParseException e) {
      throw new ConfigParsingException(
        String.format("Can't parse configuration: '%s'", config), e
      );
    } catch (IOException e) {
      throw new IllegalStateException("IOException while parsing JSON from a String");
    }
  }
}
