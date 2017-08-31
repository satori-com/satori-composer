package com.satori.mods.suite;

import com.satori.mods.core.stats.*;

public class BarrierModStats implements IStatsProvider {
  
  public int recv = 0;
  public int sent = 0;
  public int paused = 0;
  public int resumed = 0;
  public final StatsAvg infly = new StatsAvg();
  
  public BarrierModStats() {
  }
  
  // IStatsProvider implementation
  
  @Override
  public void collect(IStatsCollector collector) {
    collector.sum("recv", recv);
    collector.sum("sent", sent);
    collector.sum("paused", paused);
    collector.sum("resumed", resumed);
    collector.avg("infly", infly);
  }
  
  @Override
  public void suppress() {
    recv = 0;
    sent = 0;
    paused = 0;
    resumed = 0;
    infly.suppress();
  }
}
