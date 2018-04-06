package com.satori.libs.gradle.utils

import org.gradle.api.*

inline fun <reified T> Project.addExtension(name: String, instance: T) {
  project.extensions.add(T::class.java, name, instance)
}