package com.satori.libs.gradle.utils

import org.gradle.api.plugins.*

inline fun <reified T> Convention.getPlugin(): T {
  return getPlugin(T::class.java)
}

inline fun <reified T> Convention.getPlugin(name: String): T {
  return plugins[name] as T
}

inline fun <reified T> Convention.findPlugin(): T? {
  return findPlugin(T::class.java)
}

inline fun <reified T> Convention.findPlugin(name: String): T? {
  return plugins[name] as? T
}
