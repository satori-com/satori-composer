package com.satori.composer.rtm.core;

public interface IWsPingerContext extends IRtmContext {
  long defaultPingInterval = 10_000; // in ms., 10 sec.
  
  default long pingInterval() {
    return defaultPingInterval;
  }
}
    