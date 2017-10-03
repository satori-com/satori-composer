package com.satori.async.api;


public interface IAsyncHandler<T> {
  
  void complete(IAsyncResult<? extends T> ar);
  
  default void succeed(T value) {
    this.complete(AsyncResults.succeeded(value));
  }
  
  @SuppressWarnings("unchecked")
  default void succeed() {
    this.complete((IAsyncResult<T>) AsyncResults.succeededNull);
  }
  
  default void fail(Throwable error) {
    this.complete(AsyncResults.failed(error));
  }
}
