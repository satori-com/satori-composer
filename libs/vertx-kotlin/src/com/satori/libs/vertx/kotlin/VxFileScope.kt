package com.satori.libs.vertx.kotlin

import com.fasterxml.jackson.core.*
import com.fasterxml.jackson.databind.*
import com.satori.libs.async.api.*
import com.satori.libs.async.core.*
import com.satori.libs.async.kotlin.*
import com.satori.libs.common.kotlin.json.*
import io.vertx.core.buffer.*

class VxFileScope(val filePath: String, val fileSystem: VxFileSystem) {
  
  fun exists(): IAsyncFuture<Boolean> {
    val f = AsyncFuture<Boolean>()
    fileSystem.exists(filePath) { ar ->
      if (!ar.succeeded()) {
        f.fail(ar.cause())
      } else {
        f.succeed(ar.result())
      }
    }
    return f
  }
  
  fun write(data: Buffer, handler: (VxAsyncResult<Void>) -> Unit) = fileSystem.writeFile(
    filePath, data, handler
  )
  
  fun write(buffer: Buffer): IAsyncFuture<Unit> {
    val f = AsyncFuture<Unit>()
    write(buffer) { ar ->
      if (!ar.succeeded()) {
        f.fail(ar.cause())
      } else {
        f.succeed()
      }
    }
    return f
  }
  
  fun read(handler: (VxAsyncResult<Buffer>) -> Unit) = fileSystem.readFile(
    filePath, handler
  )

  fun read(): IAsyncFuture<Buffer> {
    val f = AsyncFuture<Buffer>()
    read { ar ->
      if (!ar.succeeded()) {
        f.fail(ar.cause())
      } else {
        f.succeed(ar.result())
      }
    }
    return f
  }
  
  fun IJsonContext.write(json: JsonNode) = write(
    jsonTreeToBuffer(json)
  )
  
  inline fun IJsonContext.write(writer: (JsonGenerator) -> Unit) = write(
    jsonTreeToBuffer(writer)
  )
  
  fun IJsonContext.readAsJsonTree(): IAsyncFuture<JsonNode?> = future {
    if (!exists().await()) {
      return@future null
    }
    return@future jsonParseAsTree(
      read().await()
    )
  }
  
}