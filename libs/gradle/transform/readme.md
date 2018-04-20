[![Maven](https://img.shields.io/nexus/s/https/oss.sonatype.org/com.satori/satori-libs-gradle-transform.svg)](https://oss.sonatype.org/content/repositories/snapshots/com/satori/satori-libs-gradle-transform/0.5.64-SNAPSHOT/)
## Gradle plugin 'com.satori.transform' 

gradle task wrappers around groovy [GStringTemplateEngine](http://docs.groovy-lang.org/next/html/documentation/template-engines.html#_gstringtemplateengine)

### apply plugin example

```gradle
buildscript{
  repositories {
    mavenCentral()
    maven { url "https://oss.sonatype.org/content/repositories/snapshots" }
  }
  dependencies {
    classpath "com.satori:satori-libs-gradle-docker:0.5.64-SNAPSHOT"
  }
}
apply plugin: "com.satori.docker"
```

#### TransformTask example
```gradle
task generateReadme(type: TransformTask) {
  group 'codegen'
  template = file('readme.template.md')
  output = file('readme.md')
}
```

#### 'transform' extension example
```gradle
task generateReadme {
  group 'codegen'
  doFirst {
    println "transforming 'readme.template.md'..."
    transform {
      template = file('readme.template.md')
      output = file('readme.md')
    }
  }
}
```

#### TransformTask properties
| property   | type                 | description                            |
|------------|----------------------|----------------------------------------|
| template   | file                 | template file                          |
| output     | file                 | transformed file                       |
| model      | map<string, object>  | space for extra properties             |


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
    <version>0.5.64-SNAPSHOT</version>
</dependency>
```
