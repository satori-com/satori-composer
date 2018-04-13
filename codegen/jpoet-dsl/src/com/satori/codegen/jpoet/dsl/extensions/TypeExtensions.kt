package com.satori.codegen.jpoet.dsl.extensions

import com.satori.codegen.jpoet.dsl.scopes.*
import com.squareup.javapoet.*

inline fun <reified T> ITypeScope.EXTENDS() = EXTENDS(ClassName.get(T::class.java))

fun ITypeScope.FUN_PUBLIC(name: String, configure: IMethodScope.() -> Unit) = FUN(name) {
  PUBLIC()
  configure()
}

// FIELD extensions

fun ITypeScope.FIELD(name: String, type: TypeName) = FIELD(name, type) {}

inline fun <reified T> ITypeScope.FIELD(name: String) = FIELD(name, ClassName.get(T::class.java))
inline fun <reified T> ITypeScope.FIELD(name: String, noinline configure: IFieldScope.() -> Unit) = FIELD(
  name, ClassName.get(T::class.java), configure
)

inline fun <reified T> ITypeScope.FIELD_PRIVATE(name: String) = FIELD_PRIVATE(name, ClassName.get(T::class.java))
inline fun <reified T> ITypeScope.FIELD_PRIVATE(name: String, noinline configure: IFieldScope.() -> Unit) = FIELD_PRIVATE(
  name, ClassName.get(T::class.java), configure
)

fun ITypeScope.FIELD_PRIVATE(name: String, type: TypeName, configure: IFieldScope.() -> Unit) = FIELD(name, type) {
  PRIVATE()
  configure()
}

fun ITypeScope.FIELD_PRIVATE(name: String, type: TypeName) = FIELD_PRIVATE(name, type) {
}

inline fun <reified T> ITypeScope.FIELD_PUBLIC(name: String) = FIELD_PUBLIC(name, ClassName.get(T::class.java))
inline fun <reified T> ITypeScope.FIELD_PUBLIC(name: String, noinline configure: IFieldScope.() -> Unit) = FIELD_PUBLIC(
  name, ClassName.get(T::class.java), configure
)

fun ITypeScope.FIELD_PUBLIC(name: String, type: TypeName, configure: IFieldScope.() -> Unit) = FIELD(name, type) {
  PUBLIC()
  configure()
}

fun ITypeScope.FIELD_PUBLIC(name: String, type: TypeName) = FIELD_PUBLIC(name, type) {
}

inline fun <reified T> ITypeScope.IMPLEMENTS() = IMPLEMENTS(ClassName.get(T::class.java))

