package com.satori.codegen.codemodel.dsl.scopes

import com.sun.codemodel.*

interface ITypeScope : IScope, ITypeContainerScope, IHasAnnotations, IHasAccessModifier, IHasComments {
  fun EXTENDS(type: JClass)
  fun CTOR(configure: IMethodScope.() -> Unit)
  fun FUN(name: String, configure: IMethodScope.() -> Unit)
  fun FIELD(name: String, type: JType, configure: IFieldScope.() -> Unit)
  fun GETTER(name: String, configure: IMethodScope.() -> Unit)
  fun IMPLEMENTS(type: JClass)
  fun STATIC()
}


