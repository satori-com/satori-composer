package com.satori.composer.stats.rtm;

import com.satori.composer.rtm.*;
import com.satori.composer.runtime.*;
import com.satori.composer.stats.*;

import io.vertx.core.*;
import org.slf4j.*;


public class StatsRtmForwarder extends StatsJsonAggregator implements IStatsForwarder {
  public static final Logger log = LoggerFactory.getLogger(StatsRtmForwarder.class);
  
  public final StatsRtmForwarderConfig config;
  public final String channel;
  public RtmChannel rtm = null;
  public final long period;
  private long nextTs = Long.MAX_VALUE;
  private boolean inProgress = false;
  
  
  public StatsRtmForwarder(StatsRtmForwarderConfig config) {
    super(config.prefix, config.ext);
    period = config.period;
    channel = config.channel;
    this.config = config;
  }
  
  
  // IFrameworkModule implementation
  
  
  @Override
  public void onStart(Vertx vertx) {
    rtm = new RtmChannel(vertx, config, channel);
    rtm.start();
    nextTs = Stopwatch.timestamp() + period;
  }
  
  @Override
  public void onStop(Vertx vertx) {
    if (rtm != null) {
      rtm.stop();
      rtm = null;
    }
    nextTs = Long.MAX_VALUE;
  }
  
  @Override
  public void onPulse(long ts) {
    rtm.onPulse(ts);
    if (inProgress || ts < nextTs) {
      return;
    }
    nextTs = ts + period;
    if (!isDirty()) {
      return;
    }
    inProgress = true;
    try {
      StatsJsonMessage msg = drainAsJsonMessage();
      msg.tags = config.tags;
      rtm.publish(channel, msg, ar -> {
        inProgress = false;
        if (ar.isFailed()) {
          log.warn("statistics skipped", ar.getError());
        }
      });
    } catch (Exception cause) {
      inProgress = false;
      log.warn("statistics skipped", cause);
    }
  }
  
  // IStatsForwarder implementation
  
  @Override
  public void dispose() {
    
  }
  
  @Override
  public boolean disposed() {
    return false;
  }
}
