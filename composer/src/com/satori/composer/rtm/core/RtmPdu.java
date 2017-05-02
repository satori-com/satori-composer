package com.satori.composer.rtm.core;

import com.fasterxml.jackson.annotation.*;

@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class RtmPdu<T> extends RtmJsonExt {
  @JsonProperty("action")
  public String action;
  
  @JsonProperty("id")
  public String id;
  
  @JsonProperty("body")
  public T body;
}