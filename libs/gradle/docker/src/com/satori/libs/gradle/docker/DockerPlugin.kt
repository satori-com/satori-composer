package com.satori.libs.gradle.docker

import com.satori.libs.gradle.utils.*
import org.gradle.api.*

open class DockerPlugin : Plugin<Project> {
  
  override fun apply(project: Project) {
    project.addExtension("DockerBaseTask", DockerBaseTask::class.java)
    project.addExtension("DockerBuildImageTask", DockerBuildImageTask::class.java)
    project.addExtension("DockerRunContainerTask", DockerRunContainerTask::class.java)
    project.addExtension("DockerStopContainerTask", DockerStopContainerTask::class.java)
  }
}