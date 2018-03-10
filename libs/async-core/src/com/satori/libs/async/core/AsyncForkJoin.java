package com.satori.libs.async.core;

import com.satori.libs.async.api.*;

public class AsyncForkJoin implements IAsyncFuture, IAsyncHandler, IAsyncResult {
  
  private int val;
  private IAsyncHandler<?> cont;
  
  public Throwable lastError;
  
  public AsyncForkJoin() {
    this(1);
  }
  
  public AsyncForkJoin(int val) {
    this.val = val;
    this.cont = null;
  }
  
  public void inc() {
    if (isCompleted()) throw new RuntimeException("async fork join was already completed");
    val += 1;
  }
  
  public void dec() {
    if (isCompleted()) throw new RuntimeException("async fork join was already completed");
    val -= 1;
    IAsyncHandler<?> cont = this.cont;
    if (val == 0 && cont != null) {
      this.cont = null;
      cont.succeed();
    }
  }
  
  // IAsyncHandler implementation
  
  @Override
  public void complete(IAsyncResult ar) {
    if (isCompleted()) throw new RuntimeException("async fork join was already completed");
    if (!ar.isSucceeded()) {
      // TODO: log error
      lastError = ar.getError();
    }
    val -= 1;
    IAsyncHandler<?> cont = this.cont;
    if (val == 0 && cont != null) {
      this.cont = null;
      cont.succeed();
    }
  }
  
  // IAsyncFuture implementation
  
  @Override
  public boolean isCompleted() {
    return val == 0;
  }
  
  @Override
  public void onCompleted(IAsyncHandler cont) {
    if (val == 0) {
      cont.succeed();
      return;
    }
    this.cont = cont;
  }
  
  @Override
  public IAsyncResult getResult() {
    return this;
  }
  
  // IAsyncResult implementation
  
  @Override
  public Object get() throws Throwable {
    return null;
  }
  
  @Override
  public Object getValue() {
    return null;
  }
  
  @Override
  public Throwable getError() {
    return null;
  }
  
  @Override
  public boolean isSucceeded() {
    return val == 0;
  }
}
