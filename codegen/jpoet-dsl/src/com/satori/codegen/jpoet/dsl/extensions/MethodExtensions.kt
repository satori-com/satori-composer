package com.satori.codegen.jpoet.dsl.extensions

import com.satori.codegen.jpoet.dsl.scopes.*
import com.squareup.javapoet.*

fun IMethodScope.ARG(name: String, type: String) {
  val i = type.lastIndexOf(".")
  return ARG(
    name = name,
    type = if (i >= 0) {
      ClassName.get(type.substring(0, i), type.substring(i + 1))
    } else {
      ClassName.get(type.substring(i), type.substring(i))
    }
  )
}

inline fun <reified T> IMethodScope.ARG(name: String) = ARG(name, ClassName.get(T::class.java))

inline fun <reified T> IMethodScope.VAR(name: String, expr: String) = STMT("\$T $name = ${expr}", T::class.java)
fun IMethodScope.RETURN(expression: String) = STMT("return ${expression}")
