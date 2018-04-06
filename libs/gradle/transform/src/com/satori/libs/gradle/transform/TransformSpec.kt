package com.satori.libs.gradle.transform

import org.gradle.api.*
import java.io.*

open class TransformSpec(val project: Project) : ITransformSpec {
  
  override var template: File? = null
  
  override var output: File? = null
}
