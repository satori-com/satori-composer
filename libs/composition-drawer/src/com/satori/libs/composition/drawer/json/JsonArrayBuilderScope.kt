package com.satori.libs.composition.drawer.json

import com.fasterxml.jackson.databind.*
import com.fasterxml.jackson.databind.node.*

class JsonArrayBuilderScope(val arrayNode: ArrayNode) : JsonBuilderScope() {
  
  fun item(value: String) = item(jsonNode(value))
  fun item(value: Int) = item(jsonNode(value))
  fun item(value: Long) = item(jsonNode(value))
  fun item(value: Float) = item(jsonNode(value))
  fun item(value: Double) = item(jsonNode(value))
  fun item(value: Boolean) = item(jsonNode(value))
  
  fun item(value: JsonNode) {
    arrayNode.add(value)
  }
}
