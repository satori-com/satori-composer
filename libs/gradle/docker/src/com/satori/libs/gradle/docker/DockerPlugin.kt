package com.satori.libs.gradle.docker

import com.satori.libs.gradle.plugin.annotations.*
import com.satori.libs.gradle.utils.*
import org.gradle.api.*

@GradlePlugin("com.satori.docker")
open class DockerPlugin() : Plugin<Project> {
  
  override fun apply(project: Project) {
    val conv = DockerPluginConvention(project)
    project.convention.plugins["docker"] = conv
    
    project.addExtension("DockerBaseTask", DockerBaseTask::class.java)
    project.addExtension("DockerBuildImageTask", DockerBuildImageTask::class.java)
    project.addExtension("DockerRunContainerTask", DockerRunContainerTask::class.java)
    project.addExtension("DockerStopContainerTask", DockerStopContainerTask::class.java)
  }
}