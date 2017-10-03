package com.satori.async.core;

import com.satori.async.api.*;

import java.util.function.*;

public abstract class AsyncPromise<T> implements IAsyncPromise<T>, IAsyncProgress {
  protected boolean completed;
  
  protected abstract void onSuccess(T value);
  
  protected abstract void onFailure(Throwable error);
  
  public AsyncPromise() {
    this.completed = false;
  }
  
  @Override
  public boolean tryComplete(IAsyncResult<? extends T> ar) {
    if (completed) {
      return false;
    }
    completed = true;
    if (ar.isSucceeded()) {
      onSuccess(ar.getValue());
    } else {
      onFailure(ar.getError());
    }
    return true;
  }
  
  @Override
  public boolean trySucceed(T value) {
    if (completed) {
      return false;
    }
    completed = true;
    onSuccess(value);
    return true;
  }
  
  @Override
  public boolean tryFail(Throwable cause) {
    if (completed) {
      return false;
    }
    completed = true;
    onFailure(cause);
    return true;
  }
  
  @Override
  public boolean isCompleted() {
    return completed;
  }
  
  public static <T> AsyncPromise<T> from(IAsyncHandler<T> cont) {
    return new AsyncPromise<T>() {
      @Override
      protected void onSuccess(T value) {
        cont.succeed(value);
      }
      
      @Override
      protected void onFailure(Throwable error) {
        cont.fail(error);
      }
    };
  }
  
  public static <T> AsyncPromise<T> from(Runnable onSuccess, Consumer<Throwable> onFailure) {
    return new AsyncPromise<T>() {
      @Override
      protected void onSuccess(T value) {
        onSuccess.run();
      }
      
      @Override
      protected void onFailure(Throwable error) {
        onFailure.accept(error);
      }
    };
  }
  
  public static <T> AsyncPromise<T> from(Consumer<T> onSuccess, Consumer<Throwable> onFailure) {
    return new AsyncPromise<T>() {
      @Override
      protected void onSuccess(T value) {
        onSuccess.accept(value);
      }
      
      @Override
      protected void onFailure(Throwable error) {
        onFailure.accept(error);
      }
    };
  }
}
