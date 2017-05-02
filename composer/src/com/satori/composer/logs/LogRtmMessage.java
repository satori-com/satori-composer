package com.satori.composer.logs;

import com.satori.composer.runtime.*;

import com.fasterxml.jackson.annotation.*;


@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class LogRtmMessage extends JsonExt {
  
  @JsonProperty("prefix")
  public String prefix;
  
  @JsonProperty("tags")
  public String[] tags;
  
  @JsonProperty("records")
  public LogRtmRecord[] records;
  
  public LogRtmMessage() {
  }
  
  public LogRtmMessage(LogRtmRecord[] records, String prefix, String[] tags) {
    this.records = records;
    this.prefix = prefix;
    this.tags = tags;
  }
  
}
