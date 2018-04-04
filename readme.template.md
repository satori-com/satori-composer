## Satori Composer (preview)
[![Build Status](https://travis-ci.org/satori-com/satori-composer.svg?branch=dev)](https://travis-ci.org/satori-com/satori-composer)
[![Build Status](https://img.shields.io/nexus/s/https/oss.sonatype.org/com.satori/satori-composer.svg)](https://oss.sonatype.org/content/repositories/snapshots/com/satori/satori-composer/${project.version}/)

#### Example
![diagram](docs/files/big-blue-bus-composition.png)
```yaml
<%= new File("mods-examples/big-blue-bus/res/com/satori/mods/resources/config.json").text %>
```

### Documentation
- [Overview](https://www.satori.com/docs/opensource/composer#overview)
- [Examples](https://www.satori.com/docs/opensource/composer#examples)
- [Mods Suite](https://www.satori.com/docs/opensource/composer#mods-suite)
- [Building and Running](https://www.satori.com/docs/opensource/composer#building-and-running)
- [Async support library](docs/async/readme.md)

### Common Libraries
- [Gtfs Library For Real-Time](https://github.com/satori-com/satori-composer/tree/dev/libs/gtfs)

<% if(!project.version.endsWith("-SNAPSHOT")) {%>
### Maven (releases)
```xml
<dependency>
    <groupId>com.satori</groupId>
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
    <groupId>com.satori</groupId>
    <artifactId>satori-composer</artifactId>
    <version>${project.version}</version>
</dependency>
```
<% }%>

### Download
[Latest release](https://github.com/satori-com/satori-composer/releases/latest)

#### Requirements
java 1.8


