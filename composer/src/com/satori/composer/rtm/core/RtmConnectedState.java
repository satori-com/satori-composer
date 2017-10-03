package com.satori.composer.rtm.core;

import com.satori.async.api.*;

import com.fasterxml.jackson.databind.*;
import io.vertx.core.http.*;

public class RtmConnectedState<TContext extends RtmDefaultContext> extends RtmState<TContext, Void> {
  
  //protected WebSocket ws;
  protected IRtmPipelineHandler pipeline;
  protected IRtmPduController tail;
  
  public RtmConnectedState(TContext ctx, WebSocket ws) {
    super(ctx);
    pipeline = createPipeline(ws, createPipelineHandler());
  }
  
  protected IRtmPduHandler createPipelineHandler() {
    return new IRtmPduHandler() {
      
      @Override
      public void onStart(IRtmPduController master) throws Throwable {
        onPipelineStart(master);
      }
      
      @Override
      public void onStop() throws Throwable {
        onPipelineStop();
      }
      
      @Override
      public void onRecv(RtmPdu<JsonNode> pdu) throws Throwable {
        onPipelineRecv(pdu);
      }
      
      @Override
      public void onPulse(long timestamp) throws Throwable {
        onPipelinePulse(timestamp);
      }
      
      @Override
      public void onWritableChanged(boolean isWritable) throws Throwable {
        onPipelineWritableChanged(isWritable);
      }
    };
  }
  
  protected IRtmPipelineHandler createPipeline(WebSocket ws, IRtmPduHandler handler) {
    final RtmParser parser;
    if (ctx.auth() != null) {
      RtmAuthenticator auth = new RtmAuthenticator(ctx, handler);
      parser = new RtmParser(ctx, auth);
    } else {
      parser = new RtmParser(ctx, handler);
    }
    WsPinger pinger = new WsPinger(ctx, parser);
    WebSockAdapter adapter = new WebSockAdapter(ws, ctx, pinger);
    
    return adapter;
  }
  
  // IRtmState implementation
  
  @Override
  public boolean enter(Runnable cont) {
    if (!super.enter(cont)) {
      return false;
    }
    try {
      pipeline.onStart(cause -> fail(cause));
    } catch (Throwable cause) {
      fail(cause);
    }
    return true;
  }
  
  @Override
  public void onPulse(long timestamp) {
    if (isCompleted()) {
      return;
    }
    try {
      pipeline.onPulse(timestamp);
    } catch (Throwable cause) {
      fail(cause);
    }
  }
  
  // RtmState implementation
  
  @Override
  protected void onCompletion() {
    try {
      if (pipeline != null) {
        pipeline.onStop();
        pipeline = null;
      }
    } catch (Throwable cause) {
      log().error("unhandled exception during completion", cause);
    }
  }
  
  // IRtm implementation
  
  @Override
  public void stop() {
    if (isCompleted()) {
      log().error("assumption !completed failed");
      return;
    }
    succeed(null);
  }
  
  @Override
  public boolean publish(String channel, JsonNode msg) {
    if (tail != null) {
      tail.send(new RtmPublishPdu<>(channel, msg));
      return true;
    }
    return false;
  }
  
  @Override
  public <T> boolean publish(String channel, T msg) {
    if (tail != null) {
      tail.send(new RtmPublishPdu<>(channel, msg));
      return true;
    }
    return false;
    
  }
  
  @Override
  public boolean isConnected() {
    return tail != null;
  }
  
  @Override
  public boolean onConnected(IAsyncHandler<Boolean> cont) {
    if (pipeline == null) {
      log().error("state has been changed, can't setup handler");
      // TODO: somehow move handler to another state
      return false;
    }
    if (tail == null) {
      // pipeline not ready, save handler...
      if (cont != null) {
        ctx.onConnected().addLast(cont);
      }
    } else if (cont != null) {
      cont.succeed(null);
    }
    return true;
  }
  
  @Override
  public boolean isWritable() {
    if (tail != null) {
      return tail.isWritable();
    }
    return false;
  }
  
  @Override
  public boolean onWritable(IAsyncHandler<Boolean> cont) {
    if (pipeline == null) {
      log().error("state has been changed, can't setup handler");
      // TODO: somehow move handler to another state
      return false;
    }
    if (tail == null) {
      // pipeline not ready, save handler...
      if (cont != null) {
        ctx.onWritable().addLast(cont);
      }
    } else if (cont != null) {
      if (isWritable()) {
        cont.succeed(Boolean.TRUE);
      } else {
        ctx.onWritable().addLast(cont);
      }
      
    }
    return true;
  }
  
  // pipeline handlers
  
  public void onPipelineStart(IRtmPduController master) throws Throwable {
    tail = master;
    IAsyncHandler<Boolean> onConnected;
    while ((onConnected = ctx.onConnected().pollFirst()) != null) {
      try {
        onConnected.succeed(Boolean.TRUE);
      } catch (Throwable cause) {
        log().error("unhandled exception", cause);
      }
    }
    onPipelineWritableChanged(tail.isWritable());
  }
  
  public void onPipelineStop() throws Throwable {
    tail = null;
  }
  
  public void onPipelineRecv(RtmPdu<JsonNode> pdu) throws Throwable {
    // unhandled pdu's, fail
    fail(new Exception("unexpected pdu received"));
  }
  
  public void onPipelinePulse(long timestamp) {
    // pulses from last, do nothing
  }
  
  public void onPipelineWritableChanged(boolean isWritable) throws Throwable {
    if (!isWritable) {
      return;
    }
    IAsyncHandler<Boolean> onWritable;
    while ((onWritable = ctx.onWritable().pollFirst()) != null) {
      try {
        onWritable.succeed(Boolean.TRUE);
      } catch (Throwable e) {
        log().error("unhandled exception", e);
      }
      if (!isWritable()) {
        return;
      }
    }
  }
  
}
