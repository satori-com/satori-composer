package com.satori.libs.gradle.codegen

import com.satori.libs.gradle.plugin.annotations.*
import org.gradle.api.*

@GradlePlugin(CodeGenPlugin.name)
open class CodeGenPlugin : Plugin<Project> {
  override fun apply(project: Project) {
    val conv = CodeGenPluginConvention(project)
    project.convention.plugins[name] = conv
  }
  
  companion object {
    const val name = "${MetaInfo.group}.${MetaInfo.alias}"
  }
}