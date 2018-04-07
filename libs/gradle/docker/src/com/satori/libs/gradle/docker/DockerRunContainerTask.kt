package com.satori.libs.gradle.docker

import org.gradle.api.tasks.*

open class DockerRunContainerTask : DockerBaseTask() {
  var imageName: String? = null
  var containerName: String? = null
  var runArgs = ArrayList<String>()
  var cmdArgs = ArrayList<String>()
  
  var logOptions: LogOptions? = null
  var restart: String? = "always"
  
  class LogOptions{
    var maxSize: String? = "32m"
    var maxFile: String? = "16"
  }
  
  @TaskAction
  fun runContainer() {
    val containerName = containerName ?: getDefaultContainerName()
    val imageName = imageName ?: getDefaultImageName()
    
    exec {
      isIgnoreExitValue = true
      setCommandLine(*cmd.toArray(),
        "rm", "-f", containerName
      )
      println("stopping docker container '$containerName'...")
      println("> ${commandLine.joinToString(" ")}")
    }
    
    exec {
      setCommandLine(*cmd.toArray(),
        "run", "-d", "--name", containerName,
        "--log-opt", "max-size=32m", "--log-opt", "max-file=16",
        "--restart=always", *runArgs.toArray(), imageName, *cmdArgs.toArray()
      )
      println("running docker container '$containerName'...")
      println("> ${commandLine.joinToString(" ")}")
    }
    
    exec {
      setCommandLine(*cmd.toArray(),
        "ps", "--filter", "name=$containerName"
      )
    }
  }
}

