package com.satori.codegen.codemodel.dsl.scopes

interface IFileScope : IScope, IHasComments {
  fun PACKAGE(name: String, configure: IPackageScope.() -> Unit)
}
