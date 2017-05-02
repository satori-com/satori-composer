package com.satori.mods.core.config;

public class InvalidConfigException extends Exception {
  public InvalidConfigException() {
    super();
  }
  
  public InvalidConfigException(String message) {
    super(message);
  }
  
  public InvalidConfigException(String message, Throwable cause) {
    super(message, cause);
  }
}
