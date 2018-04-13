package com.satori.codegen.jpoet.dsl.extensions

import com.satori.codegen.jpoet.dsl.scopes.*

inline fun <reified T : Enum<T>> IAnonymousClassScope.ARG(name: String, value: T) = ARG_FMT(
  "\$T.\$L", T::class.java, value
)

fun IAnonymousClassScope.ARG(value: String) = ARG_FMT("\$S", value)
