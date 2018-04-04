<!-- generated, do not modify -->
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
./.out/install/big-blue-bus/bin/big-blue-bus
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
docker run --rm -ti mods-examples-big-blue-bus
```

##### run daemonized docker container
```
docker run --restart=always --log-opt max-size=64m --log-opt max-file=16 -d --name=big-blue-bus mods-examples-big-blue-bus
```

