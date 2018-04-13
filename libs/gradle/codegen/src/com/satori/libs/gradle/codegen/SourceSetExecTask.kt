package com.satori.libs.gradle.codegen

import com.satori.libs.gradle.utils.*
import org.gradle.api.*
import org.gradle.api.plugins.*
import org.gradle.api.tasks.*

open class SourceSetExecTask : JavaExec() {
  
  val javaConv = project.convention.getPlugin<JavaPluginConvention>()
  
  init {
    group = "codegen"
  }
  
  fun forSourceSet(name: String) {
    forSourceSet(javaConv.sourceSets.getByName(name))
  }
  
  fun forSourceSet(forSourceSet: SourceSet) {
    classpath = forSourceSet.runtimeClasspath
  }
  
  override fun exec() {
    println(commandLine.joinToString(" "))
    super.exec()
  }
}