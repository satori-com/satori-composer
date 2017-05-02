package com.satori.composer.runtime;


import org.slf4j.helpers.*;

public class CheckFailedException extends Exception {
  
  public CheckFailedException() {
  }
  
  public CheckFailedException(String message) {
    super(message);
  }
  
  public CheckFailedException(String message, Object... args) {
    this(MessageFormatter.format(message, args).getMessage());
  }
}
