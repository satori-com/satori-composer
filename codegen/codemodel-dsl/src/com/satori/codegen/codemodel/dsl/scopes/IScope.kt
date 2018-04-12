package com.satori.codegen.codemodel.dsl.scopes

import com.satori.codegen.utils.*
import com.sun.codemodel.*

@IScope.Marker
interface IScope : ICodeFormatter {
  @DslMarker
  annotation class Marker
  
  fun CREF(cls: Class<*>): JClass
  fun CREF(cls: String): JClass
  
  fun TREF(cls: Class<*>): JType
  fun TREF(cls: String): JType
  
  fun ARRAY(type: JType) = type.array()
  fun NEW(type: JClass) = JExpr._new(type)
  fun NEW(type: JType) = JExpr._new(type)
  
  companion object {
    val NULL = JExpr._null()
    val TRUE = JExpr.TRUE
    val FALSE = JExpr.FALSE
    val THIS = JExpr._this()
  }
}
