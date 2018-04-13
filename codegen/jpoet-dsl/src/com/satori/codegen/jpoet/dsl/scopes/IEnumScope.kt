package com.satori.codegen.jpoet.dsl.scopes

interface IEnumScope : ITypeScope {
  fun OPTION(name: String, configure: IAnonymousClassScope.() -> Unit)
}
