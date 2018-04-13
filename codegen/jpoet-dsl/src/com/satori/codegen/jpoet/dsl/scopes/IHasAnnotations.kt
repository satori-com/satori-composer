package com.satori.codegen.jpoet.dsl.scopes

import com.squareup.javapoet.*

interface IHasAnnotations {
  fun ANNOTATION(type: ClassName, configure: IAnnotationScope.() -> Unit)
}
