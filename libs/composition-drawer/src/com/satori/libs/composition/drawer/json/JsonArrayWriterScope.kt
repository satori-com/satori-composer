package com.satori.libs.composition.drawer.json

import com.fasterxml.jackson.core.*

class JsonArrayWriterScope(private val jgen: JsonGenerator) : JsonWriterScope() {
  
  fun item(value: String) {
    jgen.writeString(value)
  }
  
  fun item(value: Int) {
    jgen.writeNumber(value)
  }
  
  fun item(value: Long) {
    jgen.writeNumber(value)
  }
  
  fun item(value: Float) {
    jgen.writeNumber(value)
  }
  
  fun item(value: Double) {
    jgen.writeNumber(value)
  }
  
  fun item(value: Boolean) {
    jgen.writeBoolean(value)
  }
  
  fun item(builder: JsonObjectWriterScope.() -> Unit) {
    jgen.writeStartObject()
    builder(JsonObjectWriterScope(jgen))
    jgen.writeEndObject()
  }
}
