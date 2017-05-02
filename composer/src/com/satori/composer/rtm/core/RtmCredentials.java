package com.satori.composer.rtm.core;


public class RtmCredentials {
  public String role;
  public String secret;
  
  public RtmCredentials() {
  }
  
  public RtmCredentials(String role, String secret) {
    this.role = role;
    this.secret = secret;
  }
  
  public RtmCredentials(RtmCredentialsConfig cfg) {
    this(cfg.role, cfg.secret);
  }
}
