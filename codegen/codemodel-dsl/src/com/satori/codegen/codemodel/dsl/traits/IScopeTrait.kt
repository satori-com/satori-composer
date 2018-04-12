package com.satori.codegen.codemodel.dsl.traits

import com.satori.codegen.codemodel.dsl.scopes.*
import com.sun.codemodel.*

@IScopeTrait.Marker
interface IScopeTrait : IScope {
  @DslMarker
  annotation class Marker
  
  val jmodel: JCodeModel
  
  override fun CREF(cls: Class<*>): JClass = jmodel.ref(cls)
  override fun CREF(cls: String) = jmodel.parseType(cls) as JClass;
  
  override fun TREF(cls: Class<*>) = jmodel._ref(cls)
  override fun TREF(cls: String) = jmodel.parseType(cls)
  
  companion object {
    val NULL = JExpr._null()
    val TRUE = JExpr.TRUE
    val FALSE = JExpr.FALSE
    val THIS = JExpr._this()
  }
}


