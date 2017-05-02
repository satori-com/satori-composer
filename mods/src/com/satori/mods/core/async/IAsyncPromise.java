package com.satori.mods.core.async;

public interface IAsyncPromise<T> extends IAsyncHandler<T>, IAsyncProgress {
  boolean tryComplete(IAsyncResult<T> ar);
  
  default boolean tryFail(Throwable error) {
    return tryComplete(AsyncResults.failed(error));
  }
  
  default boolean trySucceed(T value) {
    return tryComplete(AsyncResults.succeeded(value));
  }
  
  default boolean tryFail(String message) {
    return tryFail(new Exception(message));
  }
  
  default boolean tryFail(String message, Throwable inner) {
    return tryFail(new Exception(message, inner));
  }
  
  
  @Override
  default void fail(Throwable cause) {
    tryFail(cause);
  }
  
  @Override
  default void fail(String message, Throwable inner) {
    tryFail(message, inner);
  }
  
  @Override
  default void fail(String message) {
    tryFail(message);
  }
  
  @Override
  default void complete(IAsyncResult<T> ar) {
    tryComplete(ar);
  }
  
  @Override
  default void succeed(T value) {
    trySucceed(value);
  }
  
  
}
