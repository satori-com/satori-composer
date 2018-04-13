package com.satori.codegen.jpoet.dsl.scopes

import com.squareup.javapoet.*

interface ITypeScope : IScope, ITypeContainer, IHasAnnotations, IHasAccessModifier {
  fun EXTENDS(type: TypeName)
  fun CTOR(configure: IMethodScope.() -> Unit)
  fun FUN(name: String, configure: IMethodScope.() -> Unit)
  fun FIELD(name: String, type: TypeName, configure: IFieldScope.() -> Unit)
  fun GETTER(name: String, configure: IMethodScope.() -> Unit)
  fun IMPLEMENTS(type: TypeName)
  fun STATIC()
}
