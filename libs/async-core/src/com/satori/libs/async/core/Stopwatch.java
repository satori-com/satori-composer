package com.satori.libs.async.core;

import java.util.concurrent.*;

// TODO: move to some common library
public class Stopwatch {
  public static long timestamp() {
    return TimeUnit.NANOSECONDS.toMillis(System.nanoTime());
  }
}
