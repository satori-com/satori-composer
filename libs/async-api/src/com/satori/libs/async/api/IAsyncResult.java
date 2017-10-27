package com.satori.libs.async.api;

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
}
