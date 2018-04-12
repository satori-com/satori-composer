package com.satori.codegen.codemodel.dsl.scopes

interface IEnumScope : ITypeScope {
  fun OPTION(name: String, configure: IAnonymousClassScope.() -> Unit)
}
