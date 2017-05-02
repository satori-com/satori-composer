package com.satori.composer.stats.ddog;

public class DdogMetricPoint {
  public long timestamp;
  public float value;

  public DdogMetricPoint(float value) {
    this(value, System.currentTimeMillis() / 1000);
  }

  public DdogMetricPoint(float value, long timestamp) {
    this.timestamp = timestamp;
    this.value = value;
  }
}
