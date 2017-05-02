package com.satori.mods.core.async;

public class AsyncSucceededResult<T> implements IAsyncResult<T> {
  
  final T value;
  
  public AsyncSucceededResult(T value) {
    this.value = value;
  }
  
  @Override
  public T get() throws Throwable {
    return value;
  }
  
  @Override
  public T getValue() {
    return value;
  }
  
  @Override
  public boolean isSucceeded() {
    return true;
  }
  
  @Override
  public boolean isFailed() {
    return false;
  }
  
  @Override
  public Throwable getError() {
    return null;
  }
}
