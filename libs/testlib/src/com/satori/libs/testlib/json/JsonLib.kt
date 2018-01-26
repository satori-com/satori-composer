package com.satori.libs.testlib.json

import  com.satori.libs.testlib.*
import com.fasterxml.jackson.core.*
import com.fasterxml.jackson.core.type.*
import com.fasterxml.jackson.databind.*
import com.fasterxml.jackson.databind.node.*
import com.fasterxml.jackson.module.afterburner.*
import java.io.*
import java.nio.*

val mapper = ObjectMapper()
  .configure(JsonParser.Feature.ALLOW_COMMENTS, true)
  .configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true)
  .configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true)
  .configure(JsonParser.Feature.ALLOW_TRAILING_COMMA, true)
  .disable(JsonGenerator.Feature.AUTO_CLOSE_TARGET)
  .enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
  .registerModule(AfterburnerModule());

fun jsonParseAsTree(string: String): JsonNode {
  return mapper.readTree(string)
}

fun jsonParseAsTree(inputStream: InputStream): JsonNode {
  return mapper.readTree(inputStream)
}

fun jsonParseAsTree(byteBuffer: ByteBuffer?): JsonNode? {
  if (byteBuffer === null || byteBuffer.remaining() <= 0) {
    return null
  }
  return ByteBufferInputStream(byteBuffer).use {
    jsonParseAsTree(it)
  }
}

inline fun <reified T> jsonParse(string: String): T {
  return mapper.readValue(
    string, object : TypeReference<T>() {}
  )
}

inline fun <reified T> jsonParse(inputStream: InputStream): T {
  return mapper.readValue(
    inputStream, object : TypeReference<T>() {}
  )
}

inline fun <reified T> jsonParse(jsonParser: JsonParser): T {
  return mapper.readValue(
    jsonParser, object : TypeReference<T>() {}
  )
}

inline fun <reified T> jsonParse(byteBuffer: ByteBuffer?): T? {
  if (byteBuffer === null || byteBuffer.remaining() <= 0) {
    return null
  }
  return ByteBufferInputStream(byteBuffer).use {
    jsonParse<T>(it)
  }
}

inline fun <reified T : JsonNode> jsonValueToTree(value: Any): T {
  return mapper.valueToTree(value)
}

inline fun <reified T> jsonTreeToValue(value: JsonNode): T {
  return mapper.treeToValue(value, T::class.java)
}

fun jsonNull() = NullNode.instance
fun jsonNode(value: String) = TextNode.valueOf(value)
fun jsonNode(value: Int) = IntNode.valueOf(value)
fun jsonNode(value: Long) = LongNode.valueOf(value)
fun jsonNode(value: Double) = DoubleNode.valueOf(value)
fun jsonNode(value: Float) = FloatNode.valueOf(value)
fun jsonNode(value: Boolean) = BooleanNode.valueOf(value)

fun jsonArray(vararg nodes: JsonNode) = nodes.fold(JsonNodeFactory.instance.arrayNode(nodes.size)) { acc, el ->
  acc.add(el)
  return@fold acc
}

fun jsonArray() = JsonNodeFactory.instance.arrayNode()
inline fun jsonArray(builder: JsonArrayBuilderScope.() -> Unit) = jsonArray().also { n ->
  JsonArrayBuilderScope(n).builder()
}

fun jsonObject() = JsonNodeFactory.instance.objectNode()
inline fun jsonObject(builder: JsonObjectBuilderScope.() -> Unit) = jsonObject().also { n ->
  JsonObjectBuilderScope(n).builder()
}

fun jsonRef(value: String?) = jsonObject {
  if (value != null) {
    field("\$ref", value)
  } else {
    field("\$ref", jsonNull())
  }
}

fun jsonWriterToString(pretty: Boolean = false, writer: (JsonGenerator) -> Unit): String {
  StringWriter().use { w ->
    mapper.factory.createGenerator(w).use { jgen ->
      if (pretty) {
        jgen.useDefaultPrettyPrinter()
      }
      writer(jgen)
      jgen.flush()
      return w.toString()
    }
  }
}

