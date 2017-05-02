package com.satori.mods.suite;

import com.satori.mods.core.stats.*;

public class HttpPollModStats implements IStatsProvider {
  
  public int pollInit = 0;
  public int sent = 0;
  public int pollIdle = 0;
  public int pollFail = 0;
  
  public HttpPollModStats() {
  }
  
  public HttpPollModStats(HttpPollModStats other) {
    sent = other.sent;
    pollInit = other.pollInit;
    pollIdle = other.pollIdle;
    pollFail = other.pollFail;
  }
  
  // IStatsProvider implementation
  
  @Override
  public void collect(IStatsCollector collector) {
    collector.sum("sent", sent);
    collector.sum("poll.init", pollInit);
    collector.sum("poll.idle", pollIdle);
    collector.sum("poll.fail", pollFail);
  }
  
  @Override
  public void suppress() {
    reset();
  }
  
  // public methods
  
  public void reset() {
    sent = 0;
    pollInit = 0;
    pollIdle = 0;
    pollFail = 0;
  }
}
