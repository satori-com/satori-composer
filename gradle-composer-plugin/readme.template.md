[![Maven](https://img.shields.io/nexus/s/https/oss.sonatype.org/com.satori/satori-${project.name}.svg)](https://oss.sonatype.org/content/repositories/snapshots/com/satori/satori-${project.name}/${project.version}/)

## Comoser Gradle plugin 

defines project extensions to simplify maintaining build system 

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
apply plugin: "com.satori.composer"
```

### project extensions

<% rootProject.publishingProjects.forEach { p->%>
- `satori${codeFormatter.pascal(p.name)}()` - returns `"${p.group}:satori-${p.name}:${p.version}"`
<% } %>
  
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