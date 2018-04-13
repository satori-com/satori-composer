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

### Task: ProjectExec(ProjectExecTask), extends JavaExec 
Task to execute specified gradle project with provided args
#### methods
- forProject(project): specify project to execute
#### example
```gradle
task generateGtfsSchema(type: ProjectExec) {
  forProject ":codegen-pbuf2jschema"

  inputs.files pbufSchema
  outputs.files jsonSchema

  args '-schema', pbufSchema
  args '-out', jsonSchema

  doFirst {
    println "generating gtfs json schema..."
  }
}
```
### Extension: projectExec
Extension to execute specified gradle project with provided args
#### example
```gradle
task generateGtfsSchema {
  group "codegen"

  inputs.files pbufSchema
  outputs.files jsonSchema

  projectExec (":codegen-pbuf2jschema") {
    args '-schema', pbufSchema
    args '-out', jsonSchema
    println "generating gtfs json schema..."
  }
}
```

### Task: SourceSetExec(SourceSetExecTask), extends JavaExec 
Task to execute specified sourceSet with provided args
#### methods
- forSourceSet(sourceSet): specify sourceSet to execute
#### example
```gradle
task generateGraphqlClasses(type: SourceSetExec) {
  forSourceSet("codegen")
  
  inputs.file(schema)
  outputs.dir(out)

  sourceSets.main.java.srcDir out

  main = "${pckg}.App"

  args "-schema", schema
  args "-pckg", pckg
  args "-out", out

  doFirst {
    delete out
    println "generating code...."
  }

  clean.doFirst {
    delete out
  }
}
```
### Extension: sourceSetExec
Extension to execute specified sourceSet with provided args
#### example
```gradle
task generateGraphqlClasses {
  group "codegen"
  
  inputs.file(schema)
  outputs.dir(out)
  
  sourceSets.main.java.srcDir out

  sourceSetExec("codegen"){
    main = "${pckg}.App"
  
    args "-schema", schema
    args "-pckg", pckg
    args "-out", out
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