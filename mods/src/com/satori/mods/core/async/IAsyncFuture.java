package com.satori.mods.core.async;

public interface IAsyncFuture<T> extends IAsyncPendingResult<T> {
  void onCompleted(IAsyncHandler<T> cont);
}
