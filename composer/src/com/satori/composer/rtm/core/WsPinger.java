package com.satori.composer.rtm.core;

import com.satori.libs.async.core.*;

import io.vertx.core.http.impl.*;
import io.vertx.core.http.impl.ws.*;

public class WsPinger extends WsFrameInterceptor<IWsPingerContext> {
  
  private boolean started;
  private long nextPing = Long.MAX_VALUE;
  
  public WsPinger(IWsPingerContext ctx, IWsFrameHandler slave) {
    super(ctx, slave);
  }
  
  // IWsFrameHandler implementation
  
  @Override
  public void onStart(IWsFrameController master) throws Throwable {
    started = true;
    nextPing = Stopwatch.timestamp() + ctx.pingInterval();
    super.onStart(master);
  }
  
  @Override
  public void onStop() throws Throwable {
    started = false;
    nextPing = Long.MAX_VALUE;
    super.onStop();
  }
  
  @Override
  public void onPulse(long timestamp) throws Throwable {
    if (started && timestamp >= nextPing) {
      if (!isWritable()) {
        log().info("rtm ping delayed, since channel is not writable ({})", ctx);
        return;
      }
      log().debug("rtm ping ({})", ctx);
      nextPing = timestamp + ctx.pingInterval();
      WebSocketFrameImpl pingFrame = new WebSocketFrameImpl(FrameType.PING);
      try {
        send(pingFrame);
      } finally {
        pingFrame.release();
      }
    }
    slave.onPulse(timestamp);
  }
}