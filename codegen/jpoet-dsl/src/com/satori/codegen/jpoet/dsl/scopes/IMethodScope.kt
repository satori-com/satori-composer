package com.satori.codegen.jpoet.dsl.scopes

import com.squareup.javapoet.*

interface IMethodScope : IScope, IHasAnnotations, IHasAccessModifier {
  fun ARG(name: String, type: TypeName)
  fun RETURNS(type: TypeName)
  fun CODE(statements: String)
  fun STMT(statement: String)
  fun STMT(fmt: String, vararg args: Any)
  fun ABSTRACT()
}
