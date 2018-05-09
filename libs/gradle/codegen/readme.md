[![Maven](https://img.shields.io/nexus/s/https/oss.sonatype.org/com.satori/satori-libs-gradle-codegen.svg)](https://oss.sonatype.org/content/repositories/snapshots/com/satori/satori-libs-gradle-codegen/0.5.75-SNAPSHOT/)

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
    classpath "com.satori:satori-libs-gradle-codegen:0.5.75-SNAPSHOT"
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

  main = "com.satori.libs.gradle.codegen.App"

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
    main = "com.satori.libs.gradle.codegen.App"
  
    args "-schema", schema
    args "-pckg", pckg
    args "-out", out
  }

}
```

### Extension: codeFormatter
provides various code formatting methods, see [ICodeFormatter](codegen/utils/src/com/satori/codegen/utils/ICodeFormatter.kt)
#### example
```gradle
codeFormatter.camel(project.name)
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
    <artifactId>satori-libs-gradle-codegen</artifactId>
    <version>0.5.75-SNAPSHOT</version>
</dependency>
```
