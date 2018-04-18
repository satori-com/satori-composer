package com.satori.libs.common.kotlin.json

import com.fasterxml.jackson.core.*
import com.fasterxml.jackson.core.type.*
import com.fasterxml.jackson.databind.*
import com.fasterxml.jackson.databind.node.*
import com.satori.libs.common.kotlin.*
import java.io.*
import java.nio.*

var jsonNodeFactory = JsonNodeFactory.instance

fun jsonNull() = NullNode.instance
fun jsonNode(value: String) = TextNode.valueOf(value)
fun jsonNode(value: Int) = IntNode.valueOf(value)
fun jsonNode(value: Long) = LongNode.valueOf(value)
fun jsonNode(value: Double) = DoubleNode.valueOf(value)
fun jsonNode(value: Float) = FloatNode.valueOf(value)
fun jsonNode(value: Boolean) = BooleanNode.valueOf(value)
fun jsonArray() = jsonNodeFactory.arrayNode()
fun jsonObject() = jsonNodeFactory.objectNode()

fun jsonArray(vararg nodes: JsonNode) = nodes.fold(jsonNodeFactory.arrayNode(nodes.size)) { acc, el ->
  acc.add(el)
  return@fold acc
}
inline fun jsonArray(builder: JsonArrayBuilderScope.() -> Unit) = jsonArray().also { n ->
  JsonArrayBuilderScope(n).builder()
}

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
