package com.satori.libs.common.kotlin.json

import com.fasterxml.jackson.core.*

class JsonObjectWriterScope(private val jgen: JsonGenerator) : JsonWriterScope() {
  
  fun field(name: String, value: String) {
    jgen.writeStringField(name, value)
  }
  
  fun field(name: String, value: Int) {
    jgen.writeNumberField(name, value)
  }
  
  fun field(name: String, value: Long) {
    jgen.writeNumberField(name, value)
  }
  
  fun field(name: String, value: Float) {
    jgen.writeNumberField(name, value)
  }
  
  fun field(name: String, value: Double) {
    jgen.writeNumberField(name, value)
  }
  
  fun field(name: String, value: Boolean) {
    jgen.writeBooleanField(name, value)
  }
  
  fun field(name: String, writer: (JsonGenerator) -> Unit) {
    jgen.writeFieldName(name)
    writer(jgen)
  }
}
