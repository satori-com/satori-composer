package com.satori.mods.suite;

import com.satori.mods.core.stats.*;

public class DedupModStats implements IStatsProvider {
  private final static DedupModStats zero = new DedupModStats();
  
  public int recv = 0;
  public int sent = 0;
  public int expired = 0;
  public int filtered = 0;
  public StatsAvg cached = new StatsAvg();
  
  public DedupModStats() {
  }
  
  public DedupModStats(DedupModStats other) {
    copyFrom(other);
  }
  
  // IStatsProvider implementation
  
  @Override
  public void collect(IStatsCollector collector) {
    collector.sum("recv", recv);
    collector.sum("sent", sent);
    collector.sum("expired", expired);
    collector.sum("filtered", filtered);
    collector.avg("cached", cached);
  }
  
  @Override
  public void suppress() {
    reset();
  }
  
  // public methods
  
  public void reset() {
    copyFrom(zero);
  }
  
  // private methods
  
  private void copyFrom(DedupModStats other) {
    recv = other.recv;
    sent = other.sent;
    expired = other.expired;
    filtered = other.filtered;
    cached.copyFrom(other.cached);
  }
}
