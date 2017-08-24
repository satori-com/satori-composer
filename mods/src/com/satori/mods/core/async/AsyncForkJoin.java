package com.satori.mods.core.async;

public class AsyncForkJoin implements IAsyncFuture, IAsyncHandler, IAsyncResult {
  
  private int val;
  private IAsyncHandler<?> cont;
  
  public AsyncForkJoin() {
    this(1);
  }
  
  public AsyncForkJoin(int val) {
    this.val = val;
    this.cont = cont;
  }
  
  public int inc(){
    return ++val;
  }
  
  public int dec(){
    return --val;
  }
  
  // IAsyncHandler implementation
  
  @Override
  public void complete(IAsyncResult ar) {
    if(ar.isFailed()){
      // TODO: log error
    }
    val -= 1;
    IAsyncHandler<?> cont = this.cont;
    if(val == 0 && cont != null){
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
    if(val == 0){
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
  
  @Override
  public boolean isFailed() {
    return false;
  }
}
