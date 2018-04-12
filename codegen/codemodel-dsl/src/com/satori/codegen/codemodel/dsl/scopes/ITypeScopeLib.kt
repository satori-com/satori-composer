package com.satori.codegen.codemodel.dsl.scopes

import com.sun.codemodel.*

inline fun <reified T> ITypeScope.FIELD(name: String) = FIELD(name, TREF<T>()) {}
inline fun <reified T> ITypeScope.FIELD(name: String, noinline configure: IFieldScope.() -> Unit) = FIELD(
  name, TREF<T>(), configure
)

inline fun ITypeScope.FIELD(name: String, type: JType) = FIELD(name, type) {}
inline fun ITypeScope.FIELD(name: String, type: String) = FIELD(name, TREF(type))
inline fun ITypeScope.FIELD(name: String, type: Class<*>) = FIELD(name, TREF(type))
