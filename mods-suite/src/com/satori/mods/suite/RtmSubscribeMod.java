package com.satori.mods.suite;

import com.satori.composer.rtm.*;
import com.satori.composer.runtime.*;
import com.satori.libs.async.api.*;
import com.satori.libs.async.core.*;
import com.satori.mods.api.*;
import com.satori.mods.core.config.*;
import com.satori.mods.core.stats.*;

import com.fasterxml.jackson.databind.*;
import org.slf4j.*;

// low latency mode only is supported
// Duplicates are not possible
// Messages can be lost
// Minimum extra latencies
// Minimum overhead

public class RtmSubscribeMod extends Mod {
  public static final Logger log = LoggerFactory.getLogger(RtmSubscribeMod.class);
  
  private final RtmSubscribeModStats stats = new RtmSubscribeModStats();
  private final RtmSubscribeModSettings config;
  
  private final int pauseThreshold;
  private final int resumeThreshold;
  private int unconsumedMessages = 0;
  
  private RtmChannelSubscriber rtm;
  
  public RtmSubscribeMod(JsonNode userData) throws Exception {
    this(Config.parseAndValidate(userData, RtmSubscribeModSettings.class));
  }
  
  public RtmSubscribeMod(RtmSubscribeModSettings config) throws Exception {
    this.config = config;
    pauseThreshold = config.windowMaxSize;
    resumeThreshold = config.windowMaxSize / 2;
    log.info("created");
  }
  
  // IMod implementation
  
  @Override
  public void init(final IModContext context) throws Exception {
    super.init(context);
    rtm = new RtmChannelSubscriber(vertx(), config, config.channel) {
      @Override
      public void onChannelData(JsonNode msg) {
        RtmSubscribeMod.this.onChannelData(msg);
      }
    };
    rtm.start();
    stats.reset();
    log.info("initialized");
  }
  
  @Override
  public void dispose() throws Exception {
    super.dispose();
    if (rtm != null) {
      try {
        rtm.stop();
      } catch (Exception e) {
        // swallow exception
        log.error("failed to stop rtm connection", e);
      }
      rtm = null;
    }
    stats.reset();
    log.info("terminated");
  }
  
  @Override
  public void onStats(StatsCycle cycle, IStatsCollector collector) {
    log.debug("collecting statistic...");
    stats.drain(collector);
  }
  
  @Override
  public void onPulse() {
    if (rtm != null) {
      rtm.onPulse(Stopwatch.timestamp());
    }
    stats.infly.aggregate(unconsumedMessages);
  }
  
  // private methods
  
  private void onChannelData(JsonNode msg) {
    stats.recv += 1;
    unconsumedMessages += 1;
    if (unconsumedMessages > pauseThreshold) {
      rtm.unsubscribe();
    }
    stats.sent += 1;
    IAsyncHandler cont = AsyncPromise.from(this::onMessageConsumed);
    try {
      yield(msg, cont);
    } catch (Exception e) {
      cont.fail(e);
    }
  }
  
  private void onMessageConsumed(IAsyncResult ar) {
    unconsumedMessages -= 1;
    if (!ar.isSucceeded()) {
      log.warn("processing message error", ar.getError());
      stats.failed += 1;
    } else {
      stats.succeeded += 1;
    }
    if (unconsumedMessages < resumeThreshold) {
      if (rtm != null) {
        rtm.subscribe();
      }
    }
  }
}
