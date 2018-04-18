package com.satori.libs.vertx.kotlin

import com.fasterxml.jackson.core.*
import com.fasterxml.jackson.core.type.*
import com.fasterxml.jackson.databind.*
import com.satori.libs.common.kotlin.json.*
import io.netty.buffer.*
import io.vertx.core.buffer.*

inline fun <reified T> IJsonContext.jsonParse(buf: Buffer): T {
  return mapper.readValue<T>(ByteBufInputStream(buf.getByteBuf()), object : TypeReference<T>() {})
}

fun IJsonContext.jsonParseAsTree(buf: Buffer): JsonNode {
  return mapper.readTree(ByteBufInputStream(buf.getByteBuf()))
}

fun IJsonContext.jsonTreeToBuffer(tree: JsonNode) = jsonTreeToBuffer { jgen ->
  mapper.writeTree(jgen, tree)
}

inline fun IJsonContext.jsonTreeToBuffer(writer: (JsonGenerator) -> Unit): Buffer {
  val buffer = Buffer.buffer()
  mapper.factory.createGenerator(VxBufferOutputStream(buffer)).use { jgen ->
    writer(jgen)
    jgen.flush()
  }
  return buffer
}