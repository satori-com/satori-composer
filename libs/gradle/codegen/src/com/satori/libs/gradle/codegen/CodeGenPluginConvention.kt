package com.satori.libs.gradle.codegen

import com.satori.libs.gradle.utils.*
import groovy.lang.*
import org.gradle.api.*
import org.gradle.api.plugins.*
import org.gradle.api.tasks.*
import org.gradle.process.*
import org.gradle.util.*

open class CodeGenPluginConvention(val project: Project) {
  val ProjectExec = ProjectExecTask::class.java
  val SourceSetExec = SourceSetExecTask::class.java
  
  fun projectExec(name: String, closure: Closure<*>) {
    projectExec(name, ConfigureUtil.configureUsing(closure))
  }
  fun projectExec(name: String, closure: Action<JavaExecSpec>) {
    val forProject = project.findProject(name) ?: throw Exception("project '$name' not found")
    val javaConv = forProject.convention.getPlugin<JavaPluginConvention>()
    val appConv = forProject.convention.getPlugin<ApplicationPluginConvention>()
    val forSourceSet = javaConv.sourceSets.getByName(SourceSet.MAIN_SOURCE_SET_NAME)
  
    project.javaexec { spec ->
      spec.classpath = forSourceSet.runtimeClasspath
      spec.main = appConv.mainClassName
      spec.jvmArgs = appConv.applicationDefaultJvmArgs.toList()
      closure.execute(spec)
      println(spec.commandLine.joinToString(" "))
    }
  }
  
  fun sourceSetExec(name: String, closure: Action<JavaExecSpec>) {
    val javaConv = project.convention.getPlugin<JavaPluginConvention>()
    val forSourceSet = javaConv.sourceSets.getByName(SourceSet.MAIN_SOURCE_SET_NAME)
  
    project.javaexec { spec ->
      spec.classpath = javaConv.sourceSets.getByName(name).runtimeClasspath
      closure.execute(spec)
      println(spec.commandLine.joinToString(" "))
    }
  }
  fun sourceSetExec(name: String, closure: Closure<*>) {
    sourceSetExec(name, ConfigureUtil.configureUsing(closure))
  }
}