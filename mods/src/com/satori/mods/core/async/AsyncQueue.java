package com.satori.mods.core.async;

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
    switch (state) {
      case emptyOrStarving: {
        Object cont = queue.pollFirst();
        if (cont != null) {
          ((IAsyncHandler<T>) cont).succeed(el);
        } else {
          queue.addLast(AsyncResults.succeeded(el));
          state = State.emptyOrFatting;
        }
        break;
      }
      case emptyOrFatting: {
        queue.addLast(AsyncResults.succeeded(el));
        break;
      }
    }
  }
  
  @SuppressWarnings("unchecked")
  @Override
  public void fail(Throwable ex) {
    switch (state) {
      case emptyOrStarving: {
        Object cont = queue.pollFirst();
        if (cont != null) {
          ((IAsyncHandler<T>) cont).fail(ex);
        } else {
          queue.addLast(AsyncResults.failed(ex));
          state = State.emptyOrFatting;
        }
        break;
      }
      case emptyOrFatting: {
        queue.addLast(AsyncResults.failed(ex));
        break;
      }
    }
  }
  
  @SuppressWarnings("unchecked")
  @Override
  public void complete(IAsyncResult<T> ar) {
    switch (state) {
      case emptyOrStarving: {
        Object cont = queue.pollFirst();
        if (cont != null) {
          ((IAsyncHandler<T>) cont).complete(ar);
        } else {
          queue.addLast(ar);
          state = State.emptyOrFatting;
        }
        break;
      }
      case emptyOrFatting: {
        queue.addLast(ar);
        break;
      }
    }
  }
  
  @SuppressWarnings("unchecked")
  public IAsyncHandler<? extends T> tryEnq() {
    if (state == State.emptyOrStarving) {
      return (IAsyncHandler<? extends T>)queue.pollFirst();
    }
    return null;
  }
  
  @SuppressWarnings("unchecked")
  public <R> R tryEnq(Function<IAsyncHandler<? extends T>, R> block) {
    if (state == State.emptyOrStarving) {
      Object el = queue.pollFirst();
      if (el != null) {
        return block.apply((IAsyncHandler<T>)el);
      }
    }
    return null;
  }
  
  @SuppressWarnings("unchecked")
  public void enq(T el) {
    succeed(el);
  }
  
  
  @SuppressWarnings("unchecked")
  public void deq(IAsyncHandler<T> cont) {
    switch(state) {
      case emptyOrStarving: {
        queue.addLast(cont);
        break;
      }
      case emptyOrFatting: {
        IAsyncFuture<T> f = (IAsyncFuture<T>)queue.pollFirst();
        if (f != null) {
          f.onCompleted(cont);
        } else {
          queue.addLast(cont);
          state = State.emptyOrStarving;
        }
      }
    }
  }
  
  @SuppressWarnings("unchecked")
  public IAsyncFuture<T> deq() {
    if(state == State.emptyOrFatting){
      IAsyncFuture<T> f = (IAsyncFuture<T>)queue.pollFirst();
      if (f != null) {
        return f;
      }
    }
    AsyncFuture<T> future = new AsyncFuture<>();
    queue.addLast(future);
    return future;
  }
  
  
  @SuppressWarnings("unchecked")
  public IAsyncFuture<T> tryDeq(){
    if(state == State.emptyOrFatting){
      return (IAsyncFuture<T>)queue.pollFirst();
    }
    return null;
  }
  
  @SuppressWarnings("unchecked")
  public <R> R tryDeq(Function<IAsyncFuture<T>,R> block) {
    if(state == State.emptyOrFatting){
      IAsyncFuture<T> f = (IAsyncFuture<T>)queue.pollFirst();
      if(f != null){
        return block.apply(f);
      }
    }
    return null;
  }
}