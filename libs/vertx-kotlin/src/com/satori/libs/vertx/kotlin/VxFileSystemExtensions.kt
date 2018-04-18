package com.satori.libs.vertx.kotlin

import com.satori.libs.async.api.*
import com.satori.libs.async.core.*
import io.vertx.core.buffer.*

fun VxFileSystem.writeFile(path: String, buffer: Buffer): IAsyncFuture<Unit> {
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

fun VxFileSystem.writeFile(path: String, content: String) = writeFile(
  path, Buffer.buffer(content)
)

fun VxFileSystem.readFile(path: String): IAsyncFuture<Buffer> {
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

fun VxFileSystem.exists(path: String): IAsyncFuture<Boolean> {
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

fun VxFileSystem.file(path: String, block: VxFileScope.() -> Unit) {
  VxFileScope(path, this).block()
}