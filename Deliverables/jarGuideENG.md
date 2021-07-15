# USING JAR GUIDE. [ENG]
Both jars, server and client, requires Java12 version.

### SERVER
Normal start
The normal start of the server jar is launched by writing the following command on the console:

java -jar [server jar path]
	
#### Start with persistence mode

In addition to the command above:
The server must know the path of the save file and the loading file about the old game:

| Command | Description | Sintax |
|:-----------------------|:------------------|:------------------:|
| load | Select path of the game to load | -load | 
| save | Select path to save the game | -save | 

Examples:

-save C:\Users\user\Desktop\save.json

-load C:\Users\user\Desktop\load.json


It is possible to load a game and then not save it, for example:

java -jar server.jar -load C:\Users\user\Desktop\load.json


In the same way it is possible to save a game without restoring one:

java -jar server.jar -save C:\Users\user\Desktop\save.json
In this case the program will take care of creating the save file in that path.


### CLIENT

The normal start of the client jar is launched by typing the following command on the console:

java --module-path [PATH JAVA FX] --add-modules=javafx.controls -jar  [CLIENT PATH]


In this case the user will be asked if he wants to use socket or rmi and if he wants to use the cli or the gui.


If you want to specify one or more of the configuration parameters from the command line, here is a table with some examples:

| Command  | Description                              | Argument    | Default       |
|:-----------------------|:------------------|:------------------:|:-------------|
| -ui      | Selects user's interface                    | [cli\gui]     | -             |
| -com    | Selects communication protocol                         | [soc\rmi]     | -             |
| -ip      | Selects server ip                              | [server ip]   | localhost     |
| -sp      | Selects server socket port                         | [port]        | 12345         |
| -rp      | Selects server RMI port                                | [port]        | 12333         |


Example:
--module-path [PATH JAVA FX] --add-modules=javafx.controls -jar client.jar -ui cli -com soc -ip 127.0.0.1 -sp 12345 -rp 12333                                         


Example: recommended configuration if Client and Server are on the same machine:

java --module-path [PATH JAVA FX] --add-modules=javafx.controls -jar client.jar -ui cli -com soc -ip localhost

Example: using GUI

java --module-path [PATH JAVA FX] --add-modules=javafx.controls -jar client.jar -ui gui -ip localhost
