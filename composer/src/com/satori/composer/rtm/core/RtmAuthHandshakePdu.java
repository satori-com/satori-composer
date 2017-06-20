package com.satori.composer.rtm.core;

import com.fasterxml.jackson.annotation.*;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class RtmAuthHandshakePdu extends RtmPdu<RtmAuthHandshakePdu.Body> {
  
  public RtmAuthHandshakePdu(String role) {
    this(role, null);
  }
  
  public RtmAuthHandshakePdu(String role, String id) {
    this.action = "auth/handshake";
    this.body = new Body(role);
    this.id = id;
  }
  
  public static class Body extends RtmJsonExt {
    @JsonProperty("method")
    public String method = "role_secret";
    
    @JsonProperty("data")
    public Data data;
    
    public Body(String role) {
      data = new Data(role);
    }
    
    public Body() {
    }
    
    public static class Data {
      @JsonProperty("role")
      public String role;
      
      public Data(String role) {
        this.role = role;
      }
    }
  }
}
