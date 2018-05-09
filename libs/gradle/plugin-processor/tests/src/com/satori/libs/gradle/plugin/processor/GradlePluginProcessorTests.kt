package com.satori.libs.gradle.plugin.processor

import com.google.testing.compile.*
import org.junit.*

class GradlePluginProcessorTests {
  
  @Test
  fun basicTest() {
    JavaSourcesSubject.assertThat(
      JavaFileObjects.forResource("TransformPlugin.java")
    ).processedWith(
      GradlePluginProcessor()
    ).compilesWithoutError()/*.and().generatesFiles(
      JavaFileObjects.forResource("META-INF/gradle-plugins/com.satori.transform.properties")
    )*/
  }
  
}