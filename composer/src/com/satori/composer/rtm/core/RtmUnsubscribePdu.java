package com.satori.composer.rtm.core;

import com.fasterxml.jackson.annotation.*;

@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class RtmUnsubscribePdu extends RtmPdu<RtmUnsubscribePdu.Body> {
  
  public static class Body {
    @JsonProperty("subscription_id")
    public String subscription;
  
    public Body() {
    }
  
    public Body(String subscription) {
      this.subscription = subscription;
    }
  }
  
  public RtmUnsubscribePdu(String channel, String id) {
    this.action = "rtm/unsubscribe";
    this.body = new Body(channel);
    this.id = id;
  }
  
  public RtmUnsubscribePdu(String channel) {
    this(channel, null);
  }
  
  
}
