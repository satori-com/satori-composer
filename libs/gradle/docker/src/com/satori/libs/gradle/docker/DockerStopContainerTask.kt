package com.satori.libs.gradle.docker

import org.gradle.api.tasks.*

open class DockerStopContainerTask : DockerBaseTask() {
  var containerName: String? = null
  
  @TaskAction
  fun runContainer() {
    val containerName = containerName ?: getDefaultContainerName()
    
    exec {
      isIgnoreExitValue = true
      setCommandLine(*cmd.toArray(),
        "rm", "-f", containerName
      )
      println("stopping docker container '$containerName'...")
    }
    
    exec {
      setCommandLine(*cmd.toArray(),
        "ps", "--filter", "name=$containerName"
      )
    }
  }
}

