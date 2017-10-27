package com.satori.libs.async.api;

public interface IAsyncPendingResult<T> extends IAsyncProgress {
  
  IAsyncResult<? extends T> getResult();
  
  @Override
  default boolean isCompleted(){
    return getResult() != null;
  }
}
