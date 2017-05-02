package com.satori.composer.stats.ddog;

import com.satori.composer.ddog.*;

import java.util.*;

public class DdogMetric {
  public final String name;
  public final String type;
  
  public final ArrayList<DdogMetricPoint> points;
  
  public DdogMetric(String name, String type) {
    this.name = name;
    this.type = type;
    this.points = new ArrayList<>();
  }
  
  public DdogMetric(String name, String type, float value) {
    this(name, type);
    points.add(new DdogMetricPoint(value));
  }
  
  public DdogMetric(String name, String type, float value, long timestamp) {
    this(name, type);
    points.add(new DdogMetricPoint(value, timestamp));
  }
  
  public void add(float val) {
    add(val, System.currentTimeMillis() / 1000);
  }
  
  public void add(float val, long timestamp) {
    points.add(new DdogMetricPoint(val, timestamp));
  }
  
  public DdogSeries toDdogSeries(String host, String[] tags) {
    DdogSeries result = new DdogSeries();
    result.metric = name;
    result.type = type;
    result.host = host;
    result.tags = tags;
    result.points = new Object[points.size()][2];
    long min = Long.MAX_VALUE;
    long max = Long.MIN_VALUE;
    int i = 0;
    for (DdogMetricPoint p : points) {
      result.points[i][0] = p.timestamp;
      result.points[i][1] = p.value;
      i += 1;
      if (p.timestamp > max) {
        max = p.timestamp;
      }
      if (p.timestamp < min) {
        min = p.timestamp;
      }
    }
    if (points.size() > 0) {
      //result.interval = max-min;
    }
    return result;
  }
  
  
}
