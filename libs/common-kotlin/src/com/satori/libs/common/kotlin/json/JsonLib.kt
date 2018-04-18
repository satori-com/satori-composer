package com.satori.libs.common.kotlin.json

import com.fasterxml.jackson.core.*
import com.fasterxml.jackson.core.type.*
import com.fasterxml.jackson.databind.*
import com.satori.libs.common.kotlin.*
import java.io.*
import java.nio.*

fun IJsonContext.jsonParseAsTree(string: String): JsonNode {
  return mapper.readTree(string)
}

fun IJsonContext.jsonParseAsTree(inputStream: InputStream): JsonNode {
  return mapper.readTree(inputStream)
}

fun IJsonContext.jsonParseAsTree(byteBuffer: ByteBuffer?): JsonNode? {
  if (byteBuffer === null || byteBuffer.remaining() <= 0) {
    return null
  }
  return ByteBufferInputStream(byteBuffer).use {
    jsonParseAsTree(it)
  }
}

inline fun <reified T> IJsonContext.jsonParse(string: String): T {
  return mapper.readValue(
    string, object : TypeReference<T>() {}
  )
}

inline fun <reified T> IJsonContext.jsonParse(inputStream: InputStream): T {
  return mapper.readValue(
    inputStream, object : TypeReference<T>() {}
  )
}

inline fun <reified T> IJsonContext.jsonParse(jsonParser: JsonParser): T {
  return mapper.readValue(
    jsonParser, object : TypeReference<T>() {}
  )
}

inline fun <reified T> IJsonContext.jsonParse(byteBuffer: ByteBuffer?): T? {
  if (byteBuffer === null || byteBuffer.remaining() <= 0) {
    return null
  }
  
  return ByteBufferInputStream(byteBuffer).use {
    jsonParse<T>(it)
  }
}

inline fun <reified T : JsonNode> IJsonContext.jsonValueToTree(value: Any): T {
  return mapper.valueToTree(value)
}

inline fun <reified T> IJsonContext.jsonTreeToValue(value: JsonNode): T {
  return mapper.readValue(mapper.treeAsTokens(value), object : TypeReference<T>() {})
  //return mapper.treeToValue(value, T::class.java)
}

fun IJsonContext.jsonWriterToString(writer: (JsonGenerator) -> Unit): String {
  StringWriter().use { w ->
    mapper.factory.createGenerator(w).use { jgen ->
      writer(jgen)
      jgen.flush()
      return w.toString()
    }
  }
}

fun IJsonContext.jsonWriterToStringPretty(writer: (JsonGenerator) -> Unit): String {
  StringWriter().use { w ->
    mapper.factory.createGenerator(w).use { jgen ->
      jgen.useDefaultPrettyPrinter()
      writer(jgen)
      jgen.flush()
      return w.toString()
    }
  }
}

inline fun IJsonContext.jsonObjectToString(crossinline builder: JsonObjectWriterScope.() -> Unit) = jsonWriterToString(
  jsonObjectWriter(builder)
)

inline fun IJsonContext.jsonObjectToPrettyString(crossinline builder: JsonObjectWriterScope.() -> Unit) = jsonWriterToStringPretty(
  jsonObjectWriter(builder)
)

inline fun IJsonContext.jsonArrayToString(crossinline builder: JsonArrayWriterScope.() -> Unit) = jsonWriterToString(
  jsonArrayWriter(builder)
)

inline fun IJsonContext.jsonArrayToPrettyString(crossinline builder: JsonArrayWriterScope.() -> Unit) = jsonWriterToStringPretty(
  jsonArrayWriter(builder)
)

fun IJsonContext.jsonPrettyString(value: JsonNode) = jsonWriterToString { jgen ->
  jgen.useDefaultPrettyPrinter()
  mapper.writeTree(jgen, value)
}

fun IJsonContext.jsonPrettyString(value: Any) = StringWriter().use { w ->
  mapper.writerWithDefaultPrettyPrinter().writeValue(w, value)
  w.toString()
}

fun IJsonContext.writeJson(outputStream: OutputStream, jsonNode: JsonNode) {
  mapper.factory.createGenerator(outputStream).use { jgen ->
    jgen.writeTree(jsonNode)
    jgen.flush()
  }
}

fun IJsonContext.writeJsonPretty(outputStream: OutputStream, jsonNode: JsonNode) {
  mapper.factory.createGenerator(outputStream).use { jgen ->
    jgen.useDefaultPrettyPrinter()
    jgen.writeTree(jsonNode)
    jgen.flush()
  }
}

inline fun IJsonContext.writeJson(outputStream: OutputStream, writer: (JsonGenerator) -> Unit) {
  mapper.factory.createGenerator(outputStream).use { jgen ->
    writer(jgen)
    jgen.flush()
  }
}

inline fun IJsonContext.writeJsonPretty(outputStream: OutputStream, writer: (JsonGenerator) -> Unit) {
  mapper.factory.createGenerator(outputStream).use { jgen ->
    jgen.useDefaultPrettyPrinter()
    writer(jgen)
    jgen.flush()
  }
}

inline fun IJsonContext.writeJson(byteBuffer: ByteBuffer, writer: (JsonGenerator) -> Unit) {
  byteBuffer.putWithStream { outputStream ->
    mapper.factory.createGenerator(outputStream).use { jgen ->
      writer(jgen)
      jgen.flush()
    }
  }
}

inline fun IJsonContext.writeJsonPretty(byteBuffer: ByteBuffer, writer: (JsonGenerator) -> Unit) {
  byteBuffer.putWithStream { outputStream ->
    mapper.factory.createGenerator(outputStream).use { jgen ->
      jgen.useDefaultPrettyPrinter()
      writer(jgen)
      jgen.flush()
    }
  }
}

inline fun IJsonContext.writeJsonObject(byteBuffer: ByteBuffer, crossinline builder: JsonObjectWriterScope.() -> Unit) = writeJson(
  byteBuffer, jsonObjectWriter(builder)
)

inline fun IJsonContext.writeJsonObjectPretty(byteBuffer: ByteBuffer, crossinline builder: JsonObjectWriterScope.() -> Unit) = writeJsonPretty(
  byteBuffer, jsonObjectWriter(builder)
)

inline fun IJsonContext.writeJsonArray(byteBuffer: ByteBuffer, pretty: Boolean = false, crossinline builder: JsonArrayWriterScope.() -> Unit) = writeJson(
  byteBuffer, jsonArrayWriter(builder)
)

inline fun IJsonContext.writeJsonArrayPretty(byteBuffer: ByteBuffer, crossinline builder: JsonArrayWriterScope.() -> Unit) = writeJsonPretty(
  byteBuffer, jsonArrayWriter(builder)
)

class JsonNodeScope(val jsonNode: JsonNode) {
  inline fun <reified T> IJsonContext.toValue() = jsonTreeToValue<T>(jsonNode)
}

inline fun <reified T> JsonNode.scope(block: JsonNodeScope.() -> T) = JsonNodeScope(this).block()