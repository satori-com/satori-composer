package com.satori.libs.gradle.codegen

import org.gradle.api.*

open class CodeGenPlugin : Plugin<Project> {
  override fun apply(project: Project) {
    val conv = CodeGenPluginConvention(project)
    project.convention.plugins["codegen"] = conv
  }
}