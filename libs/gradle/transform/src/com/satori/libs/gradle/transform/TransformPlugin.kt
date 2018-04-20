package com.satori.libs.gradle.transform

import com.satori.libs.gradle.utils.*
import org.gradle.api.*

open class TransformPlugin : Plugin<Project> {
  
  override fun apply(project: Project) {
    val conv = TransformPluginConvention(project)
    project.convention.plugins["transform"] = conv
  }
}