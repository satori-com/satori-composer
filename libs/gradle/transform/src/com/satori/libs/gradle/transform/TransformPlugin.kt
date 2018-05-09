package com.satori.libs.gradle.transform

import com.satori.libs.gradle.plugin.annotations.*
import org.gradle.api.*

@GradlePlugin("com.satori.transform")
open class TransformPlugin : Plugin<Project> {
  
  override fun apply(project: Project) {
    val conv = TransformPluginConvention(project)
    project.convention.plugins["transform"] = conv
  }
}