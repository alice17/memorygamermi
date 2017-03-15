# memorygamermi
###### Distributed memory game



For compiling the sources move to the `src` folder and enter this command from the command line:
```
$ javac *.java
```

Before running the compiled files make sure to have started the RMI registry service in the out folder:

On Linux:
```
$ rmiregistry &
```


For running the server:

Launch command inside /src directory

```
$ java -classpath $(PROJECT_DIR) -Djava.rmi.server.codebase=file:$(PROJECT_DIR)/src/ src.Server seconds

example:

$ java -classpath /User/memorygamermi/ -Djava.rmi.server.codebase=file:/User/memorygamermi/src/ src.Server 100
```



For running the client:

Launch command inside /src directory

```
$ java -classpath $(PROJECT_DIR)/ src.MainP
```
