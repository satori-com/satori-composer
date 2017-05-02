package com.satori.composer.stats.ddog;

import com.satori.composer.ddog.*;
import com.satori.mods.core.stats.*;

import java.util.*;

public class StatsDdogAggregator implements IStatsCollector {
  public final HashMap<String, StatsSum> sums = new HashMap<>();
  public final HashMap<String, StatsAvg> avgs = new HashMap<>();
  public final String prefix;
  public final String hostname;
  public final String[] tags;
  public boolean isDirty = false;
  
  public StatsDdogAggregator(String prefix, String hostname, String[] tags) {
    this.prefix = prefix == null || prefix.isEmpty() ? null : prefix + ".";
    this.hostname = hostname;
    this.tags = tags;
  }
  
  // IStatsCollector implementation
  
  @Override
  public void sum(String aspect, double val) {
    isDirty = true;
    StatsSum metric = sums.get(aspect);
    if (metric == null) {
      sums.put(aspect, new StatsSum(val));
      return;
    }
    metric.aggregate(val);
  }
  
  @Override
  public void avg(String aspect, double sum, long n) {
    isDirty = true;
    StatsAvg metric = avgs.get(aspect);
    if (metric == null) {
      avgs.put(aspect, new StatsAvg(sum, n));
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
  
  public DdogSeries[] drainAsDdogSeries(long elapsed) {
    int totalSize = sums.size();
    totalSize += avgs.size();
    double elapsedSec = 0.001 * elapsed;
    
    DdogSeries[] result = new DdogSeries[totalSize];
    int i = 0;
    double epoch = 0.001 * System.currentTimeMillis();
    for (Map.Entry<String, StatsSum> entry : sums.entrySet()) {
      StatsSum m = entry.getValue();
      float val = (float) (m.sum / elapsedSec);
      String aspect = entry.getKey();
      if (prefix != null) {
        aspect = prefix + aspect;
      }
      result[i++] = new DdogSeries(
        aspect, DdogMetricTypes.counter, hostname, tags, val, epoch
      );
    }
    for (Map.Entry<String, StatsAvg> entry : avgs.entrySet()) {
      StatsAvg m = entry.getValue();
      float val = (float) (m.sum / m.n);
      String aspect = entry.getKey();
      if (prefix != null) {
        aspect = prefix + aspect;
      }
      result[i++] = new DdogSeries(
        aspect, DdogMetricTypes.gauge, hostname, tags, val, epoch
      );
    }
    reset();
    return result;
  }
  
  public void reset() {
    sums.clear();
    avgs.clear();
    isDirty = false;
  }
  
  public boolean isDirty() {
    return isDirty;
  }
  
}
