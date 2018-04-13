package com.satori.libs.gradle.transform

import org.gradle.api.*

open class TransformPlugin : Plugin<Project> {
  
  override fun apply(project: Project) {
    val conv = TransformPluginConvention(project)
    project.convention.plugins["transform"] = conv
  }
}