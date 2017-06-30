package com.satori.mods.suite;

import com.satori.mods.core.stats.*;

public class RtmSubscribeModStats implements IStatsProvider {
  
  public int recv = 0;
  public int sent = 0;
  public int succeeded = 0;
  public int failed = 0;
  public StatsAvg infly = new StatsAvg();
  
  public RtmSubscribeModStats() {
  }
  
  public RtmSubscribeModStats(RtmSubscribeModStats other) {
    recv = other.recv;
    succeeded = other.succeeded;
    failed = other.failed;
  }
  
  // IStatsProvider implementation
  
  @Override
  public void collect(IStatsCollector collector) {
    collector.sum("recv", recv);
    collector.sum("sent", sent);
    collector.sum("sent.err", failed);
    collector.sum("sent.ok", succeeded);
    collector.avg("infly", infly);
  }
  
  @Override
  public void suppress() {
    reset();
  }
  
  // public methods
  
  public void reset() {
    sent = 0;
    recv = 0;
    succeeded = 0;
    failed = 0;
    infly.suppress();
  }
}
