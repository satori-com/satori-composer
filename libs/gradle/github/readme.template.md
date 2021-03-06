[![Maven](https://img.shields.io/nexus/s/https/oss.sonatype.org/com.satori/satori-libs-gradle-github.svg)](https://oss.sonatype.org/content/repositories/snapshots/com/satori/satori-libs-gradle-github/${project.version}/)
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
    classpath "com.satori:satori-libs-gradle-github:${project.version}"
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