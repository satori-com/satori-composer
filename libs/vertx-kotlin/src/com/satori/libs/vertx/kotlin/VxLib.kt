package com.satori.libs.vertx.kotlin

import com.satori.libs.async.api.*
import com.satori.libs.async.core.*
import com.satori.libs.async.kotlin.*
import io.vertx.core.*
import org.slf4j.*
import kotlin.coroutines.experimental.*

fun VxBuffer.inputStream() = VxBufferInputStream(this)
fun VxBuffer.outputStream() = VxBufferOutputStream(this)

fun vertx(block: Vertx.() -> Unit): IAsyncFuture<Unit> {
  val future = AsyncFuture<Unit>()
  
  val vxOpts = VertxOptions().apply {
    eventLoopPoolSize = 1
  }
  
  val vertx = Vertx.vertx(vxOpts)
  val log = LoggerFactory.getLogger("vertx")
  
  vertx.exceptionHandler { exception ->
    log.error("unhandled exception in vertx loop, closing app", exception)
    vertx.close {
      future.fail(exception)
    }
  }
  
  vertx.runOnContext {
    vertx.block()
  }
  
  return future
}

fun <T> Vertx.future(block: suspend VxFutureScope.() -> T): IAsyncFuture<T> = future(
  VxFutureScope(this), block
)

fun <T> Vertx.future(log: Logger, block: suspend VxFutureScope.() -> T): IAsyncFuture<T> = future(
  VxFutureScope(this, log), block
)

inline fun <reified T> vxFuture(crossinline block: (VxAsyncHandler<T>) -> Unit): IAsyncFuture<T> {
  val future = AsyncFuture<T>()
  block(VxAsyncHandler { ar ->
    if (!ar.succeeded()) {
      future.fail(ar.cause())
    } else {
      future.succeed(ar.result())
    }
  })
  return future
}

suspend inline fun <reified T> vxAwait(crossinline block: (VxAsyncHandler<T>) -> Unit): T {
  return suspendCoroutine { cont ->
    block(VxAsyncHandler { ar ->
      if (!ar.succeeded()) {
        cont.resumeWithException(ar.cause())
      } else {
        cont.resume(ar.result())
      }
    })
  }
}