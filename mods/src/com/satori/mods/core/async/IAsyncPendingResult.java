package com.satori.mods.core.async;

public interface IAsyncPendingResult<T> extends IAsyncProgress {
  
  IAsyncResult<T> getResult();
  
  @Override
  default boolean isCompleted(){
    return getResult() != null;
  }
}
