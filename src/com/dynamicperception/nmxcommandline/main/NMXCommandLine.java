package com.dynamicperception.nmxcommandline.main;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.dynamicperception.nmxcommandline.coms.Serial;
import com.dynamicperception.nmxcommandline.helpers.Console;
import com.dynamicperception.nmxcommandline.helpers.Consts;
import com.dynamicperception.nmxcommandline.models.Command;
import com.dynamicperception.nmxcommandline.models.Command.Names.General;
import com.dynamicperception.nmxcommandline.models.NMXComs;

public class NMXCommandLine {

    private static boolean execute;
    private static Serial  serial;
    final static String    DELIMITER    = " ";
    // private static long lastTime;
    private static String  version   = "0.4-beta";

    public static void main(String[] args) {
        // Create serial object
        serial = new Serial();
        NMXComs.setSerialObject(serial);
        Serial.populatePorts();

        // If arguments ares supplied upon execution, run only those, then quit
        if (args.length > 0) {
            oneTimeCommand(args);
        }

        // Get user to select port
        promptForPort();

        // Print help
        printHelp();

        /*
         * Send an arbitrary command to clear initial connection hiccup. Also
         * reroute system out to temporary file to avoid printing the error this
         * will likely generate.
         */
        try {
            PrintStream oldOut = System.out;
            File tempFile = new File("temp.txt");
            tempFile.createNewFile();
            PrintStream tempOut = new PrintStream(tempFile);
            System.setOut(tempOut);
            Command.execute(General.GET_FIRMWARE);
            System.setOut(oldOut);
            tempOut.close();
            tempFile.delete();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // Enter program loop
        execute = true;
        while (execute) {
            getCommand();
        }
    }

    /**
     * Prints help for starting application from the terminal
     */
    private static void printTerminalHelp() {
        Console.pln("\nIn order to enter the interactive command line tool, run this JAR file without arguments.\n"
                + "Alternatively, you may run it with the following arguments to execute a single NMX command and then\n"
                + "immediately disconnect from the controller and close this application:\n\n"
                + "\"NMXCommander.jar [PORT NAME] [COMMAND TYPE].[COMMAND NAME] [DATA or MOTOR # (if required)] [MOTOR DATA (if required)]\"\n\n"
                + "If you're not familiar with the command types, names, data, etc., open the application in interactive\n"
                + "mode first and read the extended help there.");
    }

    /**
     * Prints general application help
     */
    private static void printHelp() {
        Console.pln("\n\n******** NMX Commander " + full_version + " Overview ********\n\n"
                + "This command line tool allows you to manually send single instuctions to the NMX controller.\n"
                + "This is done by giving input with the following syntax:\n\n"
                + "Non-motor commands -- \"[COMMAND TYPE].[COMMAND NAME] [DATA (if required)]\"\n"
                + "Motor commands -- \"m.[COMMAND NAME] [MOTOR #] [DATA (if required)]\"\n\n"
                + "Non-motor command types include \"g\" (general), \"c\" (camera), and \"k\" (key frame)\n"
                + "When specifying the motor number for motor commands, counting starts at 0 (i.e. valid motor #s are 0, 1, 2)\n\n\n"
                + "******** NMX Commander Configuration Commands ********\n\n"
                + "* \"outputAddress [ADDR # optional argument]\" -- without optional parameter, reports the current command address. With the\n"
                + "optional parameter, sets the address to which the command will be sent. This is 3 by default and should not be changed unless\n"
                + "you have manually reassigned the address of one or more of your controllers. !!!Sending commands to an incorrect address will\n"
                + "cause you lots of headaches!!!\n\n"
                + "* \"commandDetail [0/1 optional argument]\" -- without optional parameter, reports whether commandDetail is enabled. With the\n"
                + "optional parameter, sets commandDetail enabled. If true, NMX Commander will print the command name and any additional data\n"
                + "output to the NMX\n\n"
                + "* \"serialDetail [0/1 optional argument]\" -- without optional parameter, reports whether serialDetail is enabled. With the\n"
                + "optional parameter, sets serialDetail enabled. If true, NMX Commander will print the raw packet received from the NMX for\n"
                + "each command\n\n\n"
                + "* \"responseTimout [TIMEOUT optional argument]\" -- without optional parameter, reports current response timeout in milliseconds\n"
                + "With optional parameter, sets timeout length. If you find that you encounter a situation where you are getting many timeouts that\n"
                + "are not causing problems (e.g. you don't care about the response contents, but the command seems to be executing properly), you\n"
                + "may want to shorten the timout in order to increase command throughput.\n\n\n"
                + "******** Other Useful Commands ********\n\n" + "* \"help\" -- prints this information again\n\n"
                + "* \"[COMMAND TYPE].[COMMAND NAME] -h\" -- prints command-specific help\n\n"
                + "* \"list [COMMAND TYPE]\" -- lists all commands of that type\n\n"
                + "* \"find [COMMAND TYPE].[SEARCH TERM]\" -- returns all commands of that type containing the search term\n\n"
                + "* \"repeat [N] [COMMAND TYPE].[COMMAND NAME]\" -- repeats the given command N times\n\n"
                + "* \"runMacro [PATH]\" -- runs a command macro list from a text file. Type \"runMacro\" without arguments\n"
                + "for macro file syntax\n\n" + "* \"exit\" -- closes the serial port and exits the application\n\n\n"
                + "******** Some Important Tips ********\n\n"
                + "* A boolean (true/false) value is indicated by a data value of 0 or 1. For example, the command to enable the camera\n"
                + "would be \"c.setEnable 1\".\n\n"
                + "* Valid microstep settings are 4, 8, and 16. The NMX does not use full or half steps in order to reduce vibation.\n\n"
                + "* The controller accepts and returns step counts that correspond to relevant motor's current microstep setting.\n"
                + "If the NMX reports a motor position of 500 when the microstep setting is 4, it will report 1000 when the microstep\n"
                + "setting is 8, and 2000 when the microstep setting is 16 (as long as the motor doesn't move). Same deal if you\n"
                + "command a motor to move somewhere. If you command a move of 10000 steps when in quarter stepping mode (i.e. microstep\n"
                + "setting = 4), it will go twice as far as when in eighth stepping mode (i.e. microstep setting = 8).\n"
                + "BE CAREFUL ABOUT THIS! If you accidentally send your rig two or four times as far as you intended, you can break\n"
                + "your valueable equipment!\n\n"
                + "* If you try to send a motor to a position and it either does not move or does not go all the way to where you sent it,\n"
                + "try running the command \"m.resetLimits [MOTOR #]\" to clear any end limits that may be restricting movement.");
    }

    /**
     * Starts the execution of a single command initiated as arguments passed to
     * the program at launch
     * 
     * @param args
     *            The arguments passed from the terminal upon program launch
     */
    private static void oneTimeCommand(String[] args) {
        if (args[0].toLowerCase().equals("help")) {
            printTerminalHelp();
            quit();
        }
        try {
            String portName = args[0];
            int port = -1;
            for (int i = 0; i < Serial.getPortList().size(); i++) {
                if (Serial.getPortList().get(i).equals(portName)) {
                    port = i;
                }
            }
            serial.openPort(port);
        } catch (RuntimeException e) {
            Console.pln("Invalid port! Either you picked the wrong number or you have the wrong syntax.\n"
                    + "Syntax"
                    + "A full one-time execution should look something like this: "
                    + "\"NMXCommander-" + version + ".jar COM32 m.sendTo 0 15000\"");
            quit();
        }

        List<String> commandArgs = new ArrayList<String>();
        for (int i = 1; i < args.length; i++) {
            commandArgs.add(args[i]);
        }
        parseCommand(commandArgs);
        quit();
    }

    /**
     * Displays a list of available ports and prompts user for selection
     */
    private static void promptForPort() {
        // Populate the serial port list and display it
        Serial.printPorts();
        Console.pln(Serial.list() + "\n");

        // Ask user to open serial port
        Console.p("Connect to NMX on which serial port? ");
        int port = Console.getInteger();

        // If an invalid port is supplied, try again
        if (port == Consts.ERROR) {
            promptForPort();
            return;
        }
        if (port < 0) {
            quit();
        }
        serial.openPort(port - 1);
    }

    /**
     * Retrieves input from the console and passes it to command parser
     */
    private static void getCommand() {
        Console.p("\nCmd: ");
        String cmd = Console.getString();
        parseCommand(cmd);
    }

    /**
     * Parses command into discreet arguments, then passes arguments to
     * overloaded parseCommand() that accepts a List of arguments.
     * 
     * @param input
     *            Command string
     */
    private static void parseCommand(String input) {
        parseCommand(getArgs(input, DELIMITER));
    }

    /**
     * Handles any application specific commands, then attempts to execute any
     * NMX commands
     * 
     * @param args
     *            A List of String arguments that comprise the command
     */
    private static void parseCommand(List<String> args) {

        if (args.get(0).trim().length() == 0) {
            // Ignore blank lines
            return;
        }
        // Request to quit
        else if (args.get(0).equals("exit")) {
            quit();
        }
        // Skip if it's a comment line
        else if (args.get(0).indexOf("//") != -1) {
            return;
        }
        else if (args.get(0).equals("help")) {
            printHelp();
        }
        else if (args.get(0).equals("commandDetail")) {
            commandDetail(args);
        }
        else if (args.get(0).equals("serialDetail")) {
            serialDetail(args);
        }
        else if (args.get(0).equals("outputAddress")) {
            outputAddress(args);
        }
        else if (args.get(0).equals("responseTimeout")) {
            responseTimeout(args);
        }
        // Run command list from file
        else if (args.get(0).equals("runMacro")) {
            runMacro(args);
            return;
        }
        // Replay a Graffik command log
        else if (args.get(0).equals("replayLog")) {
            replayLog(args);
            return;
        }
        // List request
        else if (args.get(0).toLowerCase().equals("list")) {
            listRequest(args);
            return;
        }
        // Repeat the following command n times. (e.g. "repeat 2 m.getMS 0")
        else if (args.get(0).equals("repeat")) {
            repeatCommand(args);
            return;
        }
        // Find command name
        else if (args.get(0).equals("find")) {
            findCommand(args);
            return;
        }
        // Manual packet command
        else if (args.get(0).equals("packet")) {
            manualPacketCommand(args);
        }
        // Normal Command
        else {
            runCommand(args);
        }
    }

    /**
     * Sets or prints the current command detail state. When enabled, the
     * command detail option is enabled, additional information about the
     * command, including specific data sent to the NMX will be printed when the
     * command is executed.
     * 
     * @param args
     *            A List of String arguments that comprise the command
     */
    static void commandDetail(List<String> args) {
        // If a second argument is present, set the detail state, otherwise
        // report the current state
        try {
            boolean enabled = args.get(1).equals("0") ? false : true;
            Command.setCommandDetail(enabled);
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Command detail enabled? : " + Command.getCommandDetail());
        }
    }

    /**
     * Sets or prints the current serial detail state. When enabled, the serial
     * detail option is enabled, the NMX's raw serial response to a command will
     * be printed. This may be helpful for debugging certain unexpected
     * behaviors in the NMX.
     * 
     * @param args
     *            A List of String arguments that comprise the command
     * 
     */
    static void serialDetail(List<String> args) {
        // If a second argument is present, set the detail state, otherwise
        // report the current state
        try {
            boolean enabled = args.get(1).equals("0") ? false : true;
            NMXComs.setSerialDetail(enabled);
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Serial detail enabled? : " + NMXComs.getSerialDetail());
        }
    }

    /**
     * Sets or prints the current NMX address that will be set in the outgoing
     * packet.
     * 
     * @param args
     *            A List of String arguments that comprise the command
     * 
     */
    static void outputAddress(List<String> args) {
        try {
            // If there's a second argument, then set the address
            int addr = Integer.parseInt(args.get(1));
            Command.setAddr(addr);
            // Otherwise print the current address
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Current command address: " + Command.getAddr());
            // Handle malformed input
        } catch (NumberFormatException e) {
            System.out.println("Invalid address. Must be an integer.");
        }
    }

    /**
     * Sets or prints the response timeout in milliseconds. Setting shorter
     * timeouts increases the command throughput by more quickly recognizing a
     * bad response and moving on from it, however setting it too short will
     * cause valid responses to be rejected. Conversely, setting too long of a
     * timeout will cause the communication between NMX Commander and the NMX to
     * be sluggish. packet.
     * 
     * @param args
     *            A List of String arguments that comprise the command
     * 
     */
    static void responseTimeout(List<String> args) {
        try {
            int timeout = Integer.parseInt(args.get(1));
            NMXComs.setResponseTimeout(timeout);
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Current timout in milliseconds: " + NMXComs.getResponseTimeout());
        } catch (NumberFormatException e) {
            System.out.println("Invalid address. Must be an integer.");
        }
    }

    /**
     * Parses a request to replay a recorded Graffik session. This is useful
     * primarily for debugging issues found during a Graffik session, since the
     * exact timing and sequence of commands can be replayed without running
     * through the Graffik interface.
     * 
     * @param args
     *            A List of String arguments that comprise the command. Argument
     *            in position 1 must be a path that points to a .csv file
     *            generated by Graffik in it's recording mode.
     * 
     */
    static void replayLog(List<String> args) {
        try {
            // If there is a second argument, use it as the log path
            if (args.size() > 1)
                runCsvCommandFile(Paths.get(args.get(1)));
            // Otherwise, use the default path
            else
                runCsvCommandFile();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * This method prints information about using the list command or a list of
     * all valid commands for a specific command sub-type.
     * 
     * @param args
     *            A List of String arguments that comprise the command.
     */
    static void listRequest(List<String> args) {
        // If there aren't enough arguments, print list instructions
        if (args.size() < 2) {
            Console.pln("List syntax -- \"list [COMMAND TYPE]\"");
            Console.pln("Example -> \"list c\" prints the list of valid camera commands");
            return;
        }
        // Otherwise
        Command.printList(Command.getType(args.get(1)));
    }

    /**
     * Repeats a particular command n times, where n is an integer in argument
     * position 1 and the command is a normal command occupying however many
     * subsequent argument positions are required. On bad input, instructions
     * for using the command are printed.
     * 
     * @param args
     *            A List of String arguments that comprise the command.
     */
    static void repeatCommand(List<String> args) {
        try {
            int count = Integer.parseInt(args.get(1));
            for (int i = 0; i < 2; i++) {
                args.remove(0);
            }
            for (int i = 0; i < count; i++) {
                parseCommand(args);
            }
        }
        // On bad input, print proper repeat command syntax
        catch (IndexOutOfBoundsException | NumberFormatException e) {
            Console.pln("Invalid syntax. Try \"repeat [#] [command]\"");
        }
    }

    /**
     * Executes a command directly from a manually constructed packet, bypassing
     * the Command class structure. This is useful for debugging and for testing
     * new commands that have not yet been added to the Command class name
     * structure.
     * 
     * @param args
     *            A List of String arguments that comprise the command.
     */
    static void manualPacketCommand(List<String> args) {
        if (args.size() < 6 || args.size() > 7) {
            Console.pln("Incorrect number of arguments");
            Console.pln("Example -> \"packet [ADDR] [SUBADDR] [CMD] [LENGTH] [DATA (optional)] [RESPONSE]\"");
            Console.pln("[ADDR], [SUBADDR], [CMD], and [LENGTH] are each a single byte with values\n"
                    + "from 0-255. If [LENGTH] != 0, [DATA] may be a byte, int, long, or float,\n"
                    + "depending on the command. If [LENGTH] == 0, do not include the [DATA]\n"
                    + "parameter. [RESPONSE] is a boolean value.");
        }
        else {
            List<Integer> intArgs = new ArrayList<>();
            for (int i = 1; i < args.size(); i++) {
                String arg = args.get(i);
                intArgs.add(Integer.parseInt(arg));
            }
            boolean response = intArgs.get(intArgs.size() - 1) == 0 ? false : true;
            try {
                // Enable printing of the raw response...
                boolean oldDetail = NMXComs.getSerialDetail();
                NMXComs.setSerialDetail(true);
                if (intArgs.size() == 5) {
                    NMXComs.cmd(intArgs.get(0), intArgs.get(1), intArgs.get(2), intArgs.get(3), 0, response);
                }
                else {
                    NMXComs.cmd(intArgs.get(0), intArgs.get(1), intArgs.get(2), intArgs.get(3), intArgs.get(4),
                            response);
                }
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                // ...but revert when done with the command
                NMXComs.setSerialDetail(oldDetail);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

    /**
     * Executes a specified command
     * 
     * @param args
     *            Command arguments as a list of String arguments with the
     *            following syntax: <br>
     *            [0] == "[COMMAND TYPE].[COMMAND NAME]" <br>
     *            [1] == "-h" for help OR "[COMMAND DATA]" or "[MOTOR
     *            NUMBER] (optional) <br>
     *            [2] == "[COMMAND DATA]"(if [1] == [MOTOR NUMBER]
     *            (optional))
     */
    private static void runCommand(List<String> args) {

        String cmdStr = "";

        cmdStr = args.get(0);

        try {
            Command thisCommand = Command.get(cmdStr);

            if (args.size() == 2 && args.get(1).equals("-h")) {
                thisCommand.help();
                return;
            }

            if (args.size() == 1) {
                Command.execute(cmdStr);
            }
            else if (args.size() == 2) {
                Command.execute(cmdStr, args.get(1));
            }
            else if (args.size() == 3) {
                Command.execute(cmdStr, args.get(1), args.get(2));
            }
        } catch (UnsupportedOperationException e) {
            Console.pln("Not a valid command");
            return;
        }
    }

    /**
     * Accepts a list of String arguments and finds commands that contain the
     * search term
     * 
     * @param args
     *            A list of String arguments. The search parameter must be in
     *            array location 1 with the following syntax:
     *            "<COMMAND TYPE>.<search term>"
     */
    private static void findCommand(List<String> args) {
        if (args.size() < 2) {
            Console.pln("Find syntax -- \"find [COMMAND TYPE].[SEARCH TERM]\"");
            Console.pln("Example -> \"find m.speed\" prints the following:");
            List<String> exampleArgs = Arrays.asList("find", "m.speed");
            findCommand(exampleArgs);
            return;
        }

        String term = args.get(1);
        Command.find(term);
    }

    /**
     * Accepts a string and separates it into a list of arguments
     * 
     * @param input
     *            A string with command arguments separated by a designated
     *            delimiter
     * @param delimiter
     *            The char that separates command arguments
     * @return A list of strings representing command arguments
     */
    private static List<String> getArgs(String input, String delimiter) {
        String[] argArray = input.split(delimiter);

        List<String> args = new ArrayList<String>();
        for (String arg : argArray) {
            args.add(arg);
        }
        return args;
    }

    /**
     * Runs a command macro file
     * 
     * @param path
     *            Path of the macro text file
     * @throws IOException
     */
    private static void runMacro(List<String> args) {

        try {
            if (args.size() == 1) {
                Console.pln("\nrunMacro syntax -- \"runMacro [PATH]\"");
                Console.pln("Example -> \"runMacro c:\\NMXmacro.txt\"\n");
                Console.pln("The text file should have one command on each line with the following syntax:\n\n"
                        + "\"[DELAY TIME] [COMMAND TYPE].[COMMAND NAME] [DATA or MOTOR # (if required)] [MOTOR DATA (if required)]\"\n\n"
                        + "The following example enables the camera, sets the focus time to 600ms, trigger time to 100ms, sets home\n"
                        + "for each of the motors (e.g. sets current position to 0), immediately takes an exposure, commands the motors to\n"
                        + "a new position, waits 5000ms, takes another exposure, waits 1000ms, commands the motors back to their original\n"
                        + "positions, waits 5000ms, then takes a final exposure:\n\n" + "0 c.setEnable 1\n"
                        + "0 c.setFocus 600 \n" + "0 c.setTrigger 100 \n" + "0 m.setHome 0\n" + "0 m.setHome 1\n"
                        + "0 m.setHome 2\n" + "0 c.expose\n" + "0 m.sendTo 0 15000\n" + "0 m.sendTo 1 2560\n"
                        + "0 m.sendTo 2 -5000\n" + "5000 c.expose\n" + "1000 m.sendTo 0 0\n" + "0 m.sendTo 1 0\n"
                        + "0 m.sendTo 2 0\n" + "5000 c.expose\n\n"
                        + "Lines in the macro file may be commented out by starting a line with \"//\"");
            }
            else if (args.size() > 1) {
                Path path = Paths.get(args.get(1));
                // Get the list of commands
                final Charset ENCODING = StandardCharsets.UTF_8;
                List<String> commands = Files.readAllLines(path, ENCODING);

                // Run them
                for (int i = 0; i < commands.size(); i++) {
                    List<String> commandArgs = getArgs(commands.get(i), DELIMITER);
                    // Skip blank or commented lines
                    if ((commandArgs.get(0).trim().length() == 0) || commandArgs.get(0).indexOf("//") >= 0) {
                        continue;
                    }
                    // Wait the requested delay
                    wait(Integer.parseInt(commandArgs.get(0)));
                    // Remove the delay time from the argument list
                    commandArgs.remove(0);
                    // Pass the remaining arguments to the command parser
                    parseCommand(commandArgs);
                }
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    /**
     * Replays a Graffik command log from the default location
     */
    private static void runCsvCommandFile() throws IOException {
        runCsvCommandFile(Paths.get("c:/commandLog.csv"));
    }

    /**
     * Replays a Graffik command log from the specified path
     */
    private static void runCsvCommandFile(Path path) throws IOException {

        // Get the list of commands
        final Charset ENCODING = StandardCharsets.UTF_8;
        List<String> commands = Files.readAllLines(path, ENCODING);
        Console.pln("Commands found: " + commands.size());
        long startTime = System.currentTimeMillis();

        boolean ignore = false;
        long startIgnore = 0;

        // Run them (skip command 0; that is the header)
        for (int i = 1; i < commands.size(); i++) {
            List<String> args = new ArrayList<String>();
            args = getArgs(commands.get(i), ",");

            // Ignore comment lines
            if (args.get(0).indexOf("//") >= 0) {
                continue;
            }
            else if (args.get(10).indexOf("*/") >= 0) {
                ignore = false;
                // Adjust the start time so we're not stuck waiting for the next
                // command
                startTime -= Long.parseLong(args.get(1)) - startIgnore;
                continue;
            }
            else if (args.get(0).indexOf("/*") >= 0) {
                ignore = true;
                startIgnore = Long.parseLong(args.get(1));
                continue;
            }

            if (ignore)
                continue;

            long time = Long.parseLong(args.get(1));
            int _address = Integer.parseInt(args.get(2));
            int _subAddr = Integer.parseInt(args.get(3));
            int _command = Integer.parseInt(args.get(4));
            int _length = Integer.parseInt(args.get(5));
            int _data = Integer.parseInt(args.get(6));
            boolean getResponse = Boolean.parseBoolean(args.get(7));

            int printWait = 5000;
            long lastPrint = System.currentTimeMillis();
            while (System.currentTimeMillis() - startTime < time) {
                // Wait till it's time to issue this command
                if (System.currentTimeMillis() - lastPrint > printWait) {
                    System.out.println("Time till next command: " + (time - (System.currentTimeMillis() - startTime)));
                    lastPrint = System.currentTimeMillis();
                }
            }
            try {
                System.out.println("Command #" + args.get(0) + " -- Name: " + Command.getCommandName(_subAddr, _command)
                        + " Data: " + _data);
                NMXComs.cmd(_address, _subAddr, _command, _length, _data, getResponse);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            // Check whether the controller has died
            int failureThreshold = 10;
            if (NMXComs.getEmptyResponseCount() > failureThreshold) {
                Console.pln("Lost communication with NMX after command #" + (i - failureThreshold - 1));
                System.exit(0);
            }
        }
    }

    /**
     * Waits a given amount of time and prints a countdown of time remaining
     * 
     * @param waitTime
     *            Time to wait in milliseconds
     */
    private static void wait(int waitTime) {
        long waitStart = System.currentTimeMillis();
        long lastUpdate = waitStart;
        int updateRate = 1000;
        while (System.currentTimeMillis() - waitStart < waitTime) {
            // If it's a long wait, let the user know how much time remains
            if (System.currentTimeMillis() - lastUpdate > updateRate) {
                Console.pln("Time till next command: " + (waitTime - (System.currentTimeMillis() - waitStart)) + " ms");
                lastUpdate = System.currentTimeMillis();
            }
        }
    }

    /**
     * Closes the serial port and quits the application
     */
    private static void quit() {
        if (Serial.isPortOpen())
            serial.closePort();
        // Console.pln("Exiting application!");
        System.exit(0);
    }

}
