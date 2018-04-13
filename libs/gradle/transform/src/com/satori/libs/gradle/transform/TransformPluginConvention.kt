package com.satori.libs.gradle.transform

import groovy.lang.*
import org.gradle.api.*

open class TransformPluginConvention(val project: Project) {
  val TransformTask = TransformTask::class.java
  fun transform(closure: Closure<*>): Any? {
    val spec = TransformSpec(project)
    (closure.clone() as Closure<*>).apply {
      delegate = spec
      resolveStrategy = Closure.DELEGATE_FIRST
      call()
    }
    Transform.execute(spec, project)
    return null
  }
}