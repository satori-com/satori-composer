package com.satori.async.api;

public class AsyncResults {
  public final static AsyncSucceededResult<?> succeededNull = new AsyncSucceededResult<>(null);
  public final static AsyncSucceededResult<Boolean> succeededTrue = new AsyncSucceededResult<>(Boolean.TRUE);
  public final static AsyncSucceededResult<Boolean> succeededFalse = new AsyncSucceededResult<>(Boolean.FALSE);
  
  @SuppressWarnings("unchecked")
  public static <T> AsyncSucceededResult<T> succeeded(T result) {
    if (result == null) {
      return (AsyncSucceededResult<T>) succeededNull;
    }
    return new AsyncSucceededResult<T>(result);
  }
  
  @SuppressWarnings("unchecked")
  public static <T> AsyncSucceededResult<T> succeeded() {
    return (AsyncSucceededResult<T>) succeededNull;
  }
  
  public static AsyncSucceededResult<Boolean> succeeded(boolean result) {
    return result ? succeededTrue : succeededFalse;
  }
  
  @SuppressWarnings("unchecked")
  public static AsyncSucceededResult<Boolean> succeeded(Boolean result) {
    if (result == null) {
      return (AsyncSucceededResult<Boolean>) succeededNull;
    }
    return result ? succeededTrue : succeededFalse;
  }
  
  @SuppressWarnings("unchecked")
  public static <T> AsyncFailedResult<T> failed(Throwable cause) {
    return new AsyncFailedResult<T>(cause);
  }
  
  public static <T> AsyncFailedResult<T> failed(String message, Throwable inner) {
    return new AsyncFailedResult<T>(message, inner);
  }
  
  public static <T> AsyncFailedResult<T> failed(String message) {
    return new AsyncFailedResult<T>(message);
  }
}
