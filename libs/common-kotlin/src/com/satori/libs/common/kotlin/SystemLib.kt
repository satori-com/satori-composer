package com.satori.libs.common.kotlin

fun Any.identityHashCode() = System.identityHashCode(this)
fun Any.identity() = Integer.toHexString(identityHashCode())
