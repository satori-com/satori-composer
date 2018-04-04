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
./.out/install/barrier-queue/bin/barrier-queue
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
docker run --rm -ti mods-examples-barrier-queue
```

##### run daemonized docker container
```
docker run --restart=always --log-opt max-size=64m --log-opt max-file=16 -d --name=barrier-queue mods-examples-barrier-queue
```

