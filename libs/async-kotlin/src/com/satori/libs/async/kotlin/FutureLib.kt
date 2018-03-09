package com.satori.libs.async.kotlin

import com.satori.libs.async.api.*
import com.satori.libs.async.core.*
import kotlin.coroutines.experimental.*

fun <T> future(block: suspend () -> T): IAsyncFuture<T> {
  val future = object : AsyncFuture<T>(), Continuation<T> {
    override val context get() = EmptyCoroutineContext
    
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
    override val context get() = EmptyCoroutineContext
    
    override fun resume(value: T) {
      if(!trySucceed(value)){
        throw IllegalStateException("can't resume future due it was already completed")
      }
    }
    
    override fun resumeWithException(exception: Throwable) {
      if(!tryFail(exception)){
        throw IllegalStateException("can't resume future due it was already completed")
      }
    }
  }
  block.startCoroutine(scope, future)
  return future;
}

