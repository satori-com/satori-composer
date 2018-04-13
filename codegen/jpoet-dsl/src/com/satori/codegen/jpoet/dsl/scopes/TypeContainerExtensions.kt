package com.satori.codegen.jpoet.dsl.scopes

import com.satori.codegen.jpoet.dsl.builders.*
import com.squareup.javapoet.*

fun ITypeContainer.CLASS(name: String, configure: ITypeScope.() -> Unit) {
  val builder = TypeBuilder(name)
  builder.configure()
  TYPE(builder.build())
}

fun ITypeContainer.ENUM(name: String, configure: IEnumScope.() -> Unit) {
  val builder = EnumBuilder(name)
  builder.configure()
  TYPE(builder.build())
}

fun ITypeContainer.INTERFACE(name: String, configure: ITypeScope.() -> Unit) {
  val builder = TypeBuilder(TypeSpec.interfaceBuilder(name))
  builder.configure()
  TYPE(builder.build())
}
