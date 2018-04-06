package com.satori.libs.gradle.github

import org.gradle.testfixtures.*
import org.gradle.testkit.runner.*
import org.junit.*
import org.junit.rules.*
import java.io.*

class GitHubPluginTests : Assert() {
  
  @Test
  public fun `validate project extentions`() {
    val project = ProjectBuilder.builder().build()
    project.pluginManager.apply("com.satori.github")
  
    assertSame(
      GitHubDeleteReleaseTask::class.java,
      project.extensions.getByName("GitHubDeleteReleaseTask")
    )
  
    assertSame(
      GitHubListReleasesTask::class.java,
      project.extensions.getByName("GitHubListReleasesTask")
    )
  
    assertSame(
      GitHubPublishReleaseTask::class.java,
      project.extensions.getByName("GitHubPublishReleaseTask")
    )
  
  }
}