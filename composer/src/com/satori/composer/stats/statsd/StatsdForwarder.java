package com.satori.composer.stats.statsd;

import com.satori.composer.runtime.*;
import com.satori.composer.stats.*;

import io.vertx.core.*;
import io.vertx.core.buffer.*;
import io.vertx.core.datagram.*;
import org.slf4j.*;

public class StatsdForwarder extends StatsdAggregator implements IStatsForwarder {
  public static final Logger log = LoggerFactory.getLogger(StatsdForwarder.class);
  
  public final long period;
  public final String prefix;
  public final String tags;
  public final String host;
  public final int port;
  protected DatagramSocket socket = null;
  private long nextTs = Long.MAX_VALUE;
  private long noAck = 0;
  
  public StatsdForwarder(StatsdForwarderConfig config) {
    String prefix = config.prefix;
    this.prefix = prefix == null || prefix.isEmpty() ? null : prefix + ".";
    period = config.period;
    host = config.host;
    port = config.port;
    if (config.tags == null || config.tags.length == 0) {
      tags = null;
    } else {
      tags = String.join(",", config.tags);
    }
    
    nextTs = Long.MAX_VALUE;
  }
  
  // IComposerRuntimeModule implementation
  
  @Override
  public void onStart(Vertx vertx) {
    socket = vertx.createDatagramSocket();
    nextTs = Stopwatch.timestamp() + period;
    reset();
  }
  
  @Override
  public void onStop(Vertx vertx) {
    if (socket != null) {
      socket.close();
      socket = null;
    }
    nextTs = Long.MAX_VALUE;
    reset();
  }
  
  @Override
  public void onPulse(long ts) {
    if (noAck > 0 || ts < nextTs) {
      return;
    }
    nextTs = ts + period;
    if (!isDirty()) {
      return;
    }
    
    sum.forEach((a, m) -> {
      send(a, Double.toString(m.sum), "|c");
    });
    avg.forEach((a, m) -> {
      if (m.n > 0) {
        send(a, Double.toString(m.sum / m.n), "|g");
      }
    });
    reset();
  }
  
  // IStatsForwarder implementation
  
  @Override
  public void dispose() {
    
  }
  
  @Override
  public boolean disposed() {
    return false;
  }
  
  // protected methods
  
  protected void send(String aspect, String val, String type) {
    if (socket == null) {
      log.warn("failed to send statistics");
      return;
    }
    if (aspect == null || aspect.isEmpty()) {
      log.warn("aspect is empty or null");
      return;
    }
    try {
      noAck += 1;
      log.debug("{}: {}{}", aspect, val, type);
      Buffer buf = Buffer.buffer();
      if (prefix != null) {
        buf.appendString(prefix);
      }
      buf.appendString(aspect)
        .appendString(":")
        .appendString(val)
        .appendString(type);
      if (tags != null && !tags.isEmpty()) {
        buf.appendString("|#")
          .appendString(tags);
      }
      buf.appendString("\n");
      
      socket.send(buf, port, host, ar -> {
        noAck -= 1;
        if (!ar.succeeded()) {
          log.warn("failed to send statistics", ar.cause());
        }
      });
    } catch (Exception cause) {
      noAck -= 1;
      log.warn("failed to send statistics", cause);
    }
  }
}
