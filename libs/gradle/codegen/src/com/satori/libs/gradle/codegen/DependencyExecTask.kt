package com.satori.libs.gradle.codegen

import org.gradle.api.tasks.*

open class DependencyExecTask : JavaExec(), DependencyExecSpec {
  
  @Input
  override val configuration = project.configurations.detachedConfiguration()
  
  init {
    group = "codegen"
    classpath = configuration
  }
  
  override fun dependency(dependencyNotation: Any) {
    val dependency = project.dependencies.create(dependencyNotation)
    configuration.dependencies.add(dependency)
  }
  
  override fun exec() {
    println(commandLine.joinToString(" "))
    super.exec()
  }
}