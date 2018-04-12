package com.satori.codegen.codemodel.dsl.scopes

import com.sun.codemodel.*

interface IHasAnnotations {
  fun ANNOTATION(type: JClass, configure: IAnnotationScope.() -> Unit)
  fun ANNOTATION(type: JClass)
  fun ANNOTATION(type: JClass, value: String)
}
