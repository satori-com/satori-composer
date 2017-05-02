package com.satori.mods.core.stats;

public class StatsAvg {
  public double sum;
  public long n;
  
  public StatsAvg() {
  }
  
  public StatsAvg(double sum, long n) {
    this.sum = sum;
    this.n = n;
  }
  
  public StatsAvg(StatsAvg other) {
    this(other.sum, other.n);
  }
  
  public void aggregate(double sum, long n) {
    this.sum += sum;
    this.n += n;
  }
  
  public void aggregate(double value) {
    aggregate(value, 1);
  }
  
  public void copyFrom(StatsAvg other) {
    sum = other.sum;
    n = other.n;
  }
  
  public void suppress() {
    sum = 0;
    n = 0;
  }
}