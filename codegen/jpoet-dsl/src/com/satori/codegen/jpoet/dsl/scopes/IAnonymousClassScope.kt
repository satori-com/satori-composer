package com.satori.codegen.jpoet.dsl.scopes

interface IAnonymousClassScope : IScope, IHasAnnotations {
  fun ARG_FMT(fmt: String, vararg values: Any)
}
