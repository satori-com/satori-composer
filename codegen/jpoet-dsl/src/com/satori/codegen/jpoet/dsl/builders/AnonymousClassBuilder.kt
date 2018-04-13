package com.satori.codegen.jpoet.dsl.builders

import com.satori.codegen.jpoet.dsl.scopes.*
import com.squareup.javapoet.*

open class AnonymousClassBuilder : IAnonymousClassScope {
  private val argFmts = ArrayList<String>()
  private val argVals = ArrayList<Any>()
  private val annotations = ArrayList<AnnotationSpec>()
  
  // IAnonymousClassScope implementation
  
  override fun ARG_FMT(fmt: String, vararg values: Any) {
    argFmts.add(fmt)
    argVals.addAll(values)
  }
  
  fun build(): TypeSpec {
    val builder = TypeSpec.anonymousClassBuilder(argFmts.joinToString(","), *argVals.toArray())
    for (annotation in annotations) {
      builder.addAnnotation(annotation)
    }
    return builder.build()
  }
  
  // IHasAnnotations implementation
  
  override fun ANNOTATION(type: ClassName, configure: IAnnotationScope.() -> Unit) {
    annotations.add(AnnotationBuilder(type).apply {
      configure()
    }.build())
  }
}
