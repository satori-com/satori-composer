## Satori Gtfs Library For Real-Time
#####Requirements: java 1.8
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
    <artifactId>satori-${project.name}</artifactId>
    <version>${project.version}</version>
</dependency>
```
<% }%>

### Download
[satori-${project.name}.v${project.version}.zip](https://github.com${rootProject.githubRepo}/releases/download/v${project.version}/satori-${project.name}.v${project.version}.zip)<br/>
[or see latest releases](https://github.com${rootProject.githubRepo}/releases/latest)

### Examples

read gtfs binary protobuf as json serializable class (jackson):
```java
GtfsFeedMessage feed = GtfsProtoBufConverter.convert(
  GtfsRealtime.FeedMessage.parseFrom(binaryData)
);
```

serialize gtfs feed object as json string (jackson):
```java
mapper.writeValueAsString(gtfsFeedMessage)
```

