package com.satori.async.core;

import com.satori.async.api.*;

public class AsyncFuture<T> implements IAsyncFuture<T>, IAsyncPromise<T> {
  
  private boolean completed;
  private IAsyncResult<? extends T> result;
  private IAsyncHandler<? super T> onCompleted;
  
  public AsyncFuture() {
    completed = false;
    result = null;
    onCompleted = null;
  }
  
  @Override
  public IAsyncResult<? extends T> getResult() {
    return result;
  }
  
  public boolean isCompleted() {
    return completed;
  }
  
  @Override
  public boolean tryComplete(final IAsyncResult<? extends T> ar) {
    if (completed) {
      return false;
    }
    completed = true;
    this.result = ar;
    processCompleted();
    return true;
  }
  
  @Override
  public void onCompleted(IAsyncHandler<? super T> cont) {
    if (isCompleted()) {
      cont.complete(result);
      return;
    }
    onCompleted = cont;
  }
  
  private void processCompleted() {
    IAsyncHandler<? super T> onCompleted = this.onCompleted;
    if (onCompleted != null) {
      this.onCompleted = null;
      onCompleted.complete(result);
    }
  }
}
