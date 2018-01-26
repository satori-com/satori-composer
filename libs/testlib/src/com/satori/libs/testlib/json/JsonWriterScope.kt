package com.satori.libs.testlib.json

@JsonWriterScope.Marker
open class JsonWriterScope {
  @DslMarker
  @Target(AnnotationTarget.CLASS, AnnotationTarget.TYPE)
  annotation class Marker

  inline fun jsonObject(crossinline builder: JsonObjectWriterScope.() -> Unit) = jsonObjectWriter(builder)
  inline fun jsonArray(crossinline builder: JsonArrayWriterScope.() -> Unit) = jsonArrayWriter(builder)
}
