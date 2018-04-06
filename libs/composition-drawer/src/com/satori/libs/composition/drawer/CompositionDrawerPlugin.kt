package com.satori.libs.composition.drawer

import com.satori.libs.gradle.utils.*
import org.gradle.api.*

open class CompositionDrawerPlugin : Plugin<Project> {
  
  override fun apply(project: Project) {
    project.addExtension("GenerateCompositionDiagramTask", GenerateCompositionDiagramTask::class.java)
  }
}