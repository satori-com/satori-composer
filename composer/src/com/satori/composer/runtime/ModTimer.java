package com.satori.composer.runtime;

import com.satori.libs.async.api.*;
import com.satori.libs.async.core.*;

import java.util.concurrent.*;

import io.netty.channel.*;
import io.vertx.core.*;
import io.vertx.core.impl.*;
import org.slf4j.*;

public class ModTimer extends AsyncFuture implements IAsyncFutureDisposable, Runnable {
  public final static Logger log = LoggerFactory.getLogger(ModTimer.class);
  
  private ScheduledFuture future;
  
  public ModTimer(long delay, Vertx vertx) {
    ContextInternal ctx = (ContextInternal) vertx.getOrCreateContext();
    if (!ctx.isEventLoopContext()) {
      throw new RuntimeException("event loop context expected");
    }
    EventLoop eventLoop = ctx.nettyEventLoop();
    future = eventLoop.schedule(this, delay, TimeUnit.MILLISECONDS);
  }
  
  @SuppressWarnings("unchecked")
  @Override
  public void run() {
    if (future == null) {
      // sanity check
      log.error("unexpected timer callback", new IllegalStateException());
    }
    future = null;
    if (!trySucceed(null)) {
      // sanity check
      log.error("timer future already completed", new IllegalStateException());
    }
  }
  
  @Override
  public void dispose() {
    ScheduledFuture future = this.future;
    this.future = null;
    if (future != null) {
      try {
        future.cancel(true);
      } catch (Exception ex) {
        // swallow exception
        log.error("failed to cancel timer", ex);
      }
    }
    tryFail(DisposedException.instance);
  }
  
}
