package com.satori.composer.ddog;

import com.fasterxml.jackson.annotation.*;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class DdogEvent extends DdogExt {
  
  public DdogEvent() {
  }
  
  public DdogEvent(String title, String text) {
    this.title = title;
    this.text = text;
  }
  
  public DdogEvent(String title, String text, String[] tags) {
    this.title = title;
    this.text = text;
    this.tags = tags;
  }
  
  /*
  */
  @JsonProperty("title")
  public String title = null;
  
  /*
  */
  @JsonProperty("text")
  public String text = null;
  
  /*
  "normal"
  */
  @JsonProperty("priority")
  public String priority = "normal";
  
  /*
  */
  @JsonProperty("tags")
  public String[] tags = null;
  
  /*
  "info"
  */
  @JsonProperty("alert_type")
  public String alertType = "info";
  
}