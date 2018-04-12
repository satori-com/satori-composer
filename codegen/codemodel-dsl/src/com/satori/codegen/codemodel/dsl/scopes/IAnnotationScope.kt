package com.satori.codegen.codemodel.dsl.scopes

import com.sun.codemodel.*

interface IAnnotationScope : IScope {
  fun ARG(name: String, format: String, vararg args: Any)
  fun ARG(name: String, expr: JExpression)
}
