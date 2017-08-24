package com.satori.mods.suite;

import com.satori.composer.rtm.*;
import com.satori.composer.runtime.*;
import com.satori.mods.api.*;
import com.satori.mods.core.async.*;
import com.satori.mods.core.config.*;
import com.satori.mods.core.stats.*;

import com.fasterxml.jackson.databind.*;
import io.vertx.core.*;
import org.slf4j.*;

public class RtmPublishMod extends Mod {
  public static final Logger log = LoggerFactory.getLogger(RtmPublishMod.class);
  
  private final RtmPublishModStats stats = new RtmPublishModStats();
  private final RtmPublishModSettings config;
  
  private RtmChannel rtm;
  
  public RtmPublishMod(JsonNode userData) throws Exception {
    this(Config.parseAndValidate(userData, RtmPublishModSettings.class));
  }
  
  public RtmPublishMod(RtmPublishModSettings config) throws Exception {
    this.config = config;
    log.info("created");
  }
  
  // IMod implementation
  
  @Override
  public void init(final IModContext context) throws Exception {
    super.init(context);
    rtm = new RtmChannel(vertx(), config, config.channel);
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
  }
  
  @Override
  public void onInput(String inputName, JsonNode data, IAsyncHandler cont) throws Exception {
    stats.recv += 1;
    publish(data, cont);
  }
  
  public void publish(JsonNode msg, IAsyncHandler cont) throws Exception {
    if (!rtm.isWritable()) {
      log.info("paused...");
      rtm.onWritable(ar -> {
        log.info("resumed...");
        if (ar.isFailed()) {
          stats.sentErr += 1;
          log.error("publish failed", ar.getError());
          cont.fail(ar.getError());
          return;
        }
        try {
          publish(msg, cont);
        } catch (Exception e) {
          cont.fail(e);
        }
        
      });
      return;
    }
    stats.sent += 1;
    try {
      rtm.publish(config.channel, msg);
    } catch (Throwable e) {
      stats.sentErr += 1;
      log.error("publish failed", e);
      throw e;
    }
    stats.sentOk += 1;
    cont.succeed();
  }
}
