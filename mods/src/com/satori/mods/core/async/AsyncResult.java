package com.satori.mods.core.async;

public abstract class AsyncResult<T> implements IAsyncResult<T>, IAsyncFuture<T> {
  
  // IAsyncResult implementation
  
  @Override
  public abstract boolean isSucceeded();
  
  @Override
  public abstract boolean isFailed();
  
  @Override
  public abstract Throwable getError();
  
  @Override
  public abstract T getValue();
  
  // IAsyncFuture implementation
  
  @Override
  public boolean isCompleted() {
    return true;
  }
  
  @Override
  public void onCompleted(IAsyncHandler<T> cont) {
    cont.complete(this);
  }
  
  @Override
  public IAsyncResult<T> getResult() {
    return this;
  }
}
