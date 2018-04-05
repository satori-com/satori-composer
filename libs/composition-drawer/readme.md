## Composition diagram drawer

Generate diagram image for composer configuration

### Options

| command line   | gradle task  | description                                                  |
|----------------|--------------|--------------------------------------------------------------|
| --cfg-path     | cfgPath      |  path to composer config file                                |
| --img-path     | imgPath      |  path to image file to generate                              |
| --img-format   | imgFormat    |  image format, one of: "png", "jpg", "gif". default is "png" |
| --block-width  | blockWidth   |  block width. default is 260                                 |
| --block-height | blockHeight  |  block height. default is 40                                 |

#### Example using dedicated 'GenerateCompositionDiagram' gradle task
```gradle
buildscript{
  repositories {
    mavenCentral()
    maven {
      url('https://oss.sonatype.org/content/repositories/snapshots/')
    }
  }
  dependencies {
    classpath "com.satori:satori-libs-composition-drawer:0.5.14-SNAPSHOT"
  }
}

task generateCompositionDiagram(type: GenerateCompositionDiagramTask) {
  group "codegen"
  
  cfgPath = file("path/to/config.json")
  imgPath = file("path/to/diagram.png")
  blockWidth = 230
  blockHeight = 45
}
```

### Example using 'JavaExec' gradle task
```gradle
repositories {
  mavenCentral()
  maven {
    url('https://oss.sonatype.org/content/repositories/snapshots/')
  }
}
  
configurations{compositionDrawer}
dependencies {
  compositionDrawer "com.satori:satori-libs-composition-drawer:0.5.14-SNAPSHOT"
}

task generateCompositionDiagram(type: JavaExec) {
  group "codegen"

  def cfgPath = file("path/to/config.json")
  def imgPath = file("path/to/diagram.png")

  inputs.file(cfgPath)
  outputs.file(imgPath)

  classpath = configurations.compositionDrawer
  main = 'com.satori.libs.composition.drawer.App'

  args "--cfg-path", cfgPath
  args "--img-path", imgPath
  args "--block-width", 230
  args "--block-height", 45

  doFirst {
    println "generating composition diagram ...."
    println commandLine.join(" ")
  }
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
    <artifactId>satori-libs-composition-drawer</artifactId>
    <version>0.5.14-SNAPSHOT</version>
</dependency>
```


### Example of generated diagram
input config:

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
generated diagram:

![diagram](../../docs/files/big-blue-bus-composition.png)
