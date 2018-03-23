package com.satori.mods.suite;

import com.satori.composer.rtm.*;
import com.satori.composer.rtm.core.*;
import com.satori.composer.runtime.*;
import com.satori.libs.async.api.*;
import com.satori.mods.api.*;
import com.satori.mods.core.stats.*;

import com.fasterxml.jackson.databind.*;
import org.slf4j.*;

public abstract class RtmPublishBaseMod extends Mod {
  public abstract Logger log();
  
  public abstract RtmBaseConfig config();
  
  public abstract String label();
  
  private final RtmPublishModStats stats = new RtmPublishModStats();
  private RtmChannel rtm;
  
  
  // IMod implementation
  
  @Override
  public void init(final IModContext context) throws Exception {
    super.init(context);
    rtm = new RtmChannel(vertx(), config(), label());
    rtm.start();
    stats.reset();
    log().info("initialized");
  }
  
  @Override
  public void dispose() throws Exception {
    super.dispose();
    if (rtm != null) {
      try {
        rtm.stop();
      } catch (Exception e) {
        // swallow exception
        log().error("failed to stop rtm connection", e);
      }
      rtm = null;
    }
    stats.reset();
    log().info("disposed");
  }
  
  @Override
  public void onStats(StatsCycle cycle, IStatsCollector collector) {
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
    processInput(inputName, data, cont);
  }
  
  public void processInput(String inputName, JsonNode msg, IAsyncHandler cont) throws Exception {
    publish(inputName, msg, cont);
  }
  
  public void publish(String channel, JsonNode msg, IAsyncHandler cont) throws Exception {
    if (!rtm.isWritable()) {
      log().info("paused...");
      rtm.onWritable(ar -> {
        log().info("resumed...");
        if (!ar.isSucceeded()) {
          stats.sentErr += 1;
          log().error("publish to channel '{}' failed", channel, ar.getError());
          cont.fail(ar.getError());
          return;
        }
        try {
          publish(channel, msg, cont);
        } catch (Exception e) {
          cont.fail(e);
        }
      });
      return;
    }
    stats.sent += 1;
    try {
      rtm.publish(channel, msg);
    } catch (Throwable e) {
      stats.sentErr += 1;
      log().error("publish to channel '{}' failed", channel, e);
      throw e;
    }
    stats.sentOk += 1;
    cont.succeed();
  }
}
