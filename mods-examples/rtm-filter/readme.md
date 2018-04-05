<!-- generated, do not modify -->
## 'rtm filter' example
![diagram](docs/files/diagram.png)

##### build app
```
gradle installDist
```

##### run app using gradle
```
gradle run
```

##### run app using command line
```
./.out/install/rtm-filter/bin/rtm-filter
```

##### build docker image
```
gradle buildDockerImage
```

##### run interactive docker container using gradle
```
gradle runDockerContainer
```

##### run interactive docker container using command line
```
docker run --rm -ti mods-examples-rtm-filter
```

##### run daemonized docker container
```
docker run --restart=always --log-opt max-size=64m --log-opt max-file=16 -d --name=rtm-filter mods-examples-rtm-filter
```

