package com.satori.codegen.codemodel.dsl.scopes

interface IPackageScope : IScope, ITypeContainerScope {
  fun IMPORT()
}
