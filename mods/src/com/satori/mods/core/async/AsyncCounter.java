package com.satori.mods.core.async;

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
