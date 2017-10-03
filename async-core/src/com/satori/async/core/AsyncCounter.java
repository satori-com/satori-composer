package com.satori.async.core;

public class AsyncCounter {
  
  private int val;
  
  public AsyncCounter(int val) {
    this.val = val;
  }
  
  public int inc(){
    return ++val;
  }
  
  public int dec(){
    return --val;
  }
}
