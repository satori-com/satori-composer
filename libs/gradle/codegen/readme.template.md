[![Maven](https://img.shields.io/nexus/s/https/oss.sonatype.org/com.satori/satori-${project.name}.svg)](https://oss.sonatype.org/content/repositories/snapshots/com/satori/satori-${project.name}/${project.version}/)

## 'codegen' gradle plugin 

common utilities for code generation needs

### apply plugin example

```gradle
buildscript{
  repositories {
    mavenCentral()
    maven { url "https://oss.sonatype.org/content/repositories/snapshots" }
  }
  dependencies {
    classpath "com.satori:satori-${project.name}:${project.version}"
  }
}
apply plugin: "com.satori.codegen"
```

### ProjectExec gradle task, extends JavaExec 
allow to execute specified gradle project with provided args
#### methods
- forProject(project): specify project to execute
#### example
```gradle
task generateGtfsSchema2(type: ProjectExec) {
  forProject ':codegen-pbuf2jschema'

  inputs.files pbufSchema
  outputs.files jsonSchema

  args '-schema', pbufSchema
  args '-out', jsonSchema

  doFirst {
    println "generating gtfs json schema..."
  }
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