package com.satori.gradle.composer.plugin

import org.gradle.api.*

open class ComposerPlugin() : Plugin<Project> {
  
  override fun apply(project: Project) {
    val conv = ComposerPluginConvention(project)
    project.convention.plugins["composer"] = conv
  }
}