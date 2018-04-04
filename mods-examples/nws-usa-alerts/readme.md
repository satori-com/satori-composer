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
./.out/install/nws-usa-alerts/bin/nws-usa-alerts
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
docker run --rm -ti mods-examples-nws-usa-alerts
```

##### run daemonized docker container
```
docker run --restart=always --log-opt max-size=64m --log-opt max-file=16 -d --name=nws-usa-alerts mods-examples-nws-usa-alerts
```

