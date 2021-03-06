[![Maven](https://img.shields.io/nexus/s/https/oss.sonatype.org/com.satori/satori-gradle-composer-plugin.svg)](https://oss.sonatype.org/content/repositories/snapshots/com/satori/satori-gradle-composer-plugin/0.5.82-SNAPSHOT/)

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
    classpath "com.satori:satori-gradle-composer-plugin:0.5.82-SNAPSHOT"
  }
}
apply plugin: "com.satori.composer"
```

### project extensions


- `satoriGradleComposerPlugin()` - returns `"com.satori:satori-gradle-composer-plugin:0.5.82-SNAPSHOT"`

- `satoriComposer()` - returns `"com.satori:satori-composer:0.5.82-SNAPSHOT"`

- `satoriMods()` - returns `"com.satori:satori-mods:0.5.82-SNAPSHOT"`

- `satoriModsSuite()` - returns `"com.satori:satori-mods-suite:0.5.82-SNAPSHOT"`

- `satoriLibsAsyncApi()` - returns `"com.satori:satori-libs-async-api:0.5.82-SNAPSHOT"`

- `satoriLibsAsyncCore()` - returns `"com.satori:satori-libs-async-core:0.5.82-SNAPSHOT"`

- `satoriLibsAsyncKotlin()` - returns `"com.satori:satori-libs-async-kotlin:0.5.82-SNAPSHOT"`

- `satoriLibsCommonKotlin()` - returns `"com.satori:satori-libs-common-kotlin:0.5.82-SNAPSHOT"`

- `satoriLibsVertxKotlin()` - returns `"com.satori:satori-libs-vertx-kotlin:0.5.82-SNAPSHOT"`

- `satoriLibsGtfs()` - returns `"com.satori:satori-libs-gtfs:0.5.82-SNAPSHOT"`

- `satoriLibsCompositionDrawer()` - returns `"com.satori:satori-libs-composition-drawer:0.5.82-SNAPSHOT"`

- `satoriLibsTestlib()` - returns `"com.satori:satori-libs-testlib:0.5.82-SNAPSHOT"`

- `satoriLibsGradleUtils()` - returns `"com.satori:satori-libs-gradle-utils:0.5.82-SNAPSHOT"`

- `satoriLibsGradleTransform()` - returns `"com.satori:satori-libs-gradle-transform:0.5.82-SNAPSHOT"`

- `satoriLibsGradleGithub()` - returns `"com.satori:satori-libs-gradle-github:0.5.82-SNAPSHOT"`

- `satoriLibsGradleDocker()` - returns `"com.satori:satori-libs-gradle-docker:0.5.82-SNAPSHOT"`

- `satoriLibsGradleCodegen()` - returns `"com.satori:satori-libs-gradle-codegen:0.5.82-SNAPSHOT"`

- `satoriLibsGradlePluginAnnotations()` - returns `"com.satori:satori-libs-gradle-plugin-annotations:0.5.82-SNAPSHOT"`

- `satoriLibsGradlePluginProcessor()` - returns `"com.satori:satori-libs-gradle-plugin-processor:0.5.82-SNAPSHOT"`

- `satoriCodegenUtils()` - returns `"com.satori:satori-codegen-utils:0.5.82-SNAPSHOT"`

- `satoriCodegenCodemodelDsl()` - returns `"com.satori:satori-codegen-codemodel-dsl:0.5.82-SNAPSHOT"`

- `satoriCodegenMustacheBuilder()` - returns `"com.satori:satori-codegen-mustache-builder:0.5.82-SNAPSHOT"`

- `satoriCodegenYamlFileMerger()` - returns `"com.satori:satori-codegen-yaml-file-merger:0.5.82-SNAPSHOT"`

  

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
    <version>0.5.82-SNAPSHOT</version>
</dependency>
```
