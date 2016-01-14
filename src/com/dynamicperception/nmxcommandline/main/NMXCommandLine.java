package com.dynamicperception.nmxcommandline.main;

import java.io.IOException;
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
import com.dynamicperception.nmxcommandline.models.Command.Type;
import com.dynamicperception.nmxcommandline.models.NMXComs;

public class NMXCommandLine {
	
	private static boolean execute;
	private static Serial serial;
	final static String DELIMITER = " ";	
	private static long lastTime;
	
	
	public static void main(String[] args) {
		// Create serial object
		serial = new Serial();
		NMXComs.setSerialObject(serial);
	
		// If arguments ares supplied upon execution, run only those, then quit
		if(args.length > 0){
			try{
				serial.openPort(args[0]);
			}catch(RuntimeException e){
				Console.pln("Invalid port! Either you picked the wrong number or you have the wrong syntax.\n"
						+ "A full one-time execution should look something like this: "
						+ "\"java -jar NMXCmd.jar COM32 m.sendTo 0 15000\"");
				quit();
			}
			
			List <String> commandArgs = new ArrayList<String>();
			for(int i = 1; i < args.length; i++){				
				commandArgs.add(args[i]);
			}
			parseCommand(commandArgs);			
			quit();
		}
		
		// Get user to select port
		promptForPort();
		
		// Print help
		printHelp();
		
		// Enter program loop
		execute = true;				
		while(execute){
			getCommand();
		}		
	}
	
	private static void printHelp(){
		Console.pln("\nThis command line tool allows you to manually send single instuctions to the NMX controller.\n"
				+ "This is done by giving input with the following syntax:\n\n"
				+ "Non-motor commands -- \"<command type>.<command name> <data (if required)>\"\n"
				+ "Motor commands -- \"m.<command name> <motor #> <data (if required)>\"\n\n"
				+ "Non-motor command types include \"g\" (general), \"c\" (camera), and \"k\" (key frame)\n"
				+ "When specifying the motor number for motor commands, counting starts at 0 (i.e. valid motor #s are 0, 1, 2)\n\n"
				+ "Here are some other useful commands:\n"
				+ "\"<command type>.<command name>\" -h -- prints command-specific help\n"
				+ "\"help\" -- prints this information again\n"
				+ "\"list <command type>\" -- lists all commands of that type\n"
				+ "\"find <command type>.<search term>\" -- returns all commands of that type containing the search term\n"
				+ "\"exit\" -- closes the serial port and exits the application\n");				
	}
	
	/**
	 * Displays a list of available ports and prompts user for selection
	 */
	private static void promptForPort(){
		// Population the serial port list and display it
		Serial.checkPorts();
		Console.pln(Serial.list() + "\n");
		
		// Ask user to open serial port
		Console.p("Which serial port? ");
		int port = Console.getInteger();
		
		// If an invalid port is supplied, try again		
		if(port == Consts.ERROR){
			promptForPort();
			return;
		}
		if(port < 0){
			quit();
		}
		serial.openPort(port-1 );		
	}
	
	/**
	 * Retrieves input from the console and passes it to command parser
	 */
	public static void getCommand(){
		Console.p("\nCmd: ");
		String cmd = Console.getString();			
		parseCommand(cmd);
	}
	
	/**
	 * Parses command into discreet arguments, then passes arguments to
	 * overloaded parseCommand() that accepts a List of arguments.
	 * @param input Command string
	 */
	public static void parseCommand(String input){
		parseCommand(getArgs(input, DELIMITER));
	}

