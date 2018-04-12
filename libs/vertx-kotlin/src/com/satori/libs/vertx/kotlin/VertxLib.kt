package com.satori.libs.vertx.kotlin

import com.fasterxml.jackson.core.*
import com.fasterxml.jackson.core.type.*
import com.fasterxml.jackson.databind.*
import com.satori.libs.async.api.*
import com.satori.libs.async.core.*
import com.satori.libs.async.kotlin.*
import com.satori.libs.common.kotlin.json.*
import io.netty.buffer.*
import io.vertx.core.*
import io.vertx.core.buffer.*
import io.vertx.core.file.*
import org.slf4j.*

fun vertx(block: Vertx.() -> Unit): IAsyncFuture<Unit> {
  val future = AsyncFuture<Unit>()
  
  val vertxOpts = VertxOptions().apply {
    eventLoopPoolSize = 1
  }
  
  val vertx = Vertx.vertx(vertxOpts)
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

fun <T> Vertx.future(block: suspend VertxFutureScope.() -> T): IAsyncFuture<T> = future(
  VertxFutureScope(this), block
)

fun <T> Vertx.future(log: Logger, block: suspend VertxFutureScope.() -> T): IAsyncFuture<T> = future(
  VertxFutureScope(this, log), block
)

inline fun <reified T> jsonParse(buf: Buffer): T {
  return mapper.readValue<T>(ByteBufInputStream(buf.getByteBuf()), object : TypeReference<T>() {})
}

fun jsonParseAsTree(buf: Buffer): JsonNode {
  return mapper.readTree(ByteBufInputStream(buf.getByteBuf()))
}

fun jsonTreeToBuffer(tree: JsonNode) = jsonTreeToBuffer { jgen ->
  mapper.writeTree(jgen, tree)
}

inline fun jsonTreeToBuffer(writer: (JsonGenerator) -> Unit): Buffer {
  val buffer = Buffer.buffer()
  mapper.factory.createGenerator(BufferOutputStream(buffer)).use { jgen ->
    writer(jgen)
    jgen.flush()
  }
  return buffer
}

fun FileSystem.writeFile(path: String, buffer: Buffer): IAsyncFuture<Unit> {
  val f = AsyncFuture<Unit>()
  writeFile(path, buffer) { ar ->
    if (!ar.succeeded()) {
      f.fail(ar.cause())
    } else {
      f.succeed()
    }
  }
  return f
}

fun FileSystem.writeFile(path: String, content: String) = writeFile(
  path, Buffer.buffer(content)
)

fun FileSystem.writeFile(path: String, content: JsonNode) = writeFile(
  path, jsonTreeToBuffer(content)
)

inline fun FileSystem.writeFile(path: String, writer: (JsonGenerator) -> Unit) = writeFile(
  path, jsonTreeToBuffer(writer)
)

fun FileSystem.readFile(path: String): IAsyncFuture<Buffer> {
  val f = AsyncFuture<Buffer>()
  readFile(path) { ar ->
    if (!ar.succeeded()) {
      f.fail(ar.cause())
    } else {
      f.succeed(ar.result())
    }
  }
  return f
}

fun FileSystem.exists(path: String): IAsyncFuture<Boolean> {
  val f = AsyncFuture<Boolean>()
  exists(path) { ar ->
    if (!ar.succeeded()) {
      f.fail(ar.cause())
    } else {
      f.succeed(ar.result())
    }
  }
  return f
}

fun FileSystem.readFileAsJsonTree(path: String): IAsyncFuture<JsonNode?> = future {
  if (!exists(path).await()) {
    return@future null
  }
  return@future jsonParseAsTree(
    readFile(path).await()
  )
}
