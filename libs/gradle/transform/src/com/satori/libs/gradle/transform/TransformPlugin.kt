package com.satori.libs.gradle.transform

import com.satori.libs.gradle.utils.*
import groovy.lang.*
import org.gradle.api.*

open class TransformPlugin : Plugin<Project> {
  
  override fun apply(project: Project) {
    project.addExtension("transform") { closure: Closure<*> ->
      val spec = TransformSpec(project)
      (closure.clone() as Closure<*>).apply {
        delegate = spec
        resolveStrategy = Closure.DELEGATE_FIRST
        call()
      }
      Transform.execute(spec, project)
    }
    project.addExtension("TransformTask", TransformTask::class.java)
  }
}