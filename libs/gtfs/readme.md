## Satori Gtfs Library For Real-Time
Requirements: java 1.8<br/>


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
    <version>0.4.1-SNAPSHOT</version>
</dependency>
```


### Download
[satori-libs-gtfs.v0.4.1-SNAPSHOT.zip](https://github.com/satori-com/satori-composer/releases/download/v0.4.1-SNAPSHOT/satori-libs-gtfs.v0.4.1-SNAPSHOT.zip)<br/>
[or see latest releases](https://github.com/satori-com/satori-composer/releases/latest)

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

