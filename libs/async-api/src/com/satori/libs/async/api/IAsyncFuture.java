package com.satori.libs.async.api;

public interface IAsyncFuture<T> extends IAsyncPendingResult<T> {
  void onCompleted(IAsyncHandler<? super T> cont);
}
