package com.satori.codegen.codemodel.dsl.scopes

inline fun <reified T> IMethodScope.ARG(name: String) = ARG(
  name, TREF<T>()
)

fun IMethodScope.ARG(name: String, type: String) = ARG(
  name, CREF(type)
)

inline fun <reified T:Any> IMethodScope.THROWS() = THROWS(
  CREF<T>()
)

inline fun IMethodScope.THROWS(type: Class<*>) = THROWS(
  CREF(type)
)

