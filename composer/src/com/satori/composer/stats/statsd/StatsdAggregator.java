package com.satori.composer.stats.statsd;

import com.satori.mods.core.stats.*;

import java.util.*;

public class StatsdAggregator implements IStatsCollector {
  public final HashMap<String, StatsSumMetric> sum = new HashMap<>();
  public final HashMap<String, StatsAvgMetric> avg = new HashMap<>();
  public boolean isDirty = false;
  
  public StatsdAggregator() {
  }
  
  // IStatsCollector implementation
  
  @Override
  public void sum(String aspect, double sum) {
    isDirty = true;
    StatsSumMetric metric = this.sum.get(aspect);
    if (metric == null) {
      this.sum.put(aspect, new StatsSumMetric(aspect, sum));
      return;
    }
    metric.aggregate(sum);
  }
  
  @Override
  public void avg(String aspect, double sum, long n) {
    isDirty = true;
    StatsAvgMetric metric = this.avg.get(aspect);
    if (metric == null) {
      this.avg.put(aspect, new StatsAvgMetric(aspect, sum, n));
      return;
    }
    metric.aggregate(sum, n);
  }
  
  @Override
  public void norm(String aspect, double sum, long n, double sumSq) {
    avg(aspect, sum, n);
  }
  
  @Override
  public void series(String aspect, double[] values, int offset, int size) {
    double sum = 0.0;
    for (int i = 0; i <= size; i += 1) {
      sum += values[i];
    }
    avg(aspect, sum, size);
  }
  
  // public methods
  
  public boolean isDirty() {
    return isDirty;
  }
  
  public void reset() {
    sum.clear();
    avg.clear();
    isDirty = false;
  }
  
}
