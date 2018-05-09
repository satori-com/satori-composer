[![Maven](https://img.shields.io/nexus/s/https/oss.sonatype.org/com.satori/satori-libs-gradle-plugin-annotations.svg)](https://oss.sonatype.org/content/repositories/snapshots/com/satori/satori-libs-gradle-plugin-annotations/0.5.75-SNAPSHOT/)
## GradlePlugin annotation processor 

generates `META-INF/gradle-plugins/<pulgin-name>.properties` files for classes annotated with `GradlePlugin`  



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
  compileOnly "com.satori:satori-libs-gradle-plugin-annotations:0.5.75-SNAPSHOT"
  kapt "com.satori:satori-libs-gradle-plugin-processor:0.5.75-SNAPSHOT"
}
```


### Maven (snapshots)
```xml
<repository>
  <url>https://oss.sonatype.org/content/repositories/snapshots/</url>
</repository>
```
```xml
<dependency>
    <groupId>com.satori</groupId>
    <artifactId>satori-libs-gradle-plugin-annotations</artifactId>
    <version>0.5.75-SNAPSHOT</version>
</dependency>
```
