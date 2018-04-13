package com.satori.libs.gradle.docker

import org.gradle.api.*

open class DockerPluginConvention(val project: Project) {
  val DockerBase = DockerBaseTask::class.java
  val DockerBuildImage = DockerBuildImageTask::class.java
  val DockerRunContainer = DockerRunContainerTask::class.java
  val DockerStopContainer = DockerStopContainerTask::class.java
}