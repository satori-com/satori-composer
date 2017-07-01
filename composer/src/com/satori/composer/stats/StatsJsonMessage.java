package com.satori.composer.stats;

import java.util.*;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;

@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class StatsJsonMessage extends StatsJsonExt {
  
  @JsonProperty("prefix")
  public String prefix = null;
  
  @JsonProperty("tags")
  public HashMap<String, String> tags = null;
  
  @JsonProperty("metrics")
  public ArrayList<StatsJsonMetric> records = new ArrayList<>();
  
  public StatsJsonMessage() {
  }
  
  public StatsJsonMessage(String prefix, HashMap<String, JsonNode> ext) {
    this.prefix = prefix;
    this.ext = ext;
  }
}
