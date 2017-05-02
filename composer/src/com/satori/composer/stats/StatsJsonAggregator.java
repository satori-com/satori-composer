package com.satori.composer.stats;

import com.satori.mods.core.stats.*;

import java.util.*;

import com.fasterxml.jackson.databind.*;

public class StatsJsonAggregator extends StatsAggregator {
  public final String prefix;
  public final HashMap<String, JsonNode> ext;
  
  public StatsJsonAggregator(String prefix, HashMap<String, JsonNode> ext) {
    this.prefix = prefix;
    this.ext = ext;
  }
  
  public StatsJsonMessage drainAsJsonMessage() {
    StatsJsonMessage res = new StatsJsonMessage(prefix, ext);
    sum.forEach((a, m) -> {
      res.records.add(StatsJsonMetric.sum(m));
    });
    avg.forEach((a, m) -> {
      res.records.add(StatsJsonMetric.avg(m));
    });
    norm.forEach((a, m) -> {
      res.records.add(StatsJsonMetric.norm(m));
    });
    series.forEach((a, m) -> {
      res.records.add(StatsJsonMetric.series(m));
    });
    suppress();
    return res;
  }
  
}
