package com.satori.mods.core.async;

public interface IAsyncResult<T> {
  
  default T get() throws Throwable {
    if (isSucceeded()) {
      return getValue();
    }
    throw getError();
  }
  
  T getValue();
  
  Throwable getError();
  
  boolean isSucceeded();
  
  boolean isFailed();
  
}
