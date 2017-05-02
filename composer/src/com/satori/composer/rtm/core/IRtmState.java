package com.satori.composer.rtm.core;

import com.satori.composer.runtime.*;

public interface IRtmState<TExit> extends IPulseObject, IRtm {
  
  long created();
  
  default long age() {
    return now() - created();
  }
  
  default long age(long timestamp) {
    return timestamp - created();
  }
  
  default long now() {
    return Stopwatch.timestamp();
  }
  
  default boolean completed() {
    return succeeded() || failed();
  }
  
  boolean succeeded();
  
  boolean failed();
  
  TExit result();
  
  Throwable cause();
  
  boolean enter(Runnable cont);
  
  boolean dispose();
}
