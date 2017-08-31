package com.satori.mods.examples;

import com.satori.mods.api.*;
import com.satori.mods.core.async.*;
import com.satori.mods.suite.*;

import org.slf4j.*;

public class SourceMod extends Mod {
  private final static Logger log = LoggerFactory.getLogger(SourceMod.class);
  
  @Override
  public void init(IModContext context) throws Exception {
    super.init(context);
    exec(this::process);
  }
  
  private void onReady(IAsyncResult ar) {
    if (ar.isFailed()) {
      log.error("failure", ar.getError());
    }
    process();
  }
  
  private void process() {
    while (true) {
      try {
        IAsyncFuture future = yield("Hello world");
        if (!future.isCompleted()) {
          future.onCompleted(this::onReady);
          return;
        }
        if (future.isFailed()) {
          log.error("failure", future.getError());
        }
      } catch (Throwable e) {
        log.error("failure", e);
      }
    }
  }
}

