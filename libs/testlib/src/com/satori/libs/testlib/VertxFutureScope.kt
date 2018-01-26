package com.satori.libs.testlib

import com.satori.libs.async.api.*
import com.satori.libs.async.core.*
import io.vertx.core.*
import io.vertx.core.AsyncResult
import io.vertx.core.impl.*
import org.slf4j.*
import java.time.*
import java.util.concurrent.*
import kotlin.coroutines.experimental.intrinsics.*

/*@DslMarker
annotation class FutureVertxMarker

@FutureVertxMarker*/
open class VertxFutureScope(val vertx: Vertx) {
  val log = LoggerFactory.getLogger(javaClass)
  
  inline fun <R> thread(crossinline block: () -> R): IAsyncFuture<R> {
    val future = AsyncFuture<R>()
    Thread {
      try {
        val result = block()
        vertx.runOnContext { future.succeed(result) }
      } catch (err: Throwable) {
        vertx.runOnContext { future.fail(err) }
      }
    }.start()
    return future
  }
  
  inline fun <R> blocking(crossinline block: () -> R): IAsyncFuture<R> {
    val future = AsyncFuture<R>()
    val context = vertx.getOrCreateContext() as ContextImpl
    context.executeBlocking<R>(
      Action<R> { block() },
      Handler<AsyncResult<R>> { ar ->
        if (!ar.succeeded()) {
          future.fail(ar.cause())
        } else {
          future.succeed(ar.result())
        }
      }
    )
    return future
  }
  
  suspend fun delay(time: Long, unit: TimeUnit = TimeUnit.MILLISECONDS): Long {
    return timer(time, unit).await()
  }
  
  fun timer(duration: Duration) = timer(duration.toMillis())
  
  fun timer(time: Long, unit: TimeUnit = TimeUnit.MILLISECONDS): IAsyncFutureDisposable<Long> {
    if (time <= 0) {
      return object : AsyncSucceededResult<Long>(0), IAsyncFutureDisposable<Long> {
        override fun dispose() {
        }
      }
    }
    val started = System.currentTimeMillis()
    val future = object : AsyncFuture<Long>(), IAsyncFutureDisposable<Long> {
      var tid = Long.MIN_VALUE
      
      override fun dispose() {
        if (!tryFail(DisposedException)) {
          return
        }
        if (tid != INVALID_TID) {
          vertx.cancelTimer(tid)
          tid = INVALID_TID
        }
      }
      
      fun setTimerId(tid: Long) {
        if (isCompleted) {
          if (tid != Long.MIN_VALUE) {
            vertx.cancelTimer(tid)
          }
        } else {
          this.tid = tid
        }
      }
    }
    future.setTimerId(vertx.setTimer(unit.toMillis(time)) {
      if (!future.trySucceed(unit.convert(System.currentTimeMillis() - started, TimeUnit.MILLISECONDS))) {
        log.error("illegal state", IllegalStateException("failed to succeed timer future"))
      }
    })
    return future
  }
  
  suspend fun yield() {
    return suspendCoroutine { cont ->
      vertx.runOnContext {
        cont.succeed(Unit)
      }
    }
  }
  
  fun <T> future(block: suspend VertxFutureScope.() -> T) = future(
    this, block
  )
  
  suspend fun <T> IAsyncFuture<T>.await(time: Long, unit: TimeUnit = TimeUnit.MILLISECONDS): T {
    if (this.isCompleted) {
      return this.result.get()
    }
    return suspendCoroutineOrReturn { cont ->
      var completed = false
      var tid: Long
      tid = vertx.setTimer(unit.toMillis(time)) {
        tid = INVALID_TID
        if (completed) {
          return@setTimer
        }
        completed = true
        cont.resumeWithException(TimeoutException)
      }
      onCompleted { ar ->
        if (completed) {
          return@onCompleted
        }
        completed = true
        val t = tid
        if (t != INVALID_TID) {
          tid = INVALID_TID
          vertx.cancelTimer(t)
        }
        if (ar.isSucceeded) {
          cont.resume(ar.value)
        } else {
          cont.resumeWithException(ar.error)
        }
      }
      return@suspendCoroutineOrReturn COROUTINE_SUSPENDED
    }
  }
  
  suspend fun <T> IAsyncFuture<T>.await(): T {
    if (isCompleted) {
      return result.get()
    }
    return suspendCoroutineOrReturn { cont ->
      onCompleted { ar ->
        if (ar.isSucceeded) {
          cont.resume(ar.value)
        } else {
          cont.resumeWithException(ar.error)
        }
      }
      return@suspendCoroutineOrReturn COROUTINE_SUSPENDED
    }
  }
  
  suspend fun <T> IAsyncFuture<T>.awaitNoThrow(): IAsyncResult<out T> {
    if (isCompleted) {
      return result
    }
    return suspendCoroutineOrReturn { cont ->
      onCompleted { ar ->
        cont.resume(ar)
      }
      return@suspendCoroutineOrReturn COROUTINE_SUSPENDED
    }
  }
  
  suspend fun <T> suspendCoroutine(block: (cont: IAsyncPromise<T>) -> Unit): T {
    
    val future = AsyncFuture<T>()
    block(future)
    if (future.isCompleted) {
      return future.result.get()
    }
    return suspendCoroutineOrReturn { cont ->
      future.onCompleted { ar ->
        if (ar.isSucceeded) {
          cont.resume(ar.value)
        } else {
          cont.resumeWithException(ar.error)
        }
      }
      return@suspendCoroutineOrReturn COROUTINE_SUSPENDED
    }
  }
  
  companion object {
    const val INVALID_TID: Long = Long.MIN_VALUE
  }
}
