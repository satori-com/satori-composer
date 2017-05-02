package com.satori.mods.core.stats;

import java.util.*;

public class StatsRecorder implements IStatsCollector, IStatsProvider {
  public final ArrayList<IStatsRecord> records = new ArrayList<>();
  
  public StatsRecorder() {
  }
  
  
  // IStatsProvider implementation
  
  
  @Override
  public void collect(IStatsCollector collector) {
    records.forEach(r -> r.collect(collector));
  }
  
  @Override
  public void suppress() {
    records.clear();
  }
  
  
  // IStatsCollector implementation
  
  
  @Override
  public void sum(String aspect, double sum) {
    rec(new StatsSumMetric(aspect, sum));
  }
  
  @Override
  public void avg(String aspect, double sum, long n) {
    rec(new StatsAvgMetric(aspect, sum, n));
  }
  
  @Override
  public void norm(String aspect, double sum, long n, double sumSq) {
    rec(new StatsNormMetric(aspect, sum, n, sumSq));
  }
  
  @Override
  public void series(String aspect, double[] values, int offset, int size) {
    rec(new StatsSeriesMetric(aspect, values, offset, size));
  }
  
  @Override
  public void rec(IStatsRecord rec) {
    records.add(rec);
  }
  
  
}
