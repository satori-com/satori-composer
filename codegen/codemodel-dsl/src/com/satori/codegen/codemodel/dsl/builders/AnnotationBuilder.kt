package com.satori.codegen.codemodel.dsl.builders

import com.satori.codegen.codemodel.dsl.*
import com.satori.codegen.codemodel.dsl.scopes.*
import com.sun.codemodel.*

class AnnotationBuilder(val type: JClass) : BaseBuilder(type.owner()), IAnnotationScope {
  
  val spec = AnnotationSpec(type)
  
  fun build() = spec
  
  // IAnnotationScope
  
  override fun ARG(name: String, format: String, vararg args: Any) {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }
  
  override fun ARG(name: String, expr: JExpression) {
    spec.args.put(name, expr)
  }
}
