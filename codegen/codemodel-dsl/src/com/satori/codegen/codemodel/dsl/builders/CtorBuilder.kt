package com.satori.codegen.codemodel.dsl.builders

import com.satori.codegen.codemodel.dsl.*
import com.satori.codegen.codemodel.dsl.scopes.*
import com.satori.codegen.codemodel.dsl.traits.*
import com.sun.codemodel.*

open class CtorBuilder(val jdef: JDefinedClass) : BaseBuilder(jdef.owner()), IMethodScope, IHasAccessModifierTrait, IHasAnnotationsTrait {
  
  val args = ArrayList<ArgSpec>()
  var throws: JClass? = null
  val body = ArrayList<String>()
  
  override var mods = JMod.NONE
  override var annotations = ArrayList<AnnotationSpec>()
  
  fun build() {
    val jmethod = jdef.constructor(mods)
    args.forEach {
      jmethod.param(it.type, it.name)
    }
    annotations.forEach { spec ->
      jmethod.annotate(spec.type).apply {
        spec.args.forEach { name, expr ->
          param(name, expr)
        }
      }
    }
    throws?.let { jmethod._throws(it) }
    jmethod.body().apply {
      body.forEach {
        
        directStatement(it)
      }
      //directStatement("!!!!!!!!!!!!\n")
    }
  }
  
  override fun ARG(name: String, type: JType) {
    args.add(ArgSpec(name, type))
  }
  
  override fun RETURNS(type: JType) {
    throw Exception("not allowed")
  }
  
  override fun THROWS(type: JClass) {
    throws = type
  }
  
  override fun CODE(statements: String) {
    body.addAll(statements.lines().filter { it.isNotEmpty() })
  }
  
  override fun STMT(statement: String) {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }
  
  override fun STMT(fmt: String, vararg args: Any) {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }
  
  override fun ABSTRACT() {
    mods = (mods or JMod.ABSTRACT)
  }
  
  override fun STATIC() {
    mods = (mods or JMod.STATIC)
  }
}
