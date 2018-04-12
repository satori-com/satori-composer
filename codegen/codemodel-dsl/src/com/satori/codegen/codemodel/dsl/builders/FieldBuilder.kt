package com.satori.codegen.codemodel.dsl.builders

import com.satori.codegen.codemodel.dsl.*
import com.satori.codegen.codemodel.dsl.scopes.*
import com.satori.codegen.codemodel.dsl.traits.*
import com.sun.codemodel.*

open class FieldBuilder(val name: String, val type: JType) : BaseBuilder(type.owner()), IFieldScope, IHasAccessModifierTrait, IHasAnnotationsTrait {
  
  override var annotations = ArrayList<AnnotationSpec>()
  override var mods = JMod.NONE
  
  var init: JExpression? = null
  
  fun build(jdef: JDefinedClass) {
    jdef.field(mods, type, name).apply {
      annotations.forEach { spec ->
        annotate(spec.type).apply {
          spec.args.forEach { name, expr ->
            param(name, expr)
          }
        }
      }
      init?.let { init(it) }
    }
  }
  
  override fun STATIC() {
    mods = mods or JMod.STATIC
  }
  
  override fun FINAL() {
    mods = mods or JMod.FINAL
  }
  
  override fun INIT(expr: JExpression) {
    init = expr
  }
  
  override fun INIT(fmt: String, vararg args: Any) {
    init = JExpr.direct(fmt)
  }
}
