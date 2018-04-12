package com.satori.codegen.codemodel.dsl.scopes

import com.sun.codemodel.*

interface IMethodScope : IScope, IHasAnnotations, IHasAccessModifier {
  fun ARG(name: String, type: JType)
  fun RETURNS(type: JType)
  fun THROWS(type: JClass)
  fun CODE(statements: String)
  fun STMT(statement: String)
  fun STMT(fmt: String, vararg args: Any)
  fun ABSTRACT()
  fun STATIC()
}
