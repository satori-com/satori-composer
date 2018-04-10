package com.satori.libs.gradle.docker

import org.gradle.api.tasks.*

open class DockerStopContainerTask : DockerBaseTask() {
  var containerName: String? = null
  
  @TaskAction
  fun runContainer() {
    val containerName = containerName ?: getDefaultContainerName()
  
    println("stopping docker container '$containerName'...")
    exec {
      isIgnoreExitValue = true
      setCommandLine(*cmd.toArray(),
        "rm", "-f", containerName
      )
    }
    
    exec ("ps", "--filter", "name=$containerName")
  }
}

