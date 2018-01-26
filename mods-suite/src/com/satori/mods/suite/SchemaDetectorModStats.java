package com.satori.mods.suite;

import com.satori.mods.core.stats.*;

public class SchemaDetectorModStats implements IStatsProvider {
  
  public int recv = 0;
  public int sent = 0;
  
  public SchemaDetectorModStats() {
  }
  
  public SchemaDetectorModStats(SchemaDetectorModStats other) {
    recv = other.recv;
    sent = other.sent;
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
    recv = 0;
    sent = 0;
  }
}
