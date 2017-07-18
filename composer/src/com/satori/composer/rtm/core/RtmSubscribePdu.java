package com.satori.composer.rtm.core;

import com.fasterxml.jackson.annotation.*;

@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class RtmSubscribePdu extends RtmPdu<RtmSubscribePdu.Body> {
  
  public static class Body {
    @JsonProperty("channel")
    public String channel;
    
    @JsonProperty("subscription_id")
    public String subscription;
    
    @JsonProperty("filter")
    public String filter;
    
    @JsonProperty("position")
    public String next;
    
    public Body(String channel, String filter, String next) {
      if (filter != null && !filter.isEmpty()) {
        this.filter = filter;
        this.subscription = channel;
      } else {
        this.channel = channel;
      }
      this.next = next;
    }
    
    public Body(String channel, String filter) {
      this(channel, filter, null);
    }
  }
  
  public RtmSubscribePdu(String channel, String filter, String id, String next) {
    this.action = "rtm/subscribe";
    this.body = new Body(channel, filter, next);
    this.id = id;
  }
  
  public RtmSubscribePdu(String channel, String filter, String id) {
    this(channel, filter, id, null);
  }
  
  public RtmSubscribePdu(String channel, String filter) {
    this(channel, filter, null, null);
  }
  
  
}
