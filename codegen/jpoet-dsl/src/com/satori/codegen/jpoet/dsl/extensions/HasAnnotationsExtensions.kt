package com.satori.codegen.jpoet.dsl.extensions

import com.satori.codegen.jpoet.dsl.scopes.*
import com.squareup.javapoet.*

inline fun <reified T> IHasAnnotations.ANNOTATION() = ANNOTATION(ClassName.get(T::class.java))
inline fun <reified T> IHasAnnotations.ANNOTATION(value: String) = ANNOTATION(ClassName.get(T::class.java), value)
inline fun <reified T> IHasAnnotations.ANNOTATION(noinline configure: IAnnotationScope.() -> Unit) = ANNOTATION(
  ClassName.get(T::class.java), configure
)

fun IHasAnnotations.ANNOTATION(type: ClassName) = ANNOTATION(type) {
}

fun IHasAnnotations.ANNOTATION(type: ClassName, value: String) = ANNOTATION(type) {
  ARG("value", value)
}

inline fun <reified T> IHasAnnotations.ANNOTATION(value: Any) = ANNOTATION(ClassName.get(T::class.java)) {
  ARG("value", value)
}
