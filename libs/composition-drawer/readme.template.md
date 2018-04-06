## Composition diagram drawer

Generate diagram image for composer configuration

### Options

| command line   | gradle task  | description                                                  |
|----------------|--------------|--------------------------------------------------------------|
| --cfg-path     | cfgPath      |  path to composer config file                                |
| --img-path     | imgPath      |  path to image file to generate                              |
| --img-format   | imgFormat    |  image format, one of: "png", "jpg", "gif". default is "png" |
| --block-width  | blockWidth   |  block width. default is 260                                 |
| --block-height | blockHeight  |  block height. default is 40                                 |

#### Example using dedicated 'GenerateCompositionDiagram' gradle task
```gradle
buildscript{
  repositories {
    mavenCentral()
    maven { url "https://oss.sonatype.org/content/repositories/snapshots"}
  }
  dependencies {
    classpath "com.satori:satori-libs-composition-drawer:${project.version}"
  }
}

task generateCompositionDiagram(type: com.satori.libs.composition.drawer.GenerateCompositionDiagramTask) {
  group "codegen"
  
  cfgPath = file("path/to/config.json")
  imgPath = file("path/to/diagram.png")
  blockWidth = 230
  blockHeight = 45
}
```

#### Example using gradle plugin 'com.satori.composition.drawer'
```gradle
buildscript{
  repositories {
    mavenCentral()
    maven {url "https://oss.sonatype.org/content/repositories/snapshots"}
  }
  dependencies {
    classpath "com.satori:satori-libs-composition-drawer:${project.version}"
  }
}
apply plugin: "com.satori.composition.drawer"

task generateCompositionDiagram(type: GenerateCompositionDiagramTask) {
  group "codegen"
  
  cfgPath = file("path/to/config.json")
  imgPath = file("path/to/diagram.png")
  blockWidth = 230
  blockHeight = 45
}
```

### Example using 'JavaExec' gradle task
```gradle
repositories {
  mavenCentral()
  maven {url "https://oss.sonatype.org/content/repositories/snapshots"}
}
  
configurations{compositionDrawer}
dependencies {
  compositionDrawer "com.satori:satori-libs-composition-drawer:${project.version}"
}

task generateCompositionDiagram(type: JavaExec) {
  group "codegen"

  def cfgPath = file("path/to/config.json")
  def imgPath = file("path/to/diagram.png")

  inputs.file(cfgPath)
  outputs.file(imgPath)

  classpath = configurations.compositionDrawer
  main = 'com.satori.libs.composition.drawer.App'

  args "--cfg-path", cfgPath
  args "--img-path", imgPath
  args "--block-width", 230
  args "--block-height", 45

  doFirst {
    println "generating composition diagram ...."
    println commandLine.join(" ")
  }
}
```

<% if(!project.version.endsWith("-SNAPSHOT")) {%>
### Maven (releases)
```xml
<dependency>
    <groupId>com.satori</groupId>
    <artifactId>satori-libs-composition-drawer</artifactId>
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
    <groupId>com.satori</groupId>
    <artifactId>satori-libs-composition-drawer</artifactId>
    <version>${project.version}</version>
</dependency>
```
<% }%>

### Example of generated diagram
input config:

```yaml
<%= rootProject.file("mods-examples/big-blue-bus/res/com/satori/mods/resources/config.json").text %>
```
generated diagram:

![diagram](../../docs/files/big-blue-bus-composition.png)

