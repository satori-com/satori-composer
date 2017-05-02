package com.satori.mods.core.async;

public class AsyncFailedResult<T> implements IAsyncResult<T> {
  private final Throwable error;
  
  public AsyncFailedResult(Throwable error) {
    this.error = error;
  }
  
  public AsyncFailedResult(String message) {
    this.error = new Exception(message);
  }
  
  public AsyncFailedResult(String message, Throwable inner) {
    this.error = new Exception(message, inner);
  }
  
  @Override
  public T get() throws Throwable {
    throw error;
  }
  
  @Override
  public T getValue() {
    return null;
  }
  
  @Override
  public boolean isSucceeded() {
    return false;
  }
  
  @Override
  public boolean isFailed() {
    return true;
  }
  
  @Override
  public Throwable getError() {
    return error;
  }
  
}
