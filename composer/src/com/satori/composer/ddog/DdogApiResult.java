package com.satori.composer.ddog;

public class DdogApiResult {
  
  
  public int code;
  public String message;
  public String content;
  
  public DdogApiResult(int code, String message, String content) {
    this.code = code;
    this.message = message;
    this.content = content;
  }
}