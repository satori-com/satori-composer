package com.satori.composer.rtm.core;

public interface IRtmAuthenticatorContext extends IRtmContext {
  public final static long defaultAuthenticateTimeout = 5_000; // in ms., 5 sec.
  
  default RtmCredentials auth() {
    return null;
  }
  
  default long authenticateTimeout() {
    return defaultAuthenticateTimeout;
  }
}
    