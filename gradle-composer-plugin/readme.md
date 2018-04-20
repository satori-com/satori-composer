[![Maven](https://img.shields.io/nexus/s/https/oss.sonatype.org/com.satori/satori-gradle-composer-plugin.svg)](https://oss.sonatype.org/content/repositories/snapshots/com/satori/satori-gradle-composer-plugin/0.5.67-SNAPSHOT/)

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
    classpath "com.satori:satori-gradle-composer-plugin:0.5.67-SNAPSHOT"
  }
}
apply plugin: "com.satori.composer"
```

### project extensions


- `gradleComposerPlugin()` - returns `"com.satori:satori-gradle-composer-plugin:0.5.67-SNAPSHOT"`

- `composer()` - returns `"com.satori:satori-composer:0.5.67-SNAPSHOT"`

- `mods()` - returns `"com.satori:satori-mods:0.5.67-SNAPSHOT"`

- `modsSuite()` - returns `"com.satori:satori-mods-suite:0.5.67-SNAPSHOT"`

- `libsAsyncApi()` - returns `"com.satori:satori-libs-async-api:0.5.67-SNAPSHOT"`

- `libsAsyncCore()` - returns `"com.satori:satori-libs-async-core:0.5.67-SNAPSHOT"`

- `libsAsyncKotlin()` - returns `"com.satori:satori-libs-async-kotlin:0.5.67-SNAPSHOT"`

- `libsCommonKotlin()` - returns `"com.satori:satori-libs-common-kotlin:0.5.67-SNAPSHOT"`

- `libsVertxKotlin()` - returns `"com.satori:satori-libs-vertx-kotlin:0.5.67-SNAPSHOT"`

- `libsGtfs()` - returns `"com.satori:satori-libs-gtfs:0.5.67-SNAPSHOT"`

- `libsCompositionDrawer()` - returns `"com.satori:satori-libs-composition-drawer:0.5.67-SNAPSHOT"`

- `libsGradleUtils()` - returns `"com.satori:satori-libs-gradle-utils:0.5.67-SNAPSHOT"`

- `libsGradleTransform()` - returns `"com.satori:satori-libs-gradle-transform:0.5.67-SNAPSHOT"`

- `libsGradleGithub()` - returns `"com.satori:satori-libs-gradle-github:0.5.67-SNAPSHOT"`

- `libsGradleDocker()` - returns `"com.satori:satori-libs-gradle-docker:0.5.67-SNAPSHOT"`

- `libsGradleCodegen()` - returns `"com.satori:satori-libs-gradle-codegen:0.5.67-SNAPSHOT"`

- `codegenUtils()` - returns `"com.satori:satori-codegen-utils:0.5.67-SNAPSHOT"`

- `codegenCodemodelDsl()` - returns `"com.satori:satori-codegen-codemodel-dsl:0.5.67-SNAPSHOT"`

- `codegenMustacheBuilder()` - returns `"com.satori:satori-codegen-mustache-builder:0.5.67-SNAPSHOT"`

- `codegenYamlFileMerger()` - returns `"com.satori:satori-codegen-yaml-file-merger:0.5.67-SNAPSHOT"`

  

### Maven (snapshots)
```xml
<repository>
  <url>https://oss.sonatype.org/content/repositories/snapshots/</url>
</repository>
```
```xml
<dependency>
    <groupId>com.satori</groupId>
    <artifactId>satori-gradle-composer-plugin</artifactId>
    <version>0.5.67-SNAPSHOT</version>
</dependency>
```
