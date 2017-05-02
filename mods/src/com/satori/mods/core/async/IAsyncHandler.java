package com.satori.mods.core.async;


public interface IAsyncHandler<T> {
  
  void complete(IAsyncResult<T> ar);
  
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
  
  default void fail(String msg) {
    this.complete(AsyncResults.failed(msg));
  }
  
  default void fail(String msg, Throwable inner) {
    this.complete(AsyncResults.failed(msg, inner));
  }
}
