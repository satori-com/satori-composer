package com.satori.mods.core.async;

public interface IAsyncPendingResult<T> extends IAsyncProgress {
  
  IAsyncResult<T> getResult();
  
  default T getValue() {
    IAsyncResult<T> ar = getResult();
    return ar == null ? null : ar.getValue();
  }
  
  default Throwable getError() {
    IAsyncResult<T> ar = getResult();
    return ar == null ? null : ar.getError();
  }
  
  default boolean isSucceeded() {
    IAsyncResult<T> ar = getResult();
    return ar != null && ar.isSucceeded();
  }
  
  default boolean isFailed() {
    IAsyncResult<T> ar = getResult();
    return ar != null && ar.isFailed();
  }
}
