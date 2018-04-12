package com.satori.codegen.codemodel.dsl.builders

import com.satori.codegen.codemodel.dsl.*
import com.satori.codegen.codemodel.dsl.traits.*
import com.sun.codemodel.*

open class TypeBuilder(jdef: JDefinedClass) : BaseBuilder(jdef.owner()), ITypeTrait {
  override val typeTrait = jdef
  override val annotations = ArrayList<AnnotationSpec>()
  override var mods = JMod.NONE
  
}
