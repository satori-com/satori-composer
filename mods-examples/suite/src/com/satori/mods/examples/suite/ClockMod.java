package com.satori.mods.examples.suite;

import com.satori.mods.core.async.*;
import com.satori.mods.suite.*;

import org.slf4j.*;

public class ClockMod extends Mod {
  private final static Logger log = LoggerFactory.getLogger(ClockMod.class);
  
  @Override
  public void onStart() throws Exception {
    exec(this::process);
  }
  
  private void onReady(IAsyncResult ar) {
    if (ar.isFailed()) {
      log.error("failure", ar.getError());
    }
    process();
  }
  
  @SuppressWarnings("unchecked")
  private void process() {
    while (true) {
      final IAsyncFuture future;
      try {
        future = yield(System.currentTimeMillis());
      } catch (Throwable e) {
        log.warn("failed to process message", e);
        continue;
      }
      if (future == null) {
        log.error("internal error", new NullPointerException());
        continue;
      }
      try {
        if (!future.isCompleted()) {
          future.onCompleted(this::onReady);
          return;
        }
        if (future.isFailed()) {
          log.error("failure", future.getError());
        }
      } catch (Throwable e) {
        log.error("internal error", e);
      }
    }
  }
}

