package com.satori.composer.logs;

import com.satori.composer.runtime.*;

import com.fasterxml.jackson.annotation.*;


@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class LogRtmError extends JsonExt {
  @JsonProperty("message")
  public String message;
  
  @JsonProperty("stack")
  public String[] stack;
}
