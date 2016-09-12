# NMX Commander
An easy-to-use command line interface for the Dynamic Perception NMX 3-Axis Motor Controller

#### Interactive Command Line Tool

Download and run the JAR file in the "runnable_jar" folder to run the application as an interactive command line tool. After selecting on which serial port to connect to the NMX, an introductory help message will explain how to construct commands in the terminal.

In order to run the jar file from the command line, navigate to the directory in which you've placed "NMXCommander-[version #].jar" file and use the following command: "java -jar NMXCommander-[version #].jar". For Windows users, this assumes that your PATH evironment variable already contains the location of your current Java installation. See https://www.java.com/en/download/help/path.xml for more help setting your the PATH environment variable.

#### Single Command Tool via Arguments

Alternatively, the JAR file can be run with arguments to run a single NMX command, then immediately close the serial port and close the application. Run the JAR file with the argument "help" for details.

###### Dependent Libraries
* Make sure to add the .jar files found in "libraries" folder to \lib\ext in your local Java JRE folder.
