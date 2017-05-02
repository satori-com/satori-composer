package com.satori.mods.core.stats;

public class StatsCycle {
  public final long period;
  public final long ticks;
  public final long epoch;
  public final long lastTicks;
  public final long lastEpoch;
  
  public StatsCycle(long period, long ticks, long epoch, long lastTicks, long lastEpoch) {
    this.period = period;
    this.ticks = ticks;
    this.epoch = epoch;
    this.lastTicks = lastTicks;
    this.lastEpoch = lastEpoch;
  }
}
