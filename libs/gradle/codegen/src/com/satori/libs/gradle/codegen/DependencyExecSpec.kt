package com.satori.libs.gradle.codegen

import org.gradle.api.artifacts.*
import org.gradle.process.*

interface DependencyExecSpec : JavaExecSpec {
  val configuration: Configuration
  fun dependency(dependencyNotation: Any)
}