## Satori Gtfs Library For Real-Time

<% if(!project.version.endsWith("-SNAPSHOT")) {%>
### Maven (releases)
```xml
<dependency>
    <groupId>${project.group}</groupId>
    <artifactId>satori-composer</artifactId>
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
    <artifactId>${project.name}</artifactId>
    <version>${project.version}</version>
</dependency>
```
<% }%>

### Download
[satori-mods.v0.3.2-SNAPSHOT.zip](https://github.com${rootProject.githubRepo}/releases/download/v${project.version}/${project.name}.v${project.version}.zip)<br/>
[or see latest releases](https://github.com${rootProject.githubRepo}/releases/latest)

#### Requirements
java 1.8


