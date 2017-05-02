package com.satori.mods.core.stats;

public class StatsSum {
  public double sum;
  
  public StatsSum(double sum) {
    this.sum = sum;
  }
  
  public StatsSum(StatsSum other) {
    this(other.sum);
  }
  
  public void aggregate(double sum) {
    this.sum += sum;
  }
}