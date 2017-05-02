package com.satori.composer.runtime;

import java.io.Closeable;
import java.io.*;

import io.vertx.core.*;
import org.slf4j.*;

public class ModTimer implements Closeable, Handler<Long> {
  public final static Logger log = LoggerFactory.getLogger(ModTimer.class);
  public final static long INVALID_TIMER_ID = Long.MIN_VALUE;
  
  private long timerId = INVALID_TIMER_ID;
  private Runnable action = null;
  private final Vertx vertx;
  
  public ModTimer(long delay, Runnable action, Vertx vertx) {
    this.vertx = vertx;
    this.action = action;
    if (action != null) {
      timerId = vertx.setTimer(delay, this);
    }
  }
  
  @Override
  public void handle(Long timerId) {
    if (this.timerId != timerId) {
      log.error("unexpected timer callback");
      return;
    }
    this.timerId = Long.MIN_VALUE;
    Runnable action = this.action;
    this.action = null;
    action.run();
  }
  
  @Override
  public void close() throws IOException {
    if (timerId != Long.MIN_VALUE) {
      try {
        vertx.cancelTimer(timerId);
      } finally {
        timerId = Long.MIN_VALUE;
        action = null;
      }
    }
  }
  
}
