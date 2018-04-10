package com.satori.libs.gradle.docker

import org.gradle.api.tasks.*

open class DockerRunContainerTask : DockerBaseTask() {
  @Input var imageName: String? = null
  @Input var containerName: String? = null
  @Input var runArgs = ArrayList<String>()
  @Input var cmdArgs = ArrayList<String>()
  @Input @Optional var logOptions: LogOptions? = LogOptions()
  @Input @Optional var restart: String? = "always"
  
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
      setCommandLine(*buildDockerCommand().toArray(),
        "rm", "-f", containerName
      )
      println("stopping docker container '$containerName'...")
    }
  
    println("running docker container '$containerName'...")
    exec{
      val cmd = buildDockerCommand("run", "-d", "--name", containerName)
      logOptions?.apply {
        maxSize?.also{
          cmd.add("--log-opt")
          cmd.add("max-size=$it")
        }
        maxFile?.also{
          cmd.add("--log-opt")
          cmd.add("max-file=$it")
        }
      }
      restart?.also {
        cmd.add("--restart=$it")
      }
      cmd.addAll(runArgs)
      cmd.add(imageName)
      cmd.addAll(cmdArgs)
      setCommandLine(cmd)
    }
    
    exec ("ps", "--filter", "name=$containerName")
  }
}

