package com.satori.mods.core.stats;


public class StatsNormMetric extends StatsNorm implements IStatsRecord {
  public final String aspect;
  
  public StatsNormMetric(String aspect) {
    super(0, 0, 0);
    this.aspect = aspect;
  }
  
  public StatsNormMetric(String aspect, double sum, long n, double sumSq) {
    super(sum, n, sumSq);
    this.aspect = aspect;
  }
  
  // IStatsRecord implementation
  
  @Override
  public void collect(IStatsCollector collector) {
    if (collector != null) {
      collector.norm(aspect, this);
    }
  }
}