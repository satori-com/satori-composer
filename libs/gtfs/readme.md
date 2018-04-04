## Satori Gtfs Library For Real-Time
Requirements: java 1.8<br/>

classes are generated based on [gtfs-realtime.proto](https://developers.google.com/transit/gtfs-realtime/gtfs-realtime-proto) schema for jackson json library


### Maven (snapshots)
```xml
<repository>
  <url>https://oss.sonatype.org/content/repositories/snapshots/</url>
</repository>
```
```xml
<dependency>
    <groupId>com.satori</groupId>
    <artifactId>satori-libs-gtfs</artifactId>
    <version>0.5.11-SNAPSHOT</version>
</dependency>
```


### Download
[satori-libs-gtfs.v0.5.11-SNAPSHOT.zip](https://github.com/satori-com/satori-composer/releases/download/v0.5.11-SNAPSHOT/satori-libs-gtfs.v0.5.11-SNAPSHOT.zip)<br/>
[or see latest releases](https://github.com/satori-com/satori-composer/releases/latest)

### Examples

read gtfs binary protobuf as json serializable class (jackson):
```java
GtfsFeedMessage gtfsFeedMessage = GtfsProtoBufConverter.convert(
  GtfsRealtime.FeedMessage.parseFrom(binaryData)
);
```

serialize gtfs feed object as json string (jackson):
```java
mapper.writeValueAsString(gtfsFeedMessage)
```

### Gradle tasks for code generation

- **generateProto** - generate classes to parse protobuf from spec
- **generateGtfsSchema** - generate json schema from protobuf spec
- **generateGtfsTypes** - generate classes for json schema
- **generateGtfsConverter** - generate converter from protobuf parser to json types
