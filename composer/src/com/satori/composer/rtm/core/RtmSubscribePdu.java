package com.satori.composer.rtm.core;

import java.util.*;

import com.fasterxml.jackson.annotation.*;

@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class RtmSubscribePdu extends RtmPdu<RtmSubscribePdu.Body> {
  
  public static class Body {
    @JsonProperty("channel")
    public String channel;
  
    @JsonProperty("prefix")
    public boolean prefix = false;
    
    @JsonProperty("subscription_id")
    public String subscription;
    
    @JsonProperty("filter")
    public String filter;
    
    @JsonProperty("position")
    public String next;

    @JsonProperty("history")
    public Map history;
  
    public Body(String channel, boolean prefix, String filter, String next, Map history) {
      if (filter != null && !filter.isEmpty()) {
        this.filter = filter;
        this.subscription = channel;
      } else {
        this.channel = channel;
      }
      if(prefix) {
        this.subscription = channel;
      }
      this.prefix = prefix;
      this.next = next;
      this.history = history;
    }
  }
  
  public RtmSubscribePdu(String channel, boolean prefix, String filter, String id, String next, Map history) {
    this.action = "rtm/subscribe";
    this.body = new Body(channel, prefix, filter, next, history);
    this.id = id;
  }
}
