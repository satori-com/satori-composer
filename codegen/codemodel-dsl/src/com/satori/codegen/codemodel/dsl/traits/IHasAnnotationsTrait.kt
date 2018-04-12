package com.satori.codegen.codemodel.dsl.traits

import com.satori.codegen.codemodel.dsl.*
import com.satori.codegen.codemodel.dsl.builders.*
import com.satori.codegen.codemodel.dsl.scopes.*
import com.sun.codemodel.*

interface IHasAnnotationsTrait : IHasAnnotations, IScopeTrait {
  val annotations: ArrayList<AnnotationSpec>
  
  override fun ANNOTATION(type: JClass, configure: IAnnotationScope.() -> Unit) {
    val spec = AnnotationBuilder(type).apply {
      configure()
    }.build()
    annotations.add(spec)
  }
  
  override fun ANNOTATION(type: JClass) {
    annotations.add(AnnotationSpec(type))
  }
  
  override fun ANNOTATION(type: JClass, value: String) = ANNOTATION(type) {
    ARG("value", JExpr.lit(value))
  }
}
