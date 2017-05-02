package com.satori.mods.core.stats;


public final class StatsCounter implements IStatsProvider {
  public final String name;
  public long cnt = 0;
  
  public StatsCounter(String name) {
    this.name = name;
  }
  
  public StatsCounter(String prefix, String name) {
    if (prefix == null || prefix.isEmpty()) {
      this.name = name;
    } else if (name == null || name.isEmpty()) {
      this.name = prefix;
    } else {
      this.name = prefix + "." + name;
    }
  }
  
  public StatsCounter(StatsCounter other) {
    name = other.name;
    cnt = other.cnt;
  }
  
  // IStatsProvider implementation
  
  @Override
  public void collect(IStatsCollector collector) {
    collector.sum(name, cnt);
  }
  
  @Override
  public void suppress() {
    cnt = 0;
  }
  
  // public methods
  
  public void inc() {
    cnt += 1;
  }
  
  public void inc(long val) {
    cnt += val;
  }
  
  public void inc(StatsCounter counter) {
    cnt += counter.cnt;
  }
  
}
