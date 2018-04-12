package com.satori.codegen.codemodel.dsl.scopes

import com.sun.codemodel.*

inline fun <reified T : Any> IScope.CREF() = CREF(T::class.java)
inline fun <reified T> IScope.TREF() = TREF(T::class.java)

inline fun <reified T> IScope.ARRAY() = TREF(T::class.java).array()
inline fun IScope.ARRAY(type: String) = TREF(type).array()

val IScope.THIS get() = JExpr._this()
val IScope.NULL get() = JExpr._null()
val IScope.TRUE get() = JExpr.TRUE
val IScope.FALSE get() = JExpr.FALSE
