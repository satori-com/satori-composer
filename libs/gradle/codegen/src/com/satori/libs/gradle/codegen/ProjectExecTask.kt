package com.satori.libs.gradle.codegen

import com.satori.libs.gradle.utils.*
import org.gradle.api.*
import org.gradle.api.plugins.*
import org.gradle.api.tasks.*

open class ProjectExecTask : JavaExec() {
  
  init {
    group = "codegen"
  }
  
  fun forProject(value: String) {
    forProject(project.project(value))
  }
  
  fun forProject(targetProject: Project) {
    val javaConv = targetProject.convention.getPlugin<JavaPluginConvention>()
    val appConv = targetProject.convention.getPlugin<ApplicationPluginConvention>()
    
    classpath = javaConv.sourceSets.getByName(SourceSet.MAIN_SOURCE_SET_NAME).runtimeClasspath
    conventionMapping.map("main") { appConv.mainClassName }
    conventionMapping.map("jvmArgs") { appConv.applicationDefaultJvmArgs }
  }
  
  override fun exec() {
    println(commandLine.joinToString(" "))
    super.exec()
  }
}