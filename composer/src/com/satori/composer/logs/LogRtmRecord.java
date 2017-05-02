package com.satori.composer.logs;

import com.satori.composer.runtime.*;

import com.fasterxml.jackson.annotation.*;


@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class LogRtmRecord extends JsonExt {
  public static final long INVALID_TIMESTAMP = Long.MIN_VALUE;
  
  @JsonProperty("message")
  public String message = null;
  
  @JsonProperty("source")
  public String source = null;
  
  @JsonProperty("level")
  public String level = null;
  
  @JsonProperty("timestamp")
  public long timestamp = INVALID_TIMESTAMP; // in ms.
  
  @JsonProperty("thread")
  public String thread = null;
  
  @JsonProperty("error")
  public LogRtmError error = null;
  
  @Override
  public String toString() {
    return String.format(
      "%s [%s] %s - %s",
      thread, level, source, message
    );
  }
}
