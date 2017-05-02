package com.satori.composer.stats.console;

import com.satori.composer.runtime.*;
import com.satori.composer.stats.*;
import com.satori.mods.core.stats.*;

import io.vertx.core.*;
import org.slf4j.*;

public class StatsConsoleForwarder extends StatsAggregator implements IStatsForwarder {
  public static final Logger log = LoggerFactory.getLogger(StatsConsoleForwarder.class);
  
  public final StatsConsoleWriter writer;
  public final long period;
  private long nextTs = Long.MAX_VALUE;
  private Vertx vertx = null;
  
  public StatsConsoleForwarder() {
    this(null, StatsConsoleFrowarderConfig.defaultPeriod);
  }
  
  public StatsConsoleForwarder(long period) {
    this(null, period);
  }
  
  public StatsConsoleForwarder(String prefix) {
    this(prefix, StatsConsoleFrowarderConfig.defaultPeriod);
  }
  
  public StatsConsoleForwarder(String prefix, long period) {
    this.period = period;
    writer = new StatsConsoleWriter(prefix);
  }
  
  public StatsConsoleForwarder(StatsConsoleFrowarderConfig config) {
    this(config.prefix, config.period);
  }
  
  // IComposerRuntimeModule implementation
  
  @Override
  public void onStart(Vertx vertx) throws Exception {
    if (this.vertx != null) {
      throw new Exception("already started");
    }
    nextTs = Stopwatch.timestamp() + period;
    this.vertx = vertx;
  }
  
  @Override
  public void onStop(Vertx vertx) throws Exception {
    if (this.vertx != vertx) {
      throw new Exception("unexpected stop");
    }
    nextTs = Long.MAX_VALUE;
  }
  
  @Override
  public void onPulse(long ts) {
    if (ts < nextTs) {
      return;
    }
    nextTs = ts + period;
    if (!isDirty()) {
      return;
    }
    System.out.println("----------------------------------------------------");
    drain(writer);
  }
  
  // IStatsCollector implementation
  
  @Override
  public void sum(String aspect, double sum) {
    if (period > 0) {
      super.sum(aspect, sum);
    } else {
      writer.sum(aspect, sum);
    }
  }
  
  @Override
  public void avg(String aspect, double sum, long n) {
    if (period > 0) {
      super.avg(aspect, sum, n);
    } else {
      writer.avg(aspect, sum, n);
    }
  }
  
  @Override
  public void norm(String aspect, double sum, long n, double sumSq) {
    if (period > 0) {
      super.norm(aspect, sum, n, sumSq);
    } else {
      writer.norm(aspect, sum, n, sumSq);
    }
  }
  
  @Override
  public void series(String aspect, double[] values, int offset, int size) {
    if (period > 0) {
      super.series(aspect, values, offset, size);
    } else {
      writer.series(aspect, values, offset, size);
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
