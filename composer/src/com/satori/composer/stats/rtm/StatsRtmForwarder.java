package com.satori.composer.stats.rtm;

import com.satori.composer.rtm.*;
import com.satori.composer.stats.*;
import com.satori.libs.async.core.*;

import io.vertx.core.*;
import org.slf4j.*;


public class StatsRtmForwarder extends StatsJsonAggregator implements IStatsForwarder {
  public static final Logger log = LoggerFactory.getLogger(StatsRtmForwarder.class);
  public static final long MSGS_IN_FLY_THRESHOLD = 10;
  
  public final StatsRtmForwarderConfig config;
  public final String channel;
  public RtmChannel rtm = null;
  public final long period;
  private long nextTs = Long.MAX_VALUE;
  private boolean inProgress = false;
  private int msgsInFly = 0;
  
  
  public StatsRtmForwarder(StatsRtmForwarderConfig config) {
    super(config.prefix, config.ext);
    period = config.period;
    channel = config.channel;
    this.config = config;
  }
  
  
  // IFrameworkModule implementation
  
  
  @Override
  public void onStart(Vertx vertx) {
    msgsInFly = 0;
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
    if(msgsInFly > MSGS_IN_FLY_THRESHOLD){
      log.warn("too many messages ({}) without reply", msgsInFly);
    }
    msgsInFly += 1;
    inProgress = true;
    try {
      StatsJsonMessage msg = drainAsJsonMessage();
      msg.tags = config.tags;
      rtm.publish(channel, msg, ar -> {
        if(!inProgress) {
          log.error("unexpected state");
        }
        msgsInFly -= 1;
        inProgress = false;
        if (!ar.isSucceeded()) {
          log.warn("statistics skipped", ar.getError());
        }
      });
    } catch (Exception cause) {
      if(!inProgress) {
        log.error("unexpected state");
      }
      msgsInFly -= 1;
      inProgress = false;
      log.warn("statistics skipped", cause);
    }
    // sanity check
    if(msgsInFly < 0){
      log.error("msgsInFly is negative ({})", msgsInFly);
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
