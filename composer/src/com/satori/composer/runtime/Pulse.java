package com.satori.composer.runtime;

import java.util.concurrent.*;

public class Pulse {
  public final long ticks;
  public final long now;
  
  public Pulse(long ticks, long now) {
    this.ticks = ticks;
    this.now = now;
  }
  
  public Pulse() {
    this(ticks(), now());
  }
  
  public static long ticks() {
    return TimeUnit.NANOSECONDS.toMillis(System.nanoTime());
  }
  
  public static long now() {
    return System.currentTimeMillis();
  }
}
