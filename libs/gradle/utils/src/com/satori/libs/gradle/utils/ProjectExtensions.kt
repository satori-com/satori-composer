package com.satori.libs.gradle.utils

import org.gradle.api.*

inline fun <reified T> Project.addExtension(name: String, instance: T) {
  extensions.add(T::class.java, name, instance)
}

inline fun <reified T: Plugin<*>> Project.getPlugin(): T {
  return plugins.getPlugin(T::class.java)
}

