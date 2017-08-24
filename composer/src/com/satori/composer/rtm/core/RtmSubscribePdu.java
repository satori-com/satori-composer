package com.satori.composer.rtm.core;

import java.util.*;

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

    @JsonProperty("history")
    public Map history;
    
    public Body(String channel, String filter, String next, Map history) {
      if (filter != null && !filter.isEmpty()) {
        this.filter = filter;
        this.subscription = channel;
      } else {
        this.channel = channel;
      }
      this.next = next;
      this.history = history;
    }
    
    public Body(String channel, String filter) {
      this(channel, filter, null, null);
    }
  }
  
  public RtmSubscribePdu(String channel, String filter, String id, String next, Map history) {
    this.action = "rtm/subscribe";
    this.body = new Body(channel, filter, next, history);
    this.id = id;
  }
  
  public RtmSubscribePdu(String channel, String filter, String id, String next) {
    this(channel, filter, id, next, null);
  }
  
  public RtmSubscribePdu(String channel, String filter, String id) {
    this(channel, filter, id, null);
  }
  
  public RtmSubscribePdu(String channel, String filter) {
    this(channel, filter, null, null);
  }
  
  
}
