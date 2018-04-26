package com.satori.libs.async.kotlin

import com.satori.libs.async.api.*
import com.satori.libs.async.core.*
import org.slf4j.*
import kotlin.coroutines.experimental.*

private val log = LoggerFactory.getLogger("com.satori.libs.async.kotlin.FutureLib")

private fun AsyncFuture<*>.printState(): String{
  if(!isCompleted){
    return "in progress"
  }
  val ar = result!!
  if(!ar.isSucceeded){
    return "failed with '${ar.error.message}'"
  }
  return "succeeded with '${ar.value}'"
}

fun <T> future(block: suspend () -> T): IAsyncFuture<T> {
  val future = object : AsyncFuture<T>(), Continuation<T> {
    override val context get() = EmptyCoroutineContext
    
    override fun resume(value: T) {
      if(!trySucceed(value)){
        log.error("can't succeed future, since it is already completed ('${printState()}')", IllegalStateException("future has been already completed"))
      }
    }
    
    override fun resumeWithException(exception: Throwable) {
      if(!tryFail(exception)){
        log.error("can't fail future, since it is already completed ('${printState()}')", IllegalStateException("future has been already completed"))
      }
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

