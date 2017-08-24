package com.satori.mods.suite;

import com.satori.mods.core.stats.*;

public class XsltModStats implements IStatsProvider {
  private final static XsltModStats zero = new XsltModStats();
  
  public int received = 0;
  public int sent = 0;
  
  public XsltModStats() {
  }
  
  public XsltModStats(XsltModStats other) {
    copyFrom(other);
  }
  
  // IStatsProvider implementation
  
  @Override
  public void collect(IStatsCollector collector) {
    collector.sum("recv", received);
    collector.sum("sent", sent);
  }
  
  @Override
  public void suppress() {
    reset();
  }
  
  // public methods
  
  public void reset() {
    copyFrom(zero);
  }
  
  // private methods
  
  private void copyFrom(XsltModStats other) {
    received = other.received;
    sent = other.sent;
  }
}
