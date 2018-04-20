package com.satori.libs.gradle.utils

import org.gradle.api.*
import org.gradle.api.tasks.*

inline fun <reified T: Task> TaskContainer.create(name: String): T {
  return create(name, T::class.java)
}
