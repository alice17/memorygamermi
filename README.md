# memorygamermi
###### Distributed memory game



For compiling the sources move to the `src` folder and enter this command from the command line:
```
$ javac -d ../out *.java
```

Before running the compiled files make sure to have started the RMI registry service in the `out` folder:

On Linux:
```
$ rmiregistry &
```

For running the server, where `$(PROJECT_DIR)` must be replaced with the path of the project's directory:

```
$ java -classpath $(PROJECT_DIR)/out/ -Djava.rmi.server.codebase=$(PROJECT_DIR)/out/ -Djava.security.policy=file:$(PROJECT_DIR)/src/server.policy Server
```

For running the client: 
```
$ java -classpath $(PROJECT_DIR)/out/ Client
```
