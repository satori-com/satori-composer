package com.satori.codegen.jpoet.dsl.scopes

import com.squareup.javapoet.*

interface ITypeContainer {
  fun TYPE(type: TypeSpec)
}
