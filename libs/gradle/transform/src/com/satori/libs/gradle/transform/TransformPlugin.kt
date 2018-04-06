package com.satori.libs.gradle.transform

import com.satori.libs.gradle.utils.*
import groovy.lang.*
import org.gradle.api.*
import org.gradle.api.plugins.*

open class TransformPlugin : Plugin<Project> {
  
  override fun apply(project: Project) {
    val ext = project.properties["ext"] as ExtraPropertiesExtension
    
    project.addExtension("TransformTask", TransformTask::class.java)
    
    ext.set("transform", object : Closure<Any?>(null) {
      fun doCall(closure: Closure<*>): Any? {
        val spec = TransformSpec(project)
        (closure.clone() as Closure<*>).apply {
          delegate = spec
          resolveStrategy = Closure.DELEGATE_FIRST
          call()
        }
        Transform.execute(spec, project)
        return null
      }
    })
    
    /*project.addExtension<Any>("transform2", object: Closure<Any?>(null) {
      fun doCall(closure: Closure<*>): Any? {
        val spec = TransformSpec(project)
  
        (closure.clone() as Closure<*>).apply {
          delegate = spec
          resolveStrategy = Closure.DELEGATE_FIRST
          call()
        }
        Transform.execute(spec, project)
        return null
      }
    })*/
    
  }
}