package com.satori.composer.stats.console;

import com.satori.mods.core.stats.*;

import java.util.*;

public class StatsConsoleWriter implements IStatsCollector {
  public final String prefix;
  
  public StatsConsoleWriter() {
    this(null);
  }
  
  public StatsConsoleWriter(String prefix) {
    this.prefix = prefix == null || prefix.isEmpty() ? null : prefix + ".";
  }
  
  // IStatsCollector implementation
  
  @Override
  public void sum(String aspect, double sum) {
    printMetric(aspect, sum, "counter");
  }
  
  @Override
  public void avg(String aspect, double sum, long n) {
    if (n > 0) {
      printMetric(aspect, sum / n, "gauge");
    }
  }
  
  @Override
  public void norm(String aspect, double sum, long n, double sumSq) {
    if (n > 0) {
      printMetric(aspect, sum / n, "gauge");
    }
  }
  
  @Override
  public void series(String aspect, double[] values, int offset, int size) {
    if (offset != 0 || size != values.length) {
      values = Arrays.copyOfRange(values, offset, offset + size);
    }
    if (prefix != null) {
      System.out.printf("%s%s: %s (series)\n", prefix, aspect, Arrays.toString(values));
    } else {
      System.out.printf("%s: %s (series)\n", aspect, Arrays.toString(values));
    }
  }
  
  // private methods
  
  private void printMetric(String aspect, double val, String type) {
    if (prefix != null) {
      System.out.printf("%s%s: %s (%s)\n", prefix, aspect, val, type);
    } else {
      System.out.printf("%s: %s (%s)\n", aspect, val, type);
    }
  }
  
}