inline fun jsonObjectToString(pretty: Boolean = false, crossinline builder: JsonObjectWriterScope.() -> Unit) = jsonWriterToString(
  pretty, jsonObjectWriter(builder)
)

inline fun jsonArrayToString(pretty: Boolean = false, crossinline builder: JsonArrayWriterScope.() -> Unit) = jsonWriterToString(
  pretty, jsonArrayWriter(builder)
)

fun jsonPrettyString(value: JsonNode) = jsonWriterToString(true) { jgen ->
  mapper.writeTree(jgen, value)
}

fun jsonPrettyString(value: Any) = StringWriter().use { w ->
  mapper.writerWithDefaultPrettyPrinter().writeValue(w, value)
  w.toString()
}

inline fun jsonObjectWriter(crossinline builder: JsonObjectWriterScope.() -> Unit): (JsonGenerator) -> Unit {
  return { jgen ->
    jgen.writeStartObject()
    try {
      builder(JsonObjectWriterScope(jgen))
    } finally {
      jgen.writeEndObject()
    }
  }
}

inline fun jsonArrayWriter(crossinline builder: JsonArrayWriterScope.() -> Unit): (JsonGenerator) -> Unit {
  return { jgen ->
    jgen.writeStartArray()
    try {
      builder(JsonArrayWriterScope(jgen))
    } finally {
      jgen.writeEndArray()
    }
  }
}

fun OutputStream.writeJson(pretty: Boolean = false, jsonNode: JsonNode) {
  mapper.factory.createGenerator(this).use { jgen ->
    if (pretty) {
      jgen.useDefaultPrettyPrinter()
    }
    jgen.writeTree(jsonNode)
    jgen.flush()
  }
}

inline fun OutputStream.writeJson(pretty: Boolean = false, writer: (JsonGenerator) -> Unit) {
  mapper.factory.createGenerator(this).use { jgen ->
    if (pretty) {
      jgen.useDefaultPrettyPrinter()
    }
    writer(jgen)
    jgen.flush()
  }
}

inline fun ByteBuffer.writeJson(pretty: Boolean = false, writer: (JsonGenerator) -> Unit) {
  ByteBufferOutputStream(this).use { outputStream ->
    mapper.factory.createGenerator(outputStream).use { jgen ->
      if (pretty) {
        jgen.useDefaultPrettyPrinter()
      }
      writer(jgen)
      jgen.flush()
    }
  }
}

fun ByteBuffer.putJson(jsonNode: JsonNode, pretty: Boolean = false) {
  ByteBufferOutputStream(this).use { outputStream ->
    mapper.factory.createGenerator(outputStream).use { jgen ->
      if (pretty) {
        jgen.useDefaultPrettyPrinter()
      }
      mapper.writeTree(jgen, jsonNode)
      jgen.flush()
    }
  }
}

inline fun ByteBuffer.writeJsonObject(pretty: Boolean = false, crossinline builder: JsonObjectWriterScope.() -> Unit) = writeJson(
  pretty, jsonObjectWriter(builder)
)

inline fun ByteBuffer.writeJsonArray(pretty: Boolean = false, crossinline builder: JsonArrayWriterScope.() -> Unit) = writeJson(
  pretty, jsonArrayWriter(builder)
)

fun JsonNode?.merge(changeSet: JsonNode?): JsonNode? = let {
  if (it === null) {
    return changeSet // TODO: remove explicit nulls ?
  }
  if (changeSet == null) {
    return@let it
  }
  if (changeSet.isNull) {
    return@let null // TODO: jsonNull() ?
  }
  if (changeSet !is ObjectNode || it !is ObjectNode) {
    return changeSet
  }
  for ((k, v) in changeSet.fields()) {
    if (v === null || v.isNull) {
      it.remove(k)
      continue
    }
    val merged = it[k].merge(v)
    if (merged === null || merged.isNull) {
      it.remove(k)
      continue
    }
    it[k] = merged
  }
  return it
}

inline fun <reified T> JsonNode.toValue(): T {
  return jsonTreeToValue(this)
}
