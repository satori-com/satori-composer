package com.satori.gradle.composer.plugin

import org.codehaus.groovy.runtime.*
import org.gradle.testfixtures.*
import org.junit.*

class ComposerPluginTests : Assert() {
  
  @Test
  public fun `validate project extentions`() {
    val project = ProjectBuilder.builder().build()
    project.pluginManager.apply("com.satori.composer")
    
    assertEquals(
      "com.satori:satori-composer:${MetaInfo.version}",
      InvokerHelper.invokeMethod(project, "composer", null)
    )
  }
}