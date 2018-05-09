## Satori Composer (preview)
[![Build Status](https://travis-ci.org/satori-com/satori-composer.svg?branch=dev)](https://travis-ci.org/satori-com/satori-composer)
[![Maven](https://img.shields.io/nexus/s/https/oss.sonatype.org/com.satori/satori-composer.svg)](https://oss.sonatype.org/content/repositories/snapshots/com/satori/satori-composer/0.5.75-SNAPSHOT/)

#### Example
![diagram](mods-examples/big-blue-bus/docs/files/diagram.png)
```yaml
{
  "stats": {
    "period": 1000, // in ms., 1 sec.
    "console": {
      "period": 10000
    }
  },
  "mods": {
    "positions": {
      "type": "http-poll",
      "settings": {
        "delay": 1000, // in ms., 1 sec.
        "format": "binary",
        "host": "gtfs.bigbluebus.com",
        "ssl": true,
        "verify-host": false,
        "path": "/vehiclepositions.bin"
      }
    },
    "trips": {
      "type": "http-poll",
      "settings": {
        "delay": 1000, // in ms., 1 sec.
        "format": "binary",
        "host": "gtfs.bigbluebus.com",
        "ssl": true,
        "verify-host": false,
        "path": "/tripupdates.bin"
      }
    },
    "converter": {
      "type": "gtfs-proto-buf-to-json",
      "connectors": ["positions", "trips"],
      "settings": {
        "user-data": "auckland"
      }
    },
    "unwrapper": {
      "type": "array-unwrap",
      "connectors": "converter",
      "settings": "/entity"
    },
    "dedup": {
      "type": "dedup",
      "connectors": "unwrapper",
      "settings": {
        "expiration-interval": 600000,
        "override": false
        //"key-selector": "/entity"
      }
    },
    "printer": {
      "type": "printer",
      "connectors": "dedup"
    }
  }
}

```

### Documentation
- [Overview](https://www.satori.com/docs/opensource/composer#overview)
- [Examples](https://www.satori.com/docs/opensource/composer#examples)
- [Mods Suite](https://www.satori.com/docs/opensource/composer#mods-suite)
- [Building and Running](https://www.satori.com/docs/opensource/composer#building-and-running)
- [Async support library](docs/async/readme.md)
- [Composition diagram drawer](libs/composition-drawer/readme.md)
- [Composer gradle plugin](gradle-composer-plugin/readme.md)
- Examples:  
  - [barrier](mods-examples/barrier) 
  - [barrier queue](mods-examples/barrier-queue) 
  - [big blue bus](mods-examples/big-blue-bus) 
  - [clock](mods-examples/clock) 
  - [nws usa alerts](mods-examples/nws-usa-alerts) 
  - [queue](mods-examples/queue) 
  - [rtm filter](mods-examples/rtm-filter)
### Common gradle plugins
- [transform](libs/gradle/transform/readme.md)
- [docker](libs/gradle/docker/readme.md)
- [github](libs/gradle/github)
- [codegen](libs/gradle/codegen)
### Other Common Libraries
- [Gtfs Library For Real-Time](https://github.com/satori-com/satori-composer/tree/dev/libs/gtfs)


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
    <version>0.5.75-SNAPSHOT</version>
</dependency>
```


### Download
[Latest release](https://github.com/satori-com/satori-composer/releases/latest)

#### Requirements
java 1.8


