package com.satori.libs.testlib

import com.satori.libs.async.api.*
import com.satori.libs.async.core.*
import org.slf4j.*
import kotlin.coroutines.experimental.*
import kotlin.coroutines.experimental.intrinsics.*

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

fun <T> IAsyncHandler<T>.fail(message: String) {
  fail(Exception(message))
}

fun <T, R> IAsyncFuture<T>.map(block: (T) -> R): IAsyncFuture<R> {
  val future = AsyncFuture<R>()
  onCompleted { ar ->
    if (!ar.isSucceeded) {
      future.fail(ar.error)
      return@onCompleted
    }
    val mappedResult: R
    try {
      mappedResult = block(ar.value)
    } catch (ex: Throwable) {
      future.fail(ex)
      return@onCompleted
    }
    future.succeed(mappedResult)
  }
  return future
}

inline fun AsyncCriticalSection.exec(crossinline block: () -> Unit) = exec(
  IAsyncHandler { block() }
)

inline fun <R> AsyncCriticalSection.use(crossinline block: () -> IAsyncFuture<R>): IAsyncFuture<R> = future {
  enter().await()
  try {
    return@future block().await()
  } finally {
    leave()
  }
}

suspend fun <T> AsyncCriticalSection.run(block: suspend () -> T): T {
  enter().await()
  try {
    return block()
  } finally {
    leave()
  }
}

suspend fun <S, T> AsyncCriticalSection.run(scope: S, block: suspend S.() -> T): T {
  enter().await()
  try {
    return scope.block()
  } finally {
    leave()
  }
}

fun AsyncForkJoin.fork(): IAsyncHandler<Any?> {
  inc()
  val afj = this
  return AsyncFuture<Any?>().apply {
    onCompleted { ar ->
      if (!ar.isSucceeded) {
        LoggerFactory.getLogger("async-fork-join").warn("fork failed", ar.error) // TODO: fix it
      }
      afj.complete(ar)
    }
  }
}

fun AsyncForkJoin.fork(block: suspend () -> Unit){
  val f = fork()
  future(block).onCompleted(f)
}

fun AsyncForkJoin.join(): IAsyncFuture<Any?> {
  dec()
  return this
}

inline fun <reified T> asyncResult(block: () -> T): IAsyncFuture<T> {
  return AsyncResults.succeeded(try {
    block()
  } catch (e: Throwable) {
    return AsyncResults.failed(e)
  })
}

inline fun<reified T> sync(noinline future: suspend () -> T): T {
  
  val cont = object : Continuation<T> {
    val sync = Object()
    var completed = false
    var result: IAsyncResult<T>? = null
    
    override val context: CoroutineContext
      get() = EmptyCoroutineContext
    
    override fun resume(value: T) {
      synchronized(sync) {
        result = AsyncResults.succeeded(value)
        completed = true
        sync.notifyAll()
      }
    }
    
    override fun resumeWithException(exception: Throwable) {
      synchronized(sync) {
        result = AsyncResults.failed(exception)
        completed = true
        sync.notifyAll()
      }
    }
    
    fun block() {
      synchronized(sync) {
        if (!completed) {
          sync.wait()
        }
      }
      result!!.get()
    }
  }
  
  future.startCoroutine(cont)
  cont.block()
  return cont.result!!.get()
}

val<T> IAsyncFuture<T>.value:T? get() = result?.value