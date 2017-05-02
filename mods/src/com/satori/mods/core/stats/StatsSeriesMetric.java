package com.satori.mods.core.stats;


public class StatsSeriesMetric extends StatsSeries implements IStatsRecord {
  public final String aspect;
  
  public StatsSeriesMetric(String aspect, double[] values, int offset, int size) {
    super(values, offset, size);
    this.aspect = aspect;
  }
  
  // IStatsRecord implementation
  
  @Override
  public void collect(IStatsCollector collector) {
    if (collector != null) {
      collector.series(aspect, this);
    }
  }
}