package com.satori.composer.runtime;

import java.util.concurrent.*;

public class Stopwatch {
  public static long timestamp() {
    return TimeUnit.NANOSECONDS.toMillis(System.nanoTime());
  }
}
