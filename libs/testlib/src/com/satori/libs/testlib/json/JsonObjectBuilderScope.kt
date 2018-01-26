package com.satori.libs.testlib.json

import com.fasterxml.jackson.databind.*
import com.fasterxml.jackson.databind.node.*

class JsonObjectBuilderScope(val objectNode: ObjectNode) : JsonBuilderScope() {

  fun field(name: String, value: String) = field(name, jsonNode(value))
  fun field(name: String, value: Int) = field(name, jsonNode(value))
  fun field(name: String, value: Long) = field(name, jsonNode(value))
  fun field(name: String, value: Float) = field(name, jsonNode(value))
  fun field(name: String, value: Double) = field(name, jsonNode(value))
  fun field(name: String, value: Boolean) = field(name, jsonNode(value))

  fun field(name: String, value: JsonNode): JsonNode? {
    return objectNode.replace(name, value)
  }
}
