package com.satori.libs.gradle.docker

import com.satori.libs.gradle.plugin.annotations.*
import com.satori.libs.gradle.utils.*
import org.gradle.api.*

@GradlePlugin(DockerPlugin.name)
open class DockerPlugin() : Plugin<Project> {
  
  override fun apply(project: Project) {
    val conv = DockerPluginConvention(project)
    project.convention.plugins[DockerPlugin.name] = conv
    
    project.addExtension("DockerBaseTask", DockerBaseTask::class.java)
    project.addExtension("DockerBuildImageTask", DockerBuildImageTask::class.java)
    project.addExtension("DockerRunContainerTask", DockerRunContainerTask::class.java)
    project.addExtension("DockerStopContainerTask", DockerStopContainerTask::class.java)
  }
  
  companion object {
    const val name = "${MetaInfo.group}.${MetaInfo.alias}"
  }
}