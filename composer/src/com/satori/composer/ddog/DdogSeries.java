package com.satori.composer.ddog;

import com.fasterxml.jackson.annotation.*;

@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class DdogSeries extends DdogExt {
  
  public final static DdogSeries[] EMPTY_ARRAY = new DdogSeries[0];

  public static DdogSeries[] createArray(int size) {
    if (size <= 0) {
      return EMPTY_ARRAY;
    }
    return new DdogSeries[size];
  }
  
  public DdogSeries() {
  }
  
  public DdogSeries(String metric, String type, String host, String[] tags, float value, double epoch) {
    this.metric = metric;
    this.type = type;
    this.host = host;
    this.tags = tags;
    points = new Object[][]{
      {epoch, value}
    };
  }
  
  @JsonProperty("metric")
  public String metric = null;
  
  @JsonProperty("type")
  public String type = null;
  
  @JsonProperty("host")
  public String host = null;
  
  @JsonProperty("tags")
  public String[] tags = null;
  
  
  @JsonProperty("interval")
  public float interval = Float.NaN;
  
  @JsonProperty("points")
  public Object[][] points = null;
  
}