	/**
	 * Handles any application specific commands, then attempts to execute any NMX commands
	 * @param args A List of String arguments that comprise the command
	 */
	public static void parseCommand(List<String> args){		
		
		// Request to quit
		if(args.get(0).equals("exit")){
			quit();
		}		
		// Skip if it's a comment line
		else if(args.get(0).indexOf("//") != -1){
			return;
		}
		else if(args.get(0).equals("help")){
			printHelp();
		}
		// Run command list from file
		else if(args.get(0).equals("runMacro")){
			try {
				if(args.size() == 1)
					runCommandFile(Paths.get("c:/commandList.txt"));
				else if(args.size() > 1){
					runCommandFile(Paths.get(args.get(1)));
				}				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return;
		}
		// Replay a Graffik command log
		else if(args.get(0).equals("replayLog")){			
			try {
				// If there is a second argument, use it as the log path
				if(args.size() > 1)
					runCsvCommandFile(Paths.get(args.get(1)));
				// Otherwise, use the default path
				else
					runCsvCommandFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return;
		}
		// Help request
		else if(args.get(0).toLowerCase().equals("list")){
			if(args.size() < 2){
				Console.pln("List syntax -- \"list <command type>\"");
				Console.pln("Example -> \"list c\" prints the list of valid camera commands");
				return;
			}				
			Command.printList(Command.getType(args.get(1)));
			return;
		}
		// Repeat the following command n times. (e.g. "repeat 2 m.getMS 0")
		else if(args.get(0).equals("repeat")){						
			int count = Integer.parseInt(args.get(1));			
			for(int i = 0; i < 2; i++){
				args.remove(0);
			}
			for(int i = 0; i < count; i++){
				parseCommand(args);
			}			
			return;
		}
		// Find command name
		else if(args.get(0).equals("find")){
			if(args.size() < 2){
				Console.pln("Find syntax -- \"find <command type>.<search string>\"");
				Console.pln("Example -> \"find m.speed\" prints the following:");
				List <String> exampleArgs = Arrays.asList("find", "m.speed");				
				findCommand(exampleArgs);				
			}
			else{
				findCommand(args);
			}
			return;			
		}		
		// Normal Command
		else{
			//System.out.println("Last command loaded time elapsed: " + (System.nanoTime() - lastTime));
			runCommand(args);
			lastTime = System.nanoTime();
		}
	}
	
	/**
	 * Executes a specified command 
	 * @param args Command arguments as a list of String arguments with the following syntax:
	 * <br>[0] == "&lt<b>command type</b>&gt.&lt<b>command name</b>&gt"
	 * <br>[1] == "<b>-h</b>"  for help OR "&lt<b>command data</b>&gt" OR "&lt<b>motor number</b>&gt (optional)
	 * <br>[2] == "&lt<b>command data</b>&gt"(if [1] == &lt<b>motor number</b>&gt (optional))  
	 */
	private static void runCommand(List<String> args){
		
		String cmdStr = "";
				
		cmdStr = args.get(0);				
		
		try{			
			Command thisCommand = Command.get(cmdStr);
						
			if(args.size() == 2 && args.get(1).equals("-h")){
				thisCommand.help();		
				return;
			}
			
			if(args.size() == 1){				
				thisCommand.executeThis();
			}
			else if(args.size() == 2){				
				thisCommand.executeThis(args.get(1));
			}
			else if(args.size() == 3){				
				thisCommand.executeThis(args.get(1), args.get(2));
			}			
		}catch(UnsupportedOperationException e){
			Console.pln("Not a valid command");
			return;
		}	
	}
	
	/**
	 * Accepts a list of String arguments and finds commands that contain the search term 
	 * @param args A list of String arguments. The search parameter must be
	 * in array location 1 with the following syntax: "<command type>.<search term>"
	 */
	private static void findCommand(List<String> args){
		if(args.size() < 2){
			Console.pln("Invalid search syntax");
			return;
		}
		
		String term = args.get(1);
		Command.find(term);		
	}
	
	/**
	 * Accepts a string and separates it into a list of arguments
	 * @param input A string with command arguments separated by a designated delimiter
	 * @param delimiter The char that separates command arguments
	 * @return A list of strings representing command arguments
	 */
	private static List<String> getArgs(String input, String delimiter){		
		String[] argArray = input.split(delimiter);
		
		List<String> args = new ArrayList<String>();
		for(String arg : argArray){
			args.add(arg);
		}
		return args;		
	}
	
	/**
	 * Runs a command macro file
	 * @param path Path of the macro text file
	 * @throws IOException
	 */
	private static void runCommandFile(Path path) throws IOException {		
		
		// Get the list of commands
		final Charset ENCODING = StandardCharsets.UTF_8;
		List<String> commands = Files.readAllLines(path, ENCODING);
		
		// Run them
		for(int i = 0; i < commands.size(); i++){
			parseCommand(commands.get(i));
		}		
	}
	
	/**
	 * Replays a Graffik command log from the default location
	 */
	private static void runCsvCommandFile() throws IOException{		
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
		for(int i = 1; i < commands.size(); i++){			
			List<String> args = new ArrayList<String>();			
			args = getArgs(commands.get(i), ",");
			
			// Ignore comment lines
			if(args.get(0).indexOf("//") >= 0){
				continue;
			}
			else if(args.get(10).indexOf("*/") >= 0){
				ignore = false;				
				// Adjust the start time so we're not stuck waiting for the next command
				startTime -= Long.parseLong(args.get(1)) - startIgnore;				
				continue;
			}
			else if(args.get(0).indexOf("/*") >= 0){
				ignore = true;				
				startIgnore = Long.parseLong(args.get(1));
				continue;
			}
			
			if(ignore)
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
			while(System.currentTimeMillis() - startTime < time){				
				// Wait till it's time to issue this command
				if(System.currentTimeMillis() - lastPrint > printWait){
					System.out.println("Time till next command: " + (time - (System.currentTimeMillis() - startTime)));
					lastPrint = System.currentTimeMillis();
				}
			}			
			try {
				System.out.println("Command #" + args.get(0) + " -- Name: " + Command.getCommandName(_subAddr, _command) + " Data: " + _data);				
				NMXComs.cmd(_address, _subAddr, _command, _length, _data, getResponse);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			// Check whether the controller has died
			int failureThreshold = 10;
			if(NMXComs.getEmptyResponseCount() > failureThreshold){
				Console.pln("Lost communication with NMX after command #" + (i-failureThreshold-1));
				System.exit(0);				
			}
		}		
	}
	
	/**
	 * Closes the serial port and quits the application
	 */
	private static void quit(){
		if(Serial.isPortOpen())
			serial.closePort();
		//Console.pln("Exiting application!");
		System.exit(0);
	}

}
