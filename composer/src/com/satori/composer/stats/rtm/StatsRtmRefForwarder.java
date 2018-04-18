package com.satori.composer.stats.rtm;

import com.satori.composer.rtm.*;
import com.satori.composer.stats.*;
import com.satori.libs.async.core.*;

import io.vertx.core.*;
import org.slf4j.*;


public class StatsRtmRefForwarder extends StatsJsonAggregator implements IStatsForwarder {
  public static final Logger log = LoggerFactory.getLogger(StatsRtmRefForwarder.class);
  
  public final String channel;
  public final RtmChannel rtm;
  public final long period;
  private long nextTs = Long.MAX_VALUE;
  private boolean inProgress = false;
  
  
  public StatsRtmRefForwarder(StatsRtmRefForwarderConfig config, RtmChannel rtm) {
    super(config.prefix, config.ext);
    period = config.period;
    channel = config.channel;
    this.rtm = rtm;
  }
  
  
  // IComposerRuntimeModule implementation
  
  @Override
  public void onStart(Vertx vertx) {
    rtm.start();
    nextTs = Stopwatch.timestamp() + period;
  }
  
  @Override
  public void onStop(Vertx vertx) {
    rtm.stop();
    nextTs = Long.MAX_VALUE;
  }
  
  @Override
  public void onPulse(long ts) {
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
      rtm.publish(channel, msg, ar -> {
        inProgress = false;
        if (!ar.isSucceeded()) {
          log.warn("failed to publish statistics", ar.getError());
        }
      });
    } catch (Exception cause) {
      inProgress = false;
      log.warn("failed to publish statistics", cause);
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
