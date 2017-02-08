# memorygamermi
###### Distributed memory game

For compiling the sources:
```
javac -d ../out *.java
```

Before running the compiled files make sure to have started the RMI registry service in the out folder:

On Linux:
```
$ rmiregistry &
```

For running the server:

```
java -classpath $(PROJECT_DIR)/out/ -Djava.rmi.server.codebase=$(PROJECT_DIR)/out/ -Djava.security.policy=file:$(PROJECT_DIR)/src/server.policy Server
```

For running the client: 
```
java -classpath $(PROJECT_DIR)/out/ Client
```
