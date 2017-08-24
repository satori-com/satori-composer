package com.satori.mods.core.async;

public class AsyncFuture<T> implements IAsyncFuture<T>, IAsyncPromise<T> {
  
  private boolean completed;
  private IAsyncResult<T> result;
  private IAsyncHandler<T> onCompleted;
  
  public AsyncFuture() {
    completed = false;
    result = null;
    onCompleted = null;
  }
  
  @Override
  public IAsyncResult<T> getResult() {
    return result;
  }
  
  public boolean isCompleted() {
    return completed;
  }
  
  @Override
  public boolean tryComplete(final IAsyncResult<T> ar) {
    if (completed) {
      return false;
    }
    completed = true;
    this.result = ar;
    processCompleted();
    return true;
  }
  
  @Override
  public void onCompleted(IAsyncHandler<T> cont) {
    if (isCompleted()) {
      cont.complete(result);
      return;
    }
    onCompleted = cont;
  }
  
  private void processCompleted() {
    IAsyncHandler<T> onCompleted = this.onCompleted;
    if (onCompleted != null) {
      this.onCompleted = null;
      onCompleted.complete(result);
    }
  }
}
