package com.satori.composer.runtime;

import com.satori.composer.stats.*;
import com.satori.composer.stats.console.*;
import com.satori.composer.stats.ddog.*;
import com.satori.composer.stats.http.*;
import com.satori.composer.stats.rtm.*;
import com.satori.composer.stats.statsd.*;
import com.satori.mods.api.*;
import com.satori.mods.core.stats.*;

import java.io.Closeable;

import io.vertx.core.*;
import org.slf4j.*;

public class ModVerticle extends AbstractVerticle implements IModContext, IModRuntime {
  public static final int INVALID_TIMER = -1;
  public static final int PULSE_INTERVAL = 1000;
  public static final Logger log = LoggerFactory.getLogger(ModVerticle.class);
  
  private final IMod mod;
  private final ComposerRuntimeConfig config;
  
  //private final ArrayList<LowLatencyChannelDriver> channelDrivers;
  private long pulseTimer = INVALID_TIMER;
  private IStatsForwarder statsForwarder = null;
  private long statsNextTs;
  private Pulse statsLastPulse;
  private long statsPeriod;
  
  public ModVerticle(ComposerRuntimeConfig config, IMod mod) throws Exception {
    if (mod == null) {
      throw new IllegalArgumentException("mod argument cannot be null");
    }
    this.mod = mod;
    this.config = config;
  }
  
  // Verticle implementation
  @Override
  public void start() throws Exception {
    log.info("starting mod verticle... {}", this);
    super.start();
    
    if (pulseTimer != INVALID_TIMER) {
      vertx.cancelTimer(pulseTimer);
      pulseTimer = INVALID_TIMER;
    }
    pulseTimer = vertx.setPeriodic(
      PULSE_INTERVAL, this::pulse
    );
    
    statsForwarder = createStatsForwarder(config.stats);
    if (statsForwarder != null) {
      statsForwarder.onStart(getVertx());
    }
    
    statsPeriod = config.stats.period;
    statsLastPulse = new Pulse();
    statsNextTs = statsLastPulse.ticks + statsPeriod;
    
    mod.init(this);
    mod.onStart();
  }
  
  @Override
  public void stop() throws Exception {
    log.info("stopping mod verticle... {}", this);
    
    mod.onStop();
    mod.dispose();
    
    if (pulseTimer != INVALID_TIMER) {
      vertx.cancelTimer(pulseTimer);
      pulseTimer = INVALID_TIMER;
    }
    
    if (statsForwarder != null) {
      statsForwarder.onStop(getVertx());
      statsForwarder.dispose();
      statsForwarder = null;
    }
    
  }
  
  public void onPulse(Pulse pulse) throws Exception {
    mod.onPulse();
  }
  
  public void onStats(StatsCycle cycle) throws Exception {
    mod.onStats(cycle, statsForwarder);
  }
  
  private void pulse(long timer) {
    if (timer != pulseTimer) {
      log.warn("pulse timer has been recreated or stopped");
      return;
    }
    Pulse pulse = new Pulse();
    log.debug("pulse {}", pulse.now);
    
    if (pulse.ticks >= statsNextTs) {
      try {
        onStats(new StatsCycle(
          statsPeriod,
          pulse.ticks, pulse.now,
          statsLastPulse.ticks, statsLastPulse.now
        ));
      } catch (Exception exn) {
        log.error("unhandled exception", exn);
      } finally {
        statsLastPulse = pulse;
        statsNextTs = statsLastPulse.ticks + statsPeriod;
      }
    }
    
    if (statsForwarder != null) {
      try {
        statsForwarder.onPulse(pulse.ticks);
      } catch (Exception exn) {
        log.error("unhandled exception", exn);
      }
    }
    
    try {
      onPulse(pulse);
    } catch (Exception exn) {
      log.error("unhandled exception", exn);
    }
    
  }
  
  public void undeploy() {
    vertx.undeploy(deploymentID());
  }
  
  // IModContext
  
  
  @Override
  public IModRuntime runtime() {
    return this;
  }
  
  @Override
  public void exec(Runnable action) throws Exception {
    vertx.runOnContext(v -> action.run());
  }
  
  @Override
  public Closeable timer(long delay, Runnable action) {
    return new ModTimer(delay, action, vertx);
  }
  
  public static IStatsForwarder createStatsForwarder(StatsModuleConfig config) {
    if (config == null) {
      return null;
    }
    
    StatsCompositeForwarder res = new StatsCompositeForwarder();
    
    if (config.console != null) {
      for (StatsConsoleFrowarderConfig c : config.console) {
        res.add(new StatsConsoleForwarder(c));
      }
    }
    if (config.rtm != null) {
      for (StatsRtmForwarderConfig c : config.rtm) {
        res.add(new StatsRtmForwarder(c));
      }
    }
    if (config.statsd != null) {
      for (StatsdForwarderConfig c : config.statsd) {
        res.add(new StatsdForwarder(c));
      }
    }
    if (config.http != null) {
      for (StatsHttpForwarderConfig c : config.http) {
        res.add(new StatsHttpForwarder(c));
      }
    }
    if (config.ddog != null) {
      for (StatsDdogForwarderConfig c : config.ddog) {
        res.add(new StatsDdogForwarder(c));
      }
    }
    return res;
  }
}
