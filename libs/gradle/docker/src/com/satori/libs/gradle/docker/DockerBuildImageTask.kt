package com.satori.libs.gradle.docker

import groovy.lang.*
import org.gradle.api.tasks.*
import java.io.*

open class DockerBuildImageTask : DockerBaseTask() {
  @Input var imageName: String? = null
  @Input var imageTag: String? = null
  @Input var buildArgs = arrayOf<String>()
  @Input @Optional var contextDir: File? = null
  
  private var _prepareContext: Closure<*>? = null
  private var _cleanupContext: Closure<*>? = null
  
  fun prepareContext(closure: Closure<*>) {
    _prepareContext = closure
  }
  
  fun prepareContext() {
    _prepareContext?.let {
      println("preparing docker context...")
      it.call()
    }
  }
  
  fun cleanupContext(closure: Closure<*>) {
    _cleanupContext = closure
  }
  
  fun cleanupContext() {
    _cleanupContext?.let {
      println("cleaning up docker context...")
      it.call()
    }
  }
  
  fun getDefaultContextDir(): File {
    return File(buildDir, "docker")
  }
  
  @TaskAction
  fun buildImage() {
    
    val imageName = imageName ?: getDefaultImageName()
    val taggedImageName = "$imageName:${imageTag ?: getDefaultImageTag()}"
    val contextDir = contextDir ?: getDefaultContextDir()
    
    prepareContext()
    
    try {
      
      println("building docker image '$taggedImageName'...")
      exec("build", *buildArgs, "-t", taggedImageName, contextDir.path)
      
      println("tagging docker image '${imageName}'...")
      exec("tag", taggedImageName, imageName)
      
    } finally {
      cleanupContext()
    }
  }
}

