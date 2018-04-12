package com.satori.codegen.codemodel.dsl.scopes

import com.sun.codemodel.*

interface IFieldScope : IScope, IHasAccessModifier, IHasAnnotations {
  fun STATIC()
  fun FINAL()
  fun INIT(fmt:String, vararg args: Any)
  fun INIT(expr:JExpression)
}
