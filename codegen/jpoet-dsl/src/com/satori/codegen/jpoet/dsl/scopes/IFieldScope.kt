package com.satori.codegen.jpoet.dsl.scopes

interface IFieldScope : IScope, IHasAccessModifier, IHasAnnotations {
  fun STATIC()
  fun INIT(fmt: String, vararg args: Any)
}
