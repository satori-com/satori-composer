package com.satori.codegen.codemodel.dsl.scopes

import com.satori.codegen.codemodel.dsl.*

interface ITypeContainerScope : IScope {
  fun CLASS(name: String, configure: ITypeScope.() -> Unit): ClassSpec
  fun ENUM(name: String, configure: IEnumScope.() -> Unit)
  fun INTERFACE(name: String, configure: ITypeScope.() -> Unit)
}
