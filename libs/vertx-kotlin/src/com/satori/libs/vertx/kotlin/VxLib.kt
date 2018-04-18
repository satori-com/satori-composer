package com.satori.libs.vertx.kotlin

import com.satori.libs.async.api.*
import com.satori.libs.async.core.*
import com.satori.libs.async.kotlin.*
import io.vertx.core.*
import io.vertx.core.buffer.*
import org.slf4j.*

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