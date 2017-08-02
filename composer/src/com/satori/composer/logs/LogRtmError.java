package com.satori.composer.logs;

import com.satori.composer.runtime.*;

import com.fasterxml.jackson.annotation.*;


@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class LogRtmError extends JsonExt {
  
  @JsonProperty("message")
  public String message;
  
  @JsonProperty("class")
  public String type;
  
  @JsonProperty("stack")
  public String[] stack;
}
