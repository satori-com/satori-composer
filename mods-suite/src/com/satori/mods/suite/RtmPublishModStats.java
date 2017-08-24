package com.satori.mods.suite;

import com.satori.mods.core.stats.*;

public class RtmPublishModStats implements IStatsProvider {
  private final static RtmPublishModStats zero = new RtmPublishModStats();
  
  public int recv = 0;
  public int sent = 0;
  public int sentOk = 0;
  public int sentErr = 0;
  public StatsAvg winSize = new StatsAvg();
  public int sentNoAck = 0;
  
  public RtmPublishModStats() {
  }
  
  public RtmPublishModStats(RtmPublishModStats other) {
    copyFrom(other);
  }
  
  @Override
  public void collect(IStatsCollector collector) {
    
    int msgNoAckNext = sentNoAck + sent;
    msgNoAckNext -= sentOk;
    msgNoAckNext -= sentErr;
    
    collector.sum("recv", recv);
    collector.sum("sent", sent);
    collector.sum("sent.ok", sentOk);
    collector.sum("sent.err", sentErr);
    collector.avg("sent.noack", msgNoAckNext);
    if (winSize.n > 0) {
      collector.avg("win.size", winSize);
    }
  }
  
  @Override
  public void suppress() {
    sentNoAck += sent;
    sentNoAck -= sentOk;
    sentNoAck -= sentErr;
    
    recv = 0;
    sent = 0;
    sentOk = 0;
    sentErr = 0;
    winSize.suppress();
  }
  
  // public methods
  
  public void reset() {
    copyFrom(zero);
  }
  
  // private methods
  
  private void copyFrom(RtmPublishModStats other) {
    recv = other.recv;
    sent = other.sent;
    sentOk = other.sentOk;
    sentErr = other.sentErr;
    winSize = new StatsAvg(other.winSize);
    sentNoAck = other.sentNoAck;
  }
}
