package com.satori.composer.config.loader;

public class ConfigParsingException extends Exception {
  
  ConfigParsingException() {
    super(createErrorMessage(null));
  }
  
  ConfigParsingException(String config) {
    super(createErrorMessage(config));
  }
  
  public ConfigParsingException(String config, Throwable cause) {
    super(createErrorMessage(config), cause);
  }
  
  public ConfigParsingException(Throwable cause) {
    super(createErrorMessage(null), cause);
  }
  
  private static String createErrorMessage(String config){
    if(config != null){
      return String.format("Can't parse configuration: '%s'", config);
    }
  
    return "Can't parse configuration";
  }
}
