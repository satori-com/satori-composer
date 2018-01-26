package com.satori.libs.testlib

import com.satori.libs.async.api.*
import com.satori.libs.async.core.*
import kotlin.coroutines.experimental.*

fun <T> future(block: suspend () -> T): IAsyncFuture<T> {
  val future = object : AsyncFuture<T>(), Continuation<T> {
    override val context: CoroutineContext
      get() = EmptyCoroutineContext
    
    override fun resume(value: T) {
      succeed(value)
    }
    
    override fun resumeWithException(exception: Throwable) {
      fail(exception)
    }
  }
  block.startCoroutine(future)
  return future;
}

fun <R, T> future(scope: R, block: suspend R.() -> T): IAsyncFuture<T> {
  val future = object : AsyncFuture<T>(), Continuation<T> {
    override val context: CoroutineContext
      get() = EmptyCoroutineContext
    
    override fun resume(value: T) {
      succeed(value)
    }
    
    override fun resumeWithException(exception: Throwable) {
      fail(exception)
    }
  }
  block.startCoroutine(scope, future)
  return future;
}

