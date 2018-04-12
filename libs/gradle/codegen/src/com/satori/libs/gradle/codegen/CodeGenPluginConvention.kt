package com.satori.libs.gradle.codegen

import org.gradle.api.*

open class CodeGenPluginConvention(val project: Project) {
  val ProjectExec = ProjectExecTask::class.java
}