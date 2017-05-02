package com.satori.mods.suite;

import com.satori.mods.core.stats.*;

public class HttpPostModStats implements IStatsProvider {
  
  public int recv = 0;
  public int reqFailed = 0;
  public int reqSucceeded = 0;
  
  public HttpPostModStats() {
  }
  
  public HttpPostModStats(HttpPostModStats other) {
    recv = other.recv;
    reqSucceeded = other.reqSucceeded;
    reqFailed = other.reqFailed;
  }
  
  // IStatsProvider implementation
  
  @Override
  public void collect(IStatsCollector collector) {
    collector.sum("recv", recv);
    collector.sum("req.ok", reqSucceeded);
    collector.sum("req.err", reqFailed);
  }
  
  @Override
  public void suppress() {
    reset();
  }
  
  // public methods
  
  public void reset() {
    recv = 0;
    reqSucceeded = 0;
    reqFailed = 0;
  }
}
