package com.satori.codegen.jpoet.dsl.scopes

interface IFileScope : IScope {
  fun COMMENT(line: String)
  fun PACKAGE(name: String, configure: IPackageScope.() -> Unit)
}
