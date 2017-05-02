package com.satori.mods.core.stats;

public interface IStatsProvider extends IStatsRecord {
  void suppress();
  
  default void drain(IStatsCollector collector) {
    collect(collector);
    suppress();
  }
}
