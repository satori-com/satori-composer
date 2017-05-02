package com.satori.composer.rtm.core;

import com.satori.mods.core.async.*;

import java.util.*;

import io.vertx.core.http.*;
import org.slf4j.*;

public class WsFrameInterceptor<TContext extends IRtmContext> implements IWsFrameController, IWsFrameHandler, IRtmContext {
  IWsFrameHandler slave;
  IWsFrameController master;
  TContext ctx;
  
  public WsFrameInterceptor(TContext ctx, IWsFrameHandler slave) {
    this.ctx = ctx;
    this.slave = slave;
  }
  
  // IWsFrameController implementation
  
  @Override
  public void onStart(IWsFrameController master) throws Throwable {
    this.master = master;
    slave.onStart(this);
  }
  
  @Override
  public void onStop() throws Throwable {
    this.master = null;
    slave.onStop();
  }
  
  @Override
  public void onRecv(WebSocketFrame frame) throws Throwable {
    slave.onRecv(frame);
  }
  
  @Override
  public void onPulse(long timestamp) throws Throwable {
    slave.onPulse(timestamp);
  }
  
  @Override
  public void onWritableChanged(boolean isWritable) throws Throwable {
    slave.onWritableChanged(isWritable);
  }
  
  // IWsFrameHandler implementation
  
  @Override
  public void send(WebSocketFrame frame) {
    master.send(frame);
  }
  
  @Override
  public boolean isWritable() {
    return master.isWritable();
  }
  
  @Override
  public void fail(Throwable cause) {
    master.fail(cause);
  }
  
  @Override
  public void close() {
    master.close();
  }
  
  @Override
  public Logger log() {
    return ctx.log();
  }
  
  @Override
  public String genUid() {
    return ctx.genUid();
  }
  
  @Override
  public ArrayDeque<IAsyncHandler<Boolean>> onConnected() {
    return ctx.onConnected();
  }
  
  @Override
  public ArrayDeque<IAsyncHandler<Boolean>> onWritable() {
    return ctx.onWritable();
  }
}