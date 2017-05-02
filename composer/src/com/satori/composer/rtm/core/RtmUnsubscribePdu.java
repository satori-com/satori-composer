package com.satori.composer.rtm.core;

import com.fasterxml.jackson.annotation.*;

@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class RtmUnsubscribePdu extends RtmPdu<RtmUnsubscribePdu.Body> {
  
  public static class Body {
  }
  
  public RtmUnsubscribePdu(String channel, String id) {
    this.action = "rtm/unsubscribe";
    this.body = null;
    this.id = id;
  }
  
  public RtmUnsubscribePdu(String channel) {
    this(channel, null);
  }
  
  
}
