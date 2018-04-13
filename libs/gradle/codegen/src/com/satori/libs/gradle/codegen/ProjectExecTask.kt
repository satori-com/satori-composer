package com.satori.libs.gradle.codegen

import com.satori.libs.gradle.utils.*
import org.gradle.api.*
import org.gradle.api.plugins.*
import org.gradle.api.tasks.*

open class ProjectExecTask : JavaExec() {
  
  init {
    group = "codegen"
  }
  
  fun forProject(name: String) {
    forProject(project.project(name))
  }
  
  fun forProject(forProject: Project) {
    val javaConv = forProject.convention.getPlugin<JavaPluginConvention>()
    val appConv = forProject.convention.getPlugin<ApplicationPluginConvention>()
    
    classpath = javaConv.sourceSets.getByName(SourceSet.MAIN_SOURCE_SET_NAME).runtimeClasspath
    conventionMapping.map("main") { appConv.mainClassName }
    conventionMapping.map("jvmArgs") { appConv.applicationDefaultJvmArgs }
  }
  
  override fun exec() {
    println(commandLine.joinToString(" "))
    super.exec()
  }
}