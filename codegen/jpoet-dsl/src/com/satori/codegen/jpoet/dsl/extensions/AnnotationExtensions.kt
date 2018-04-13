package com.satori.codegen.jpoet.dsl.extensions

import com.satori.codegen.jpoet.dsl.scopes.*

fun IAnnotationScope.ARG(name: String, value: String) = ARG_FMT(name, "\$S", value)

fun IAnnotationScope.ARG(name: String, value: Any) = ARG_FMT(name, "\$L", value)

inline fun <reified T : Enum<T>> IAnnotationScope.ARG(name: String, value: T) = ARG_FMT(
  name, "\$T.\$L", T::class.java, value
)

inline fun <reified T : Enum<T>> IAnnotationScope.ARG(value: T) = ARG("value", value)
