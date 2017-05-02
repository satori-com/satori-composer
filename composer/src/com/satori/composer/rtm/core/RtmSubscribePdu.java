package com.satori.composer.rtm.core;

import com.fasterxml.jackson.annotation.*;

@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class RtmSubscribePdu extends RtmPdu<RtmSubscribePdu.Body> {
  
  public static class Body {
    @JsonProperty("channel")
    public String channel;
    
    @JsonProperty("filter")
    public String filter;
    
    @JsonProperty("next")
    public String next;
    
    public Body(String channel, String next) {
      this.channel = channel;
      this.next = next;
    }
    
    public Body(String channel) {
      this(channel, null);
    }
  }
  
  public RtmSubscribePdu(String channel, String id, String next) {
    this.action = "rtm/subscribe";
    this.body = new Body(channel, next);
    this.id = id;
  }
  
  public RtmSubscribePdu(String channel, String id) {
    this(channel, id, null);
  }
  
  public RtmSubscribePdu(String channel) {
    this(channel, null, null);
  }
  
  
}
