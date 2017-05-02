package com.satori.composer.rtm.core;

import com.satori.mods.core.config.*;

import com.fasterxml.jackson.annotation.*;

@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class RtmCredentialsConfig {
  
  @JsonProperty("role")
  public String role = null;
  
  @JsonProperty("secret")
  public String secret = null;
  
  public RtmCredentialsConfig() {
  }
  
  public RtmCredentialsConfig(String role, String secret) {
    this.role = role;
    this.secret = secret;
  }
  
  public void validate() throws InvalidConfigException {
    if (role == null || role.isEmpty()) {
      throw new InvalidConfigException(this.getClass() + ": role is not specified");
    }
    if (secret == null || secret.isEmpty()) {
      throw new InvalidConfigException(this.getClass() + ": secret is not specified");
    }
  }
}
