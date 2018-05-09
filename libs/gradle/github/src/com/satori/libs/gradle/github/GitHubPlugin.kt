package com.satori.libs.gradle.github

import com.satori.libs.gradle.plugin.annotations.*
import com.satori.libs.gradle.utils.*
import org.gradle.api.*

@GradlePlugin("com.satori.github")
open class GitHubPlugin : Plugin<Project> {
  
  override fun apply(project: Project) {
    project.addExtension("GitHubPublishReleaseTask", GitHubPublishReleaseTask::class.java)
    project.addExtension("GitHubDeleteReleaseTask", GitHubDeleteReleaseTask::class.java)
    project.addExtension("GitHubListReleasesTask", GitHubListReleasesTask::class.java)
  }
}