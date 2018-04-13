package com.satori.codegen.jpoet.dsl.scopes

interface IAnnotationScope : IScope {
  fun ARG_FMT(name: String, format: String, vararg args: Any)
}
