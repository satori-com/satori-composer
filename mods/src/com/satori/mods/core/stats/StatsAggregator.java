package com.satori.mods.core.stats;

import java.util.*;

public class StatsAggregator implements IStatsCollector, IStatsProvider {
  public final HashMap<String, StatsSumMetric> sum = new HashMap<>();
  public final HashMap<String, StatsAvgMetric> avg = new HashMap<>();
  public final HashMap<String, StatsNormMetric> norm = new HashMap<>();
  public final HashMap<String, StatsSeriesMetric> series = new HashMap<>();
  public boolean isDirty = false;
  
  public StatsAggregator() {
  }
  
  
  // IStatsProvider implementation
  
  
  @Override
  public void collect(IStatsCollector collector) {
    if (collector == null) {
      return;
    }
    sum.forEach(collector::sum);
    avg.forEach(collector::avg);
    norm.forEach(collector::norm);
    series.forEach(collector::series);
  }
  
  @Override
  public void suppress() {
    sum.clear();
    avg.clear();
    norm.clear();
    series.clear();
    isDirty = false;
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
    isDirty = true;
    StatsNormMetric metric = this.norm.get(aspect);
    if (metric == null) {
      this.norm.put(aspect, new StatsNormMetric(aspect, sum, n, sumSq));
      return;
    }
    metric.aggregate(sum, n, sumSq);
  }
  
  @Override
  public void series(String aspect, double[] values, int offset, int size) {
    isDirty = true;
    StatsSeriesMetric metric = this.series.get(aspect);
    if (metric == null) {
      this.series.put(aspect, new StatsSeriesMetric(aspect, values, offset, size));
      return;
    }
    metric.aggregate(values, offset, size);
  }
  
  // public methods
  
  public boolean isDirty() {
    return isDirty;
  }
  
}
