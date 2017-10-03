package com.satori.composer.rtm.core;


import com.satori.async.api.*;

import java.util.*;

import com.fasterxml.jackson.databind.*;
import org.slf4j.*;

public class RtmPduInterceptor<TContext extends IRtmContext> implements IRtmPduController, IRtmPduHandler, IRtmContext {
  IRtmPduHandler slave;
  IRtmPduController master;
  TContext ctx;
  
  public RtmPduInterceptor(TContext ctx, IRtmPduHandler slave) {
    this.ctx = ctx;
    this.master = null;
    this.slave = slave;
  }
  
  // IRtmPduHandler implementation
  
  @Override
  public void onStart(IRtmPduController master) throws Throwable {
    this.master = master;
    slave.onStart(this);
  }
  
  @Override
  public void onStop() throws Throwable {
    this.master = null;
    slave.onStop();
  }
  
  @Override
  public void onRecv(RtmPdu<JsonNode> pdu) throws Throwable {
    slave.onRecv(pdu);
  }
  
  @Override
  public void onPulse(long timestamp) throws Throwable {
    slave.onPulse(timestamp);
  }
  
  @Override
  public void onWritableChanged(boolean isWritable) throws Throwable {
    slave.onWritableChanged(isWritable);
  }
  
  // IRtmPduController implementation
  
  @Override
  public <T> void send(RtmPdu<T> pdu) {
    master.send(pdu);
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
  
  
  // IRtmContext implementation
  
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