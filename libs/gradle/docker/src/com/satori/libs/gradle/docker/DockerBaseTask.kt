package com.satori.libs.gradle.docker

import org.gradle.api.*
import org.gradle.api.plugins.*
import org.gradle.api.tasks.*
import org.gradle.process.*
import java.io.*

open class DockerBaseTask : DefaultTask() {
  val basePluginConvention = project.convention.plugins.get("base") as? BasePluginConvention
  val archivesBaseName: String? get() = basePluginConvention?.archivesBaseName
  val rootProject: Project get() = project.rootProject
  val buildDir: File get() = project.buildDir
  
  @Input var cmd = arrayListOf("docker")
  
  @Input @Optional var host: String? = null
  @Input @Optional var tls: Boolean = false
  @Input @Optional var tlsVerify: Boolean = false
  @Input @Optional var tlsKey: File? = null
  @Input @Optional var tlsCert: File? = null
  @Input @Optional var tlsCaCert: File? = null
  
  fun host(value: String?) {
    host = value
  }
  
  fun buildDockerCommand(vararg args: String): ArrayList<String> {
    val result = ArrayList<String>()
    result.addAll(cmd)
    host?.also {
      result.add("-H")
      result.add(it)
    }
    if (tls) {
      result.add("--tls")
    }
    if (tlsVerify) {
      result.add("--tlsverify")
    }
    
    tlsCert?.also {
      result.add("--tlscert")
      result.add(it.path)
    }
    
    tlsKey?.also {
      if (result.contains("--tlskey")) {
        System.err.println("'--tls' specified multiple times")
      }
      result.add("--tlskey")
      result.add(it.path)
    }
    
    tlsCaCert?.also {
      result.add("--tlscacert")
      result.add(it.path)
    }
    result.addAll(args)
    return result
  }
  
  fun getDefaultImageName(): String {
    return if (archivesBaseName != null) {
      "${rootProject.name}/${archivesBaseName}".toLowerCase()
    } else {
      rootProject.name.toLowerCase()
    }
  }
  
  fun getDefaultImageTag(): String {
    return project.version.toString().toLowerCase()
  }
  
  fun getDefaultContainerName(): String {
    return if (archivesBaseName != null) {
      "${rootProject.name}-${archivesBaseName}".toLowerCase()
    } else {
      rootProject.name.toLowerCase()
    }
  }
  
  fun exec(vararg args: String) {
    project.exec { execSpec ->
      execSpec.setCommandLine(*buildDockerCommand(*args).toArray())
      println("> ${execSpec.getCommandLineAsString()}")
    }
  }
  
  fun exec(builder: ExecSpec.() -> Unit) {
    project.exec { execSpec ->
      execSpec.builder()
      println("> ${execSpec.getCommandLineAsString()}")
    }
  }
  
  fun buildDockerCommandAsString(vararg args: String): String{
    return buildDockerCommand(*args).joinToString(" ")
  }
  
  companion object {
    fun ExecSpec.getCommandLineAsString(): String {
      return commandLine.joinToString(" ")
    }
  }
}

