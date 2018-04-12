package com.satori.codegen.codemodel.dsl.scopes

import com.satori.codegen.codemodel.dsl.*

interface IHasAccessModifier {
  fun ACCESS(value: AccessModifier)
  fun PUBLIC() = ACCESS(AccessModifier.PUBLIC)
  fun PRIVATE() = ACCESS(AccessModifier.PRIVATE)
  fun PROTECTED() = ACCESS(AccessModifier.PROTECTED)
}
