package com.satori.codegen.jpoet.dsl.builders

import com.satori.codegen.jpoet.dsl.*
import com.satori.codegen.jpoet.dsl.scopes.*
import com.squareup.javapoet.*
import javax.lang.model.element.*

class FieldBuilder(builder: FieldSpec.Builder) : IFieldScope {
  var access = AccessModifier.PUBLIC
  val builder = builder
  
  fun build(): FieldSpec {
    when (access) {
      AccessModifier.PUBLIC -> builder.addModifiers(Modifier.PUBLIC)
      AccessModifier.PRIVATE -> builder.addModifiers(Modifier.PRIVATE)
      AccessModifier.PROTECTED -> builder.addModifiers(Modifier.PROTECTED)
    }
    return builder.build()
  }
  
  // IFieldScope implementation
  
  override fun STATIC() {
    builder.addModifiers(Modifier.STATIC)
  }
  
  override fun INIT(fmt: String, vararg args: Any) {
    builder.initializer(fmt, *args)
  }
  
  // IHasAccessModifier implementation
  
  override fun ACCESS(value: AccessModifier) {
    access = value
  }
  
  // IAnnotateableScope
  
  override fun ANNOTATION(type: ClassName, configure: IAnnotationScope.() -> Unit) {
    builder.addAnnotation(AnnotationBuilder(type).apply {
      configure()
    }.build())
  }
}
