package com.satori.composer.rtm.core;

import io.vertx.core.http.*;

public class RtmConnectingState<TContext extends IRtmFactoryContext> extends RtmState<TContext, WebSocket> {
  
  public RtmConnectingState(TContext ctx) {
    super(ctx);
  }
  
  public boolean enter(Runnable cont) {
    if (!super.enter(cont)) {
      return false;
    }
    ctx.connect(this);
    return true;
  }
  
  private void onWebSocketConnected(WebSocket ws) {
    if (ws == null) {
      fail(new Exception("null websocket returned"));
      return;
    }
    if (!trySucceed(ws)) {
      ws.close();
    }
  }
  
  private void onWebSocketError(Throwable cause) {
    fail(cause);
  }
  
  @Override
  public void onPulse(long timestamp) {
    if (checkNotCompleted() && age(timestamp) > ctx.connectTimeout()) {
      fail(new Exception("connecting timeouted"));
    }
  }
  
  @Override
  public void stop() {
    succeed(null);
  }
}
