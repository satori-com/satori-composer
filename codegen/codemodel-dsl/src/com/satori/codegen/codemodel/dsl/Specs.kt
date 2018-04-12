package com.satori.codegen.codemodel.dsl

import com.sun.codemodel.*

data class ArgSpec(
  val name: String,
  val type: JType
)

data class AnnotationSpec(
  val type: JClass,
  val args: LinkedHashMap<String, JExpression> = LinkedHashMap()
)

data class ClassSpec(val jclass: JDefinedClass) {
}

data class ClassName(
  val pckg: String,
  val name: String
)
