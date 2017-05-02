package com.satori.mods.core.stats;


public class StatsAvgMetric extends StatsAvg implements IStatsRecord {
  public final String aspect;
  
  public StatsAvgMetric(String aspect) {
    super(0, 0);
    this.aspect = aspect;
  }
  
  public StatsAvgMetric(String aspect, double sum, long n) {
    super(sum, n);
    this.aspect = aspect;
  }
  
  // IStatsRecord implementation
  
  @Override
  public void collect(IStatsCollector collector) {
    if (collector != null) {
      collector.avg(aspect, this);
    }
  }
}