package com.satori.async.api;

public abstract class AsyncResult<T> implements IAsyncResult<T>, IAsyncFuture<T> {
  
  // IAsyncResult implementation
  
  @Override
  public abstract boolean isSucceeded();
  
  @Override
  public abstract Throwable getError();
  
  @Override
  public abstract T getValue();
  
  @Override
  public T get() throws Throwable{
    if(isSucceeded()){
      return getValue();
    }
    throw getError();
  }

  // IAsyncFuture implementation
  
  @Override
  public boolean isCompleted() {
    return true;
  }
  
  @Override
  public void onCompleted(IAsyncHandler<? super T> cont) {
    cont.complete(this);
  }
  
  @Override
  public IAsyncResult<T> getResult() {
    return this;
  }
  
  public static<T> IAsyncFuture<T> asFuture(IAsyncResult<T> ar){
    return new AsyncResult<T>() {
      Throwable error = ar.getError();
      T value = ar.getValue();
      boolean isSucceeded = ar.isSucceeded();
  
      @Override
      public boolean isSucceeded() {
        return isSucceeded;
      }
  
      @Override
      public Throwable getError() {
        return error;
      }
  
      @Override
      public T getValue() {
        return value;
      }
    };
  }
}
