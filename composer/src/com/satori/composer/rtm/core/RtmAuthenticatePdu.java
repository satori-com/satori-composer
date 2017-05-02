package com.satori.composer.rtm.core;

import com.fasterxml.jackson.annotation.*;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class RtmAuthenticatePdu extends RtmPdu<RtmAuthenticatePdu.Body> {
  
  public RtmAuthenticatePdu(String hash) {
    this(hash, null);
  }
  
  public RtmAuthenticatePdu(String hash, String id) {
    this.action = "auth/authenticate";
    this.body = new Body(hash);
    this.id = id;
  }
  
  public static class Body extends RtmJsonExt {
    @JsonProperty("method")
    public String method = "role_key";
    
    @JsonProperty("credentials")
    public Credentials credentials;
    
    public Body(String hash) {
      credentials = new Credentials(hash);
    }
    
    public Body() {
    }
    
    public static class Credentials {
      @JsonProperty("hash")
      public String hash;
      
      public Credentials(String hash) {
        this.hash = hash;
      }
    }
  }
  
  
}
