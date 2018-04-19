[![Maven](https://img.shields.io/nexus/s/https/oss.sonatype.org/com.satori/satori-libs-gradle-docker.svg)](https://oss.sonatype.org/content/repositories/snapshots/com/satori/satori-libs-gradle-docker/0.5.56-SNAPSHOT/)

## Gradle plugin 'com.satori.transform' 

task wrappers for docker cli
https://docs.docker.com/engine/reference/commandline/cli/

### apply plugin example

```gradle
buildscript{
  repositories {
    mavenCentral()
    maven { url "https://oss.sonatype.org/content/repositories/snapshots" }
  }
  dependencies {
    classpath "com.satori:satori-libs-gradle-docker:0.5.56-SNAPSHOT"
  }
}
apply plugin: "com.satori.docker"
```

#### Examples

```gradle

task buildDockerImage(type: DockerBuildImageTask) {
  group 'docker'
  dependsOn 'installDist'

  imageName = project.name
  imageTag = project.version

  contextDir = installDist.destinationDir
  prepareContext {
    copy {
      from 'Dockerfile'
      into contextDir
    }
  }
  cleanupContext {
    //delete new File(contextDir, "Dockerfile")
  }
}

task runDockerContainer(type: DockerRunContainerTask) {
  group 'docker'
  dependsOn 'buildDockerImage'

  imageName = project.name
  containerName = project.name
}


task stopDockerContainer(type: DockerStopContainerTask) {
  group 'docker'

  containerName = project.name
}

task listDockerImages(type: DockerBaseTask) {
  group 'docker'

  doFirst {
    exec("images", "-a")
  }
}

```

### DockerBaseTask 
Provides common functionality for all docker tasks
```
<cmd> [-H <host>] [--tls] [--tlsverify] [--tlskey <tlsKey>] [--tlscert <tlsCert>] [--tlscacert <tlsCaCert>]
```

#### properties
| property   | type     | description                            |
|------------|----------|----------------------------------------|
| cmd        | string[] | docker executable, default ["docker"]  |
| host       | string?  | `--host <host>`                        |
| tls        | bool     | `--tls`                                |
| tlsVerify  | bool     | `--tlsverify`                          |
| tlsKey     | file?    | `--tlskey <tlsKey>`                    |
| tlsCert    | file?    | `--tlscert <tlsCert>`                  |
| tlsCaCert  | file?    | `--tlscacert <tlsCaCert>`              |
#### methods
- exec(String... args): executes docker command with specified arguments, 
for example `exec("images", "-qa")` will execute `<dockerCmd> images -qa`

### DockerBuildImageTask 
build docker image
```
<prepareContext>
<dockerCmd> build <buildArgs> -t <imageName:imageTag> <contextDir>
<dockerCmd> tag <imageName:imageTag> <imageName> <contextDir>
<clenupContext>
```
#### properties
| property      | type        | description                      |
|---------------|-------------|----------------------------------|
| imageName     | string?     | image name                       |
| imageTag      | string?     | image tag                        |
| buildArgs     | string[]    | additional build arguments       |
| contextDir    | file?       | build context dir                |
#### methods
- `prepareContext(Closure closure)`: runs closure before build in order to prepare context
- `cleanupContext(Closure closure)`: runs closure after build in order to cleanup context

### DockerRunContainerTask 
run docker container
```
<dockerCmd> rm -f <containerName>
<dockerCmd> run -d --name <containerName> <logOptions> [--restart=<restart>] <runArgs> <imageName> <cmdArgs>
<dockerCmd> ps --filter name=<containerName>
```
##### properties
| property      | type        | description                                             |
|---------------|-------------|---------------------------------------------------------|
| imageName     | string?     | image name                                              |
| containerName | string?     | container name                                          |
| restart       | string?     | `--restart=<restart>`, default "always"                 |
| logOptions    | LogOptions? | log options, default: {max-size: "32m",  max-file:"16"} |
| runArgs       | string[]    | additional run arguments                                |
| cmdArgs       | string[]    | command arguments to be passed to entry point           |

### DockerStopContainerTask 
stop docker container
```
<dockerCmd> rm -f <containerName>
<dockerCmd> ps --filter name=<containerName>
```
#### properties
| property      | type        | description      |
|---------------|-------------|------------------|
| containerName | string?     | container name   |


### Maven (snapshots)
```xml
<repository>
  <url>https://oss.sonatype.org/content/repositories/snapshots/</url>
</repository>
```
```xml
<dependency>
    <groupId>com.satori</groupId>
    <artifactId>satori-libs-gradle-docker</artifactId>
    <version>0.5.56-SNAPSHOT</version>
</dependency>
```
