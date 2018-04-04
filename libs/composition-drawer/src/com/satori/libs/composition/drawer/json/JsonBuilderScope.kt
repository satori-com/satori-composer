package com.satori.libs.composition.drawer.json

@JsonBuilderScope.Marker
open class JsonBuilderScope {
  @DslMarker
  @Target(AnnotationTarget.CLASS, AnnotationTarget.TYPE)
  annotation class Marker
}
