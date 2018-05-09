[![Maven](https://img.shields.io/nexus/s/https/oss.sonatype.org/com.satori/satori-${project.name}.svg)](https://oss.sonatype.org/content/repositories/snapshots/com/satori/satori-${project.name}/${project.version}/)
## GradlePlugin annotation processor 

generates `META-INF/gradle-plugins/<pulgin-name>.properties` files for classes annotated with `GradlePlugin`  

<%
def annotationsProject=project(":libs-gradle-plugin-annotations")
def processorProject=project(":libs-gradle-plugin-processor")
%>

### kotlin example
*TransformPlugin.kt:*
```kotlin
package com.satori.libs.gradle.transform

import com.satori.libs.gradle.plugin.annotations.*
import org.gradle.api.*

@GradlePlugin(TransformPlugin.name)
open class TransformPlugin : Plugin<Project> {
  override fun apply(project: Project) {
    val conv = TransformPluginConvention(project)
    project.convention.plugins[name] = conv
  }
  companion object {
    const val name = "com.satori.transform"
  }
}
```

*build.gradle:*
```gradle
apply plugin: "kotlin-kapt"

kapt {
  generateStubs = true
  correctErrorTypes = true
}

dependencies {
  compileOnly "${annotationsProject.group}:satori-${annotationsProject.name}:${annotationsProject.version}"
  kapt "${processorProject.group}:satori-${processorProject.name}:${processorProject.version}"
}
```

<% if(!project.version.endsWith("-SNAPSHOT")) {%>
### Maven (releases)
```xml
<dependency>
    <groupId>${project.group}</groupId>
    <artifactId>satori-${project.name}</artifactId>
    <version>${project.version}</version>
</dependency>
```
<% } else {%>
### Maven (snapshots)
```xml
<repository>
  <url>https://oss.sonatype.org/content/repositories/snapshots/</url>
</repository>
```
```xml
<dependency>
    <groupId>${project.group}</groupId>
    <artifactId>satori-${project.name}</artifactId>
    <version>${project.version}</version>
</dependency>
```
<% }%>