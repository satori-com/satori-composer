package com.satori.mods.suite;

import com.satori.mods.core.stats.*;

public class GtfsProtoBufToJsonModStats implements IStatsProvider {
  private final static GtfsProtoBufToJsonModStats zero = new GtfsProtoBufToJsonModStats();
  
  public int recv = 0;
  public int sent = 0;
  
  public GtfsProtoBufToJsonModStats() {
  }
  
  public GtfsProtoBufToJsonModStats(GtfsProtoBufToJsonModStats other) {
    copyFrom(other);
  }
  
  // IStatsProvider implementation
  
  @Override
  public void collect(IStatsCollector collector) {
    collector.sum("recv", recv);
    collector.sum("sent", sent);
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
  
  private void copyFrom(GtfsProtoBufToJsonModStats other) {
    recv = other.recv;
    sent = other.sent;
  }
}
