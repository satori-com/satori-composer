package com.satori.libs.common.kotlin.json

@JsonBuilderScope.Marker
open class JsonBuilderScope {
  @DslMarker
  @Target(AnnotationTarget.CLASS, AnnotationTarget.TYPE)
  annotation class Marker
}
