package com.satori.libs.gradle.codegen

import com.satori.libs.gradle.utils.*
import org.codehaus.groovy.runtime.*
import org.gradle.internal.installation.*
import org.gradle.testfixtures.*
import org.gradle.tooling.*
import org.junit.*
import org.junit.rules.*
import java.io.*

class CodeGenPluginTests : Assert() {
  
  @Test
  public fun `validate project extentions`() {
    val project = ProjectBuilder.builder().build()
    project.pluginManager.apply("com.satori.codegen")
    
    assertSame(
      ProjectExecTask::class.java,
      InvokerHelper.getProperty(project, "ProjectExec")
    )
  
    assertSame(
      SourceSetExecTask::class.java,
      InvokerHelper.getProperty(project, "SourceSetExec")
    )
  
    assertSame(
      project.convention.getPlugin("codegen"),
      project.convention.getPlugin<CodeGenPluginConvention>()
    )
    
  }
}