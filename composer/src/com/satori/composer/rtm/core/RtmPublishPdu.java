package com.satori.composer.rtm.core;

import com.fasterxml.jackson.annotation.*;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class RtmPublishPdu<T> extends RtmPdu<RtmPublishPdu.Body> {
  
  public RtmPublishPdu(String channel, T message, String id) {
    this.action = "rtm/publish";
    this.body = new Body<>(channel, message);
    this.id = id;
  }
  
  public RtmPublishPdu(String channel, T message) {
    this(channel, message, null);
  }
  
  public static class Body<T> extends RtmJsonExt {
    @JsonProperty("channel")
    public String channel;
    
    public T message;
    
    public Body(String channel, T message) {
      this.channel = channel;
      this.message = message;
    }
  }
}
