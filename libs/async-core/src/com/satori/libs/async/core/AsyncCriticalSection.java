package com.satori.libs.async.core;

import com.satori.libs.async.api.*;

import java.util.*;
import java.util.function.*;

public class AsyncCriticalSection {
  
  private ArrayDeque<IAsyncHandler> awaiters = new ArrayDeque<>();
  private boolean entered = false;
  private boolean inloop = false;
  private final Consumer<Throwable> errorHandler;
  
  public AsyncCriticalSection() {
    this(null);
  }
  
  public AsyncCriticalSection(Consumer<Throwable> errorHandler) {
    this.errorHandler = errorHandler;
  }
  
  public void enter(IAsyncHandler cont) {
    if (entered) {
      awaiters.addLast(cont);
      return;
    }
    entered = true;
    try {
      cont.succeed();
    } catch (Throwable ex) {
      if (errorHandler != null) {
        errorHandler.accept(ex);
      }
    }
  }
  
  public IAsyncFuture enter() {
    if (entered) {
      AsyncFuture f = new AsyncFuture<>();
      awaiters.addLast(f);
      return f;
    }
    entered = true;
    return AsyncResults.succeeded();
  }
  
  public boolean tryEnter() {
    if (entered) {
      return false;
    }
    entered = true;
    return true;
  }
  
  public void leave() {
    if (!entered) {
      throw new IllegalStateException("trying to leave unentered critical section");
    }
    entered = false;
    if (inloop) {
      return;
    }
    inloop = true;
    do {
      IAsyncHandler cont = awaiters.pollFirst();
      if (cont == null) break;
      
      entered = true;
      try {
        cont.succeed();
      } catch (Throwable ex) {
        if (errorHandler != null) {
          errorHandler.accept(ex);
        }
      }
    } while (!entered);
    
    inloop = false;
  }
  
  public void exec(IAsyncHandler cont) {
    if (entered) {
      awaiters.addLast(ar -> {
        try {
          if (ar.isSucceeded()) cont.succeed();
          else cont.fail(ar.getError());
        } finally {
          leave();
        }
      });
      return;
    }
    entered = true;
    try {
      cont.succeed();
    } finally {
      leave();
    }
  }
}
