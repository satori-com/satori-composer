package com.satori.mods.core.stats;

import java.util.*;

public class StatsSeries {
  public double[] values;
  public int size;
  
  public StatsSeries(double[] values, int offset, int size) {
    int newLength = 8;
    do {
      newLength = newLength << 1;
      if (newLength < 0) {
        newLength = size;
        break;
      }
    } while (newLength < size);
    this.values = new double[newLength];
    System.arraycopy(values, offset, this.values, 0, size);
    this.size = size;
  }
  
  public void aggregate(double[] values, int offset, int size) {
    if (values == null) {
      return;
    }
    int reqLength = this.size + size;
    if (reqLength < 0) {
      throw new OutOfMemoryError();
    }
    if (this.values.length < reqLength) {
      int newLength = this.values.length;
      do {
        newLength = newLength << 1;
        if (newLength < 0) {
          newLength = reqLength;
          break;
        }
      } while (newLength < reqLength);
      this.values = Arrays.copyOf(this.values, newLength);
    }
    System.arraycopy(values, offset, this.values, this.size, size);
    this.size = reqLength;
  }
  
  public void suppress() {
    size = 0;
  }
  
}