package com.satori.libs.gradle.docker

import org.gradle.testfixtures.*
import org.junit.*

class DockerPluginTests : Assert() {
  
  @Test
  public fun `validate project extentions`() {
    val project = ProjectBuilder.builder().build()
    project.pluginManager.apply("com.satori.docker")
    
    assertSame(
       DockerBuildImageTask::class.java,
       project.extensions.getByName("DockerBuildImageTask")
     )
   
     assertSame(
       DockerRunContainerTask::class.java,
       project.extensions.getByName("DockerRunContainerTask")
     )
   
     assertSame(
       DockerStopContainerTask::class.java,
       project.extensions.getByName("DockerStopContainerTask")
     )
    
  }
}