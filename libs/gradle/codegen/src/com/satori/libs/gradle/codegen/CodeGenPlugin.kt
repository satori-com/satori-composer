package com.satori.libs.gradle.codegen

import com.satori.libs.gradle.plugin.annotations.*
import org.gradle.api.*

@GradlePlugin("com.satori.codegen")
open class CodeGenPlugin : Plugin<Project> {
  override fun apply(project: Project) {
    val conv = CodeGenPluginConvention(project)
    project.convention.plugins["codegen"] = conv
  }
}