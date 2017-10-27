package com.satori.libs.async.core;

import com.satori.libs.async.api.*;

import java.util.*;
import java.util.function.*;

public class AsyncQueue<T> implements IAsyncHandler<T> {
  private enum State {emptyOrFatting, emptyOrStarving}
  
  private final ArrayDeque<Object> queue = new ArrayDeque<>();
  private State state = State.emptyOrFatting;
  
  // IAsyncHandler implementation
  
  @SuppressWarnings("unchecked")
  @Override
  public void succeed(T el) {
    if (state == State.emptyOrStarving) {
      Object cont = queue.pollFirst();
      if (cont != null) {
        ((IAsyncHandler<T>) cont).succeed(el);
        return;
      }
      state = State.emptyOrFatting;
    }
    queue.addLast(AsyncResults.succeeded(el));
  }
  
  @SuppressWarnings("unchecked")
  @Override
  public void fail(Throwable ex) {
    if (state == State.emptyOrStarving) {
      Object cont = queue.pollFirst();
      if (cont != null) {
        ((IAsyncHandler<T>) cont).fail(ex);
        return;
      }
      state = State.emptyOrFatting;
    }
    queue.addLast(AsyncResults.failed(ex));
  }
  
  @SuppressWarnings("unchecked")
  @Override
  public void complete(IAsyncResult<? extends T> ar) {
    promise(AsyncResult.asFuture(ar));
  }
  
  @SuppressWarnings("unchecked")
  public void promise(IAsyncFuture<? extends T> future) {
    if (state == State.emptyOrStarving) {
      Object cont = queue.pollFirst();
      if (cont != null) {
        future.onCompleted((IAsyncHandler<? super T>) cont);
        return;
      }
      state = State.emptyOrFatting;
    }
    queue.addLast(future);
  }
  
  @SuppressWarnings("unchecked")
  public IAsyncHandler<? super T> promise() {
    if (state == State.emptyOrStarving) {
      Object cont = queue.pollFirst();
      if (cont != null) {
        return (IAsyncHandler<T>) cont;
      }
      state = State.emptyOrFatting;
    }
    AsyncFuture<T> promise = new AsyncFuture<>();
    queue.addLast(promise);
    return promise;
  }
  
  @SuppressWarnings("unchecked")
  public IAsyncHandler<? extends T> tryEnq() {
    if (state == State.emptyOrStarving) {
      return (IAsyncHandler<T>) queue.pollFirst();
    }
    return null;
  }
  
  @SuppressWarnings("unchecked")
  public <R> R tryEnq(Function<IAsyncHandler<? extends T>, R> block) {
    if (state == State.emptyOrStarving) {
      Object el = queue.pollFirst();
      if (el != null) {
        return block.apply((IAsyncHandler<T>) el);
      }
    }
    return null;
  }
  
  @SuppressWarnings("unchecked")
  public void enq(T el) {
    succeed(el);
  }
  
  @SuppressWarnings("unchecked")
  public void deq(IAsyncHandler<? super T> cont) {
    if (state == State.emptyOrFatting) {
      IAsyncFuture<T> f = (IAsyncFuture<T>) queue.pollFirst();
      if (f != null) {
        f.onCompleted(cont);
        return;
      }
      state = State.emptyOrStarving;
    }
    queue.addLast(cont);
  }
  
  @SuppressWarnings("unchecked")
  public IAsyncFuture<? extends T> deq() {
    if (state == State.emptyOrFatting) {
      IAsyncFuture<T> f = (IAsyncFuture<T>) queue.pollFirst();
      if (f != null) {
        return f;
      }
      state = State.emptyOrStarving;
    }
    AsyncFuture<T> future = new AsyncFuture<>();
    queue.addLast(future);
    return future;
  }
  
  
  @SuppressWarnings("unchecked")
  public IAsyncFuture<? extends T> tryDeq() {
    if (state == State.emptyOrFatting) {
      return (IAsyncFuture<T>) queue.pollFirst();
    }
    return null;
  }
  
  @SuppressWarnings("unchecked")
  public <R> R tryDeq(Function<IAsyncFuture<? extends T>, R> block) {
    if (state == State.emptyOrFatting) {
      IAsyncFuture<? extends T> f = (IAsyncFuture<? extends T>) queue.pollFirst();
      if (f != null) {
        return block.apply(f);
      }
    }
    return null;
  }
}