package com.satori.mods.core.stats;

public class StatsNorm {
  public double sum;
  public long n;
  public double sumSq;
  
  public StatsNorm() {
  }
  
  public StatsNorm(double sum, long n, double sumSq) {
    this.sum = sum;
    this.n = n;
    this.sumSq = sumSq;
  }
  
  public void aggregate(double sum, long n, double sumSq) {
    this.sum += sum;
    this.n += n;
    this.sumSq += sumSq;
  }
  
  public void suppress() {
    sum = 0;
    n = 0;
    sumSq = 0;
  }
}