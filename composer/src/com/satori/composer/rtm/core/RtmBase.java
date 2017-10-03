package com.satori.composer.rtm.core;

import com.satori.async.api.*;
import com.satori.composer.runtime.*;

import com.fasterxml.jackson.databind.*;
import io.vertx.core.*;
import io.vertx.core.http.*;

public class RtmBase extends RtmDefaultContext implements IRtm, IPulseObject {
  IRtmState current;
  
  public RtmBase(Vertx vertx, RtmBaseConfig cfg) {
    super(vertx, cfg);
    enterStoppedState();
  }
  
  
  // stopped state
  
  protected IRtmState<Void> createStoppedState() {
    return new RtmStoppedState(this);
  }
  
  protected void enterStoppedState() {
    log().debug("rtm stopped ({})", this);
    IRtmState<Void> state = createStoppedState();
    this.current = state;
    state.enter(() -> leaveStoppedState(state));
  }
  
  protected void leaveStoppedState(IRtmState<Void> state) {
    log().debug("rtm started ({})", this);
    if (!leaveCurrentState(state)) {
      return;
    }
    enterConnectingState();
  }
  
  // connecting state
  
  protected IRtmState<WebSocket> createConnectingState() {
    return new RtmConnectingState<>(this);
  }
  
  protected void enterConnectingState() {
    log().info("rtm connecting... ({})", this);
    IRtmState<WebSocket> state = createConnectingState();
    this.current = state;
    state.enter(() -> leaveConnectingState(state));
  }
  
  protected void leaveConnectingState(IRtmState<WebSocket> state) {
    if (!leaveCurrentState(state)) {
      return;
    }
    
    if (!state.succeeded()) {
      log().error("Failed to connect ({})", this, state.cause());
      enterRelaxingState(reconnectDelay);
      return;
    }
    
    WebSocket ws = state.result();
    if (ws == null) {
      enterStoppedState();
      return;
    }
    
    enterConnectedState(ws);
  }
  
  // connected state
  
  protected IRtmState<Void> createConnectedState(WebSocket ws) {
    return new RtmConnectedState<>(this, ws);
  }
  
  protected boolean enterConnectedState(WebSocket ws) {
    log().info("rtm connected ({})", this);
    IRtmState<Void> state = createConnectedState(ws);
    current = state;
    state.enter(() -> leaveConnectedState(state));
    return true;
  }
  
  protected void leaveConnectedState(IRtmState<Void> state) {
    if (!leaveCurrentState(state)) {
      return;
    }
    
    if (state.succeeded()) {
      log().info("rtm disconnected ({})", this);
      enterStoppedState();
      return;
    }
    
    log().warn("rtm disconnected ({}) ", this, state.cause());
    
    if (state.age(Stopwatch.timestamp()) > reconnectDelay) {
      enterConnectingState();
    } else {
      enterRelaxingState(reconnectDelay);
    }
  }
  
  // relaxing state
  
  protected IRtmState<RtmRelaxingState.Exit> createRelaxingState(long delay) {
    return new RtmRelaxingState(this, delay);
  }
  
  protected void enterRelaxingState(long delay) {
    log().info("rtm relaxing for {} ms ({})", delay, this);
    IRtmState<RtmRelaxingState.Exit> state = createRelaxingState(delay);
    this.current = state;
    state.enter(() -> leaveRelaxingState(state));
  }
  
  protected void leaveRelaxingState(IRtmState<RtmRelaxingState.Exit> state) {
    if (!leaveCurrentState(state)) {
      return;
    }
    this.current = null;
    if (state.result() == RtmRelaxingState.Exit.stopped) {
      enterStoppedState();
      return;
    }
    enterConnectingState();
  }
  
  // IRtm implementation
  
  @Override
  public void start() {
    if (current != null) {
      current.start();
    }
  }
  
  @Override
  public void stop() {
    if (current != null) {
      current.stop();
    }
  }
  
  @Override
  public boolean stopped() {
    return current != null && current.stopped();
  }
  
  @Override
  public boolean publish(String channel, JsonNode msg) {
    return current != null && current.publish(channel, msg);
  }
  
  @Override
  public <T> boolean publish(String channel, T msg) {
    return current != null && current.publish(channel, msg);
  }
  
  @Override
  public boolean isConnected() {
    return current != null && current.isConnected();
  }
  
  @Override
  public boolean onConnected(IAsyncHandler<Boolean> cont) {
    if (current != null) {
      return current.onConnected(cont);
    }
    cont.fail(new Exception("rtm stopped"));
    return true;
  }
  
  @Override
  public boolean isWritable() {
    return current != null && current.isWritable();
  }
  
  @Override
  public boolean onWritable(IAsyncHandler<Boolean> cont) {
    if (current != null) {
      return current.onWritable(cont);
    }
    cont.fail(new Exception("rtm stopped"));
    return true;
  }
  
  // IPulseObject implementation
  
  @Override
  public void onPulse(long timestamp) {
    if (current != null) {
      current.onPulse(timestamp);
    }
  }
  
  //
  
  protected boolean leaveCurrentState(final IRtmState s) {
    if (s == null || !s.completed()) {
      log().error("assumption 's!=null && !s.completed' failed ({},{})", current, s);
    }
    if (current != s) {
      log().error("assumption 'current == s' failed ({} != {})", current, s);
      return false;
    }
    current = null;
    if (s != null) {
      s.dispose();
    }
    return true;
  }
  
}
