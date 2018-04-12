package com.satori.codegen.codemodel.dsl.traits

import com.satori.codegen.codemodel.dsl.*
import com.satori.codegen.codemodel.dsl.scopes.*
import com.sun.codemodel.*

interface IHasAccessModifierTrait : IHasAccessModifier, IHasModifiersTrait {
  
  override fun ACCESS(value: AccessModifier) {
    val access = when (value) {
      AccessModifier.PUBLIC -> JMod.PUBLIC
      AccessModifier.PRIVATE -> JMod.PRIVATE
      AccessModifier.PROTECTED -> JMod.PROTECTED
      AccessModifier.DEFAULT -> JMod.NONE
    }
    mods = (mods and ACCESS_MOD_MASK) or access
  }
  
  companion object {
    val ACCESS_MOD_MASK = (JMod.PUBLIC or JMod.PRIVATE or JMod.PROTECTED).inv()
  }
}
