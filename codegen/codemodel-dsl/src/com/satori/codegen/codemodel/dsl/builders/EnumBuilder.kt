package com.satori.codegen.codemodel.dsl.builders

import com.satori.codegen.codemodel.dsl.*
import com.satori.codegen.codemodel.dsl.scopes.*
import com.satori.codegen.codemodel.dsl.traits.*
import com.sun.codemodel.*

open class EnumBuilder(jdef: JDefinedClass) : BaseBuilder(jdef.owner()), ITypeTrait, IEnumScope {
  
  override val typeTrait = jdef
  override val annotations = ArrayList<AnnotationSpec>()
  override var mods = JMod.NONE
  
  // IEnumScope implementation
  
  override fun OPTION(name: String, configure: IAnonymousClassScope.() -> Unit) {
    val c: JEnumConstant = typeTrait.enumConstant(name)
  }
  
}
