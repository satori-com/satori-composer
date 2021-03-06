package com.satori.libs.gradle.transform

import org.codehaus.groovy.runtime.*
import org.gradle.api.plugins.*
import org.gradle.testfixtures.*
import org.gradle.testkit.runner.*
import org.junit.*
import org.junit.rules.*
import java.io.*

class TransformPluginTests : Assert() {
  
  @Test
  public fun `validate project extentions`() {
    val project = ProjectBuilder.builder().build()
    project.pluginManager.apply("com.satori.transform")
    
    assertSame(
      TransformTask::class.java,
      InvokerHelper.getProperty(project, "TransformTask")
    )
  }
}