package com.satori.libs.gradle.codegen

import com.satori.libs.gradle.utils.*
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
  
  /*fun test() {
    
    val javaConv = project.convention.getPlugin<JavaPluginConvention>()
    val appConv = project.convention.getPlugin<ApplicationPluginConvention>()
    
    val dependency = project.dependencies.create(getMarkerDependency(request))
    
    val configurations = project.configurations
    val configuration = configurations.detachedConfiguration()
    
    
    
    configuration.setTransitive(false)
  }*/
}