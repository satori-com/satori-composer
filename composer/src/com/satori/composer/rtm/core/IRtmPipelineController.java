package com.satori.composer.rtm.core;

public interface IRtmPipelineController {
  void fail(Throwable cause);
  
  default void close() {
    fail(new Exception("pipeline unexpectedly closed"));
  }
}
