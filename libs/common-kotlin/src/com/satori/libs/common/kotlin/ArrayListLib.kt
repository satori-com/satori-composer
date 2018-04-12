package com.satori.libs.common.kotlin

inline fun <reified T> ArrayList<T>.addAll(vararg values: T) {
  addAll(values)
}