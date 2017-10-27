package com.satori.libs.async.api;

public class AsyncSucceededResult<T> extends AsyncResult<T> {
  
  final T value;
  
  public AsyncSucceededResult(T value) {
    this.value = value;
  }
  
  // AsyncResult implementation
  
  @Override
  public T getValue() {
    return value;
  }
  
  @Override
  public T get() throws Throwable {
    return value;
  }

  @Override
  public boolean isSucceeded() {
    return true;
  }
  
  @Override
  public Throwable getError() {
    return null;
  }
  
  
}
