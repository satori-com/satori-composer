## Gradle plugin 'com.satori.transform' 

gradle task wrappers around groovy GStringTemplateEngine

#### Example
```gradle
buildscript{
  repositories {
    mavenCentral()
    maven { url "https://oss.sonatype.org/content/repositories/snapshots"}
  }
  dependencies {
    classpath "com.satori:satori-libs-gradle-transform:0.5.30-SNAPSHOT"
  }
}
apply plugin: "com.satori.transform"

task generateReadme(type: TransformTask) {
  group 'codegen'
  template = file('readme.template.md')
  output = file('readme.md')
}
```

apply plugin: "com.satori.github"
apply plugin: "com.satori.composition.drawer"


### Maven (snapshots)
```xml
<repository>
  <url>https://oss.sonatype.org/content/repositories/snapshots/</url>
</repository>
```
```xml
<dependency>
    <groupId>com.satori</groupId>
    <artifactId>satori-libs-gradle-transform</artifactId>
    <version>0.5.30-SNAPSHOT</version>
</dependency>
```
