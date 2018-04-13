package com.satori.codegen.jpoet.dsl.builders

import com.satori.codegen.jpoet.dsl.scopes.*
import com.squareup.javapoet.*

class AnnotationBuilder(builder: AnnotationSpec.Builder) : IAnnotationScope {
  val builder = builder
  
  constructor(type: ClassName) : this(AnnotationSpec.builder(type))
  
  fun build(): AnnotationSpec = builder.build()
  
  // IAnnotationScope
  
  override fun ARG_FMT(name: String, format: String, vararg args: Any) {
    builder.addMember(name, format, *args)
  }
}
