package com.satori.mods.core.stats;


public class StatsSumMetric extends StatsSum implements IStatsRecord {
  public final String aspect;
  
  public StatsSumMetric(String aspect) {
    super(0);
    this.aspect = aspect;
  }
  
  public StatsSumMetric(String aspect, double sum) {
    super(sum);
    this.aspect = aspect;
  }
  
  // IStatsRecord implementation
  
  @Override
  public void collect(IStatsCollector collector) {
    if (collector != null) {
      collector.sum(aspect, this);
    }
  }
}