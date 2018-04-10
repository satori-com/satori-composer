[![Maven](https://img.shields.io/nexus/s/https/oss.sonatype.org/com.satori/satori-libs-gradle-github.svg)](https://oss.sonatype.org/content/repositories/snapshots/com/satori/satori-libs-gradle-github/${project.version}/)
## Gradle plugin 'com.satori.github' 

<% if(!project.version.endsWith("-SNAPSHOT")) {%>
### Maven (releases)
```xml
<dependency>
    <groupId>com.satori</groupId>
    <artifactId>satori-libs-gradle-github</artifactId>
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
    <groupId>com.satori</groupId>
    <artifactId>satori-libs-gradle-github</artifactId>
    <version>${project.version}</version>
</dependency>
```
<% }%>