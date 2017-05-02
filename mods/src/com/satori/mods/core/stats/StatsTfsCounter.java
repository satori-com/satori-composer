package com.satori.mods.core.stats;

// Total-Failed-Succeeded counter metric
public final class StatsTfsCounter implements IStatsProvider {
  public final StatsCounter total;
  public final StatsCounter failed;
  public final StatsCounter succeeded;
  
  public StatsTfsCounter(String name) {
    this.total = new StatsCounter(name, "total");
    this.failed = new StatsCounter(name, "failed");
    this.succeeded = new StatsCounter(name, "succeeded");
  }
  
  public StatsTfsCounter(StatsTfsCounter other) {
    this.total = new StatsCounter(other.total);
    this.failed = new StatsCounter(other.failed);
    this.succeeded = new StatsCounter(other.succeeded);
  }
  
  // IStatsProvider implementation
  
  @Override
  public void collect(IStatsCollector collector) {
    total.collect(collector);
    failed.collect(collector);
    succeeded.collect(collector);
  }
  
  @Override
  public void suppress() {
    total.suppress();
    failed.suppress();
    succeeded.suppress();
  }
  
  public void inc(StatsTfsCounter counter) {
    incTotal(counter.total.cnt);
    incFailed(counter.failed.cnt);
    incSucceeded(counter.succeeded.cnt);
  }
  
  // public methods
  
  public void incTotal() {
    total.inc();
  }
  
  void incTotal(long val) {
    total.inc(val);
  }
  
  public void incFailed() {
    failed.inc();
  }
  
  void incFailed(long val) {
    failed.inc(val);
  }
  
  public void incSucceeded() {
    succeeded.inc();
  }
  
  void incSucceeded(long val) {
    succeeded.inc(val);
  }
  
}

