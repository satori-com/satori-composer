package com.satori.codegen.codemodel.dsl.scopes

interface IAnonymousClassScope : IScope, IHasAnnotations {
  fun ARG_FMT(fmt: String, vararg values: Any)
}
