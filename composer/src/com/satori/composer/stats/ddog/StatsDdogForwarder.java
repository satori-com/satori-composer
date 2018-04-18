package com.satori.composer.stats.ddog;

import com.satori.composer.ddog.*;
import com.satori.composer.stats.*;
import com.satori.libs.async.core.*;

import io.vertx.core.*;
import org.slf4j.*;


public class StatsDdogForwarder extends StatsDdogAggregator implements IStatsForwarder {
  public static final Logger log = LoggerFactory.getLogger(StatsDdogForwarder.class);
  
  public final StatsDdogForwarderConfig config;
  public DdogClient ddog = null;
  public final long period;
  private long lastTs = Stopwatch.timestamp();
  private long nextTs = Long.MAX_VALUE;
  private int noack = 0;
  
  
  public StatsDdogForwarder(StatsDdogForwarderConfig config) {
    super(config.prefix, config.hostname, config.tags);
    period = config.period;
    this.config = config;
  }
  
  
  // IComposerRuntimeModule implementation
  
  
  @Override
  public void onStart(Vertx vertx) {
    ddog = new DdogClient(vertx, config);
    lastTs = Stopwatch.timestamp();
    nextTs = lastTs + period;
    reset();
  }
  
  @Override
  public void onStop(Vertx vertx) {
    if (ddog != null) {
      ddog.close();
      ddog = null;
    }
    nextTs = Long.MAX_VALUE;
    reset();
  }
  
  @Override
  public void onPulse(long ts) {
    if (noack > 0 || ts < nextTs) {
      return;
    }
    long elapsed = ts - lastTs;
    lastTs = ts;
    nextTs = ts + period;
    if (!isDirty()) {
      return;
    }
    noack += 1;
    try {
      DdogSeries[] series = drainAsDdogSeries(elapsed);
      ddog.postSeries(series, ar -> {
        noack -= 1;
        if (!ar.isSucceeded()) {
          log.warn("statistics skipped", ar.getError());
        }
      });
    } catch (Exception cause) {
      noack -= 1;
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
