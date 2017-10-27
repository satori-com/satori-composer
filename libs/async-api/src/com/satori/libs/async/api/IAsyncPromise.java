package com.satori.libs.async.api;

public interface IAsyncPromise<T> extends IAsyncHandler<T>, IAsyncProgress {
  boolean tryComplete(IAsyncResult<? extends T> ar);
  
  default boolean tryFail(Throwable error) {
    return tryComplete(AsyncResults.failed(error));
  }
  
  default boolean trySucceed(T value) {
    return tryComplete(AsyncResults.succeeded(value));
  }
  
  // IAsyncHandler implementation
  @Override
  default void fail(Throwable cause) {
    tryFail(cause);
  }
  
  @Override
  default void complete(IAsyncResult<? extends T> ar) {
    tryComplete(ar);
  }
  
  @Override
  default void succeed(T value) {
    trySucceed(value);
  }
  
  
}
