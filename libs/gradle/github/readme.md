[![Maven](https://img.shields.io/nexus/s/https/oss.sonatype.org/com.satori/satori-libs-gradle-github.svg)](https://oss.sonatype.org/content/repositories/snapshots/com/satori/satori-libs-gradle-github/0.5.80-SNAPSHOT/)
## 'github' gradle plugin 
tasks for management github releases using [Github Releases API v3](https://developer.github.com/v3/repos/releases/)

### apply plugin example
```gradle
buildscript{
  repositories {
    mavenCentral()
    maven { url "https://oss.sonatype.org/content/repositories/snapshots" }
  }
  dependencies {
    classpath "com.satori:satori-libs-gradle-github:0.5.80-SNAPSHOT"
  }
}
apply plugin: "com.satori.github"
```

#### Examples
```gradle
task githubListReleases(type: GitHubListReleasesTask) {
  url githubUrl
  authToken project.properties.get("githubAuthToken")
}

task githubPublishRelease(type: GitHubPublishReleaseTask) {
  url githubUrl
  authToken project.properties.get("githubAuthToken")
  
  asset jar
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
    <artifactId>satori-libs-gradle-github</artifactId>
    <version>0.5.80-SNAPSHOT</version>
</dependency>
```
