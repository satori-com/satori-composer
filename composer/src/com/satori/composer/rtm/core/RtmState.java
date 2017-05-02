package com.satori.composer.rtm.core;


import com.satori.composer.runtime.*;
import com.satori.mods.core.async.*;

import java.util.*;

import com.fasterxml.jackson.databind.*;
import org.slf4j.*;
import org.slf4j.helpers.*;


public abstract class RtmState<TContext extends IRtmContext, TExit> implements IRtmState<TExit>, IRtmContext, IAsyncPromise<TExit> {
  
  public final long created;
  public final TContext ctx;
  
  protected Runnable cont;
  protected Stage stage = Stage.incomplete;
  protected Object result = null;
  
  public RtmState(TContext ctx) {
    this.ctx = ctx;
    created = now();
  }
  
  //
  
  public boolean enter(Runnable cont) {
    if (this.cont != null) {
      log().error("assumption 'cont == null' failed ({} != null)", cont);
      return false;
    }
    if (cont == null) {
      log().error("assumption 'cont != null' failed");
      return false;
    }
    if (isCompleted()) {
      log().error("assumption '!completed' failed (stage={}, result={})", stage, result);
      return false;
    }
    
    this.cont = cont;
    return true;
  }
  
  private boolean complete(Stage finalStage, Object result) {
    if (isCompleted()) {
      return false;
    }
    this.stage = finalStage;
    this.result = result;
    Runnable cont = this.cont;
    this.cont = null;
    onCompletion();
    cont.run();
    return true;
  }
  
  protected void onCompletion() {
  }
  
  protected boolean complete(IRtmState<? extends TExit> other) {
    if (other == null) {
      return tryFail(checkFailed("null completion source"));
    }
    if (other.succeeded()) {
      return trySucceed(other.result());
    }
    return tryFail(other.cause());
  }
  
  // ICompletionSink implementation
  
  
  @Override
  public boolean tryComplete(IAsyncResult<TExit> ar) {
    if (ar.isSucceeded()) {
      return complete(Stage.succeeded, ar.getValue());
    } else {
      return complete(Stage.failed, ar.getError());
    }
  }
  
  @Override
  public boolean trySucceed(TExit result) {
    return complete(Stage.succeeded, result);
  }
  
  @Override
  public boolean tryFail(Throwable cause) {
    return complete(Stage.failed, cause);
  }
  
  public CheckFailedException checkFailed(String message, Object... args) {
    CheckFailedException cause = new CheckFailedException(
      MessageFormatter.format(message, args).getMessage()
    );
    log().error("check failed", cause);
    return cause;
  }
  
  public boolean checkNotCompleted() {
    if (isCompleted()) {
      checkFailed("not in completed stage({})", stage);
      return false;
    }
    return true;
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
  
  // IRtmState implementation
  
  @Override
  public long created() {
    return created;
  }
  
  @Override
  public boolean isCompleted() {
    return stage != Stage.incomplete;
  }
  
  @Override
  public boolean succeeded() {
    return stage == Stage.succeeded;
  }
  
  @Override
  public boolean failed() {
    return stage == Stage.failed;
  }
  
  @SuppressWarnings("unchecked")
  @Override
  public TExit result() {
    if (succeeded()) {
      return (TExit) result;
    }
    return null;
  }
  
  @Override
  public Throwable cause() {
    if (failed()) {
      return (Throwable) result;
    }
    return null;
  }
  
  @Override
  public boolean dispose() {
    cont = null;
    if (!isCompleted()) {
      result = null;
      stage = Stage.failed;
      return true;
    }
    return false;
  }
  
  // IRtm implementation
  
  @Override
  public void start() {
  }
  
  @Override
  public void stop() {
  }
  
  @Override
  public boolean stopped() {
    return false;
  }
  
  @Override
  public boolean publish(String channel, JsonNode msg) {
    log().info("rtm publishing skipped ({})", ctx);
    return false;
  }
  
  @Override
  public <T> boolean publish(String channel, T msg) {
    log().info("rtm publishing skipped ({})", ctx);
    return false;
  }
  
  @Override
  public boolean isConnected() {
    return false;
  }
  
  @Override
  public boolean onConnected(IAsyncHandler<Boolean> cont) {
    onConnected().addLast(cont);
    return true;
  }
  
  @Override
  public boolean isWritable() {
    return false;
  }
  
  @Override
  public boolean onWritable(IAsyncHandler<Boolean> cont) {
    onWritable().addLast(cont);
    return true;
  }
  
  // subtypes
  
  enum Stage {
    incomplete, succeeded, failed
  }
}
