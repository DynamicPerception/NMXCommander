package com.dynamicperception.nmxcommandline.main;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.dynamicperception.nmxcommandline.coms.Serial;
import com.dynamicperception.nmxcommandline.helpers.Console;
import com.dynamicperception.nmxcommandline.models.Command;
import com.dynamicperception.nmxcommandline.models.Command.Type;
import com.dynamicperception.nmxcommandline.models.NMXComs;

public class NMXCommandLine {
	
	private static boolean execute;
	private static Serial serial;
	final static String DELIMITER = " ";	
	private static long lastTime;
	
	public static void main(String[] args) {
		serial = new Serial();
		
		Serial.checkPorts();
		Console.pln(Serial.list() + "\n");
		
		Console.p("Which serial port? ");
		int port = Console.getInteger() - 1;
		if(port < 0){
			quit();
		}
		serial.openPort(port);		
		NMXComs.setSerialObject(serial);
		
		execute = true;				
		while(execute){
			getCommand();
		}		
	}
	
	public static void getCommand(){
		Console.p("\nCmd: ");
		String cmd = Console.getString();			
		parseCommand(cmd);
	}
	
	public static void parseCommand(String input){
		parseCommand(getArgs(input, DELIMITER));
	}

	public static void parseCommand(List<String> args){		
		
		// Request to quit
		if(args.get(0).equals("exit")){
			quit();
		}
		// Skip if it's a comment line
		else if(args.get(0).indexOf("//") != -1){
			return;
		}
		// Run command list from file
		else if(args.get(0).equals("run")){
			try {
				runCommandFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else if(args.get(0).equals("runCSV")){			
			try {
				if(args.size() > 1)
					runCsvCommandFile(args.get(1));
				else
					runCsvCommandFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		// Request to repeat command
		else if(args.get(0).equals("r")){						
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
			findCommand(args);
			return;			
		}
		// Help list
		else if(args.size()== 1){
			getHelp(args);
			return;
		}
		// Normal Command
		else{
			System.out.println("Last command loaded time elapsed: " + (System.nanoTime() - lastTime));
			runCommand(args);
			lastTime = System.nanoTime();
		}
	}
	
	private static void runCommand(List<String> args){
		
		String typeStr = args.get(0);
		String cmdStr = "";
		
		if(args.size() > 1){
			cmdStr = args.get(1);
		}
		
		Command.Type type = null;
		if(typeStr.equals("g")){
			type = Type.GENERAL;
		}
		else if(typeStr.equals("m")){
			type = Type.MOTOR;
		}
		else if(typeStr.equals("c")){
			type = Type.CAMERA;
		}
		else if(typeStr.equals("k")){
			type = Type.KEYFRAME;
		}
		try{			
			Command thisCommand = Command.get(type, cmdStr);
						
			if(args.size() == 3 && args.get(2).equals("-h")){
				thisCommand.printInfo();			
				return;
			}
			
			if(args.size() == 2){				
				thisCommand.execute();
			}
			else if(args.size() == 3){				
				thisCommand.execute(args.get(2));
			}
			else if(args.size() == 4){				
				thisCommand.execute(args.get(2), args.get(3));
			}			
		}catch(UnsupportedOperationException e){
			Console.pln("Not a valid command");
			return;
		}	
	}
	
	private static void findCommand(List<String> args){
		if(args.size() < 3){
			Console.pln("Invalid search syntax");
			return;
		}		
		
		String typeStr = args.get(1);
		String term = args.get(2);
		if(typeStr.equals("g")){
			Command.Names.General.find(term);
			return;
		}
		else if(typeStr.equals("m")){
			Command.Names.Motor.find(term);
			return;
		}
		else if(typeStr.equals("c")){
			return;
		}
		else if(typeStr.equals("k")){
			return;
		}
		else{
			Console.pln("Invalid type");
			return;
		}
	}
	
	private static void getHelp(List<String> args){
		String typeStr = args.get(0);
		if(typeStr.equals("g")){
			Command.Names.General.help();
			return;
		}
		else if(typeStr.equals("m")){
			Command.Names.Motor.help();;
			return;
		}
		else if(typeStr.equals("c")){
			return;
		}
		else if(typeStr.equals("k")){
			return;
		}
	}
	
	private static List<String> getArgs(String input, String delimiter){		
		int argCount = input.length() - input.replace(delimiter, "").length() + 1;
		List<String> args = new ArrayList<String>();
		for(int i = 0; i < argCount; i++){
			if(i == argCount-1){
				args.add(input);
			}
			else{
				int index = input.indexOf(delimiter);
				args.add(input.substring(0, index));
				input = input.substring(index + 1, input.length());
			}
		}
		return args;		
	}
	
	private static void runCommandFile() throws IOException {
		Path path = Paths.get("c:/commandList.txt");
		
		// Get the list of commands
		final Charset ENCODING = StandardCharsets.UTF_8;
		List<String> commands = Files.readAllLines(path, ENCODING);
		
		// Run them
		for(int i = 0; i < commands.size(); i++){
			parseCommand(commands.get(i));
		}		
	}
	
	private static void runCsvCommandFile() throws IOException{
		runCsvCommandFile("");
	}
	
	private static void runCsvCommandFile(String which) throws IOException {
		Path path;
		
		if(which.equals(""))
			path = Paths.get("c:/commandLog.csv");
		else
			path = Paths.get("c:/commandLog_"+which+".csv");
		
		// Get the list of commands
		final Charset ENCODING = StandardCharsets.UTF_8;
		List<String> commands = Files.readAllLines(path, ENCODING);
		Console.pln("Commands found: " + commands.size());
		long startTime = System.currentTimeMillis();
		
		boolean ignore = false;
		long startIgnore = 0;
		long stopIgnore = 0;
		
		
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
			
			// Replace position queries with firmware queries
//			if(_command == 106){
//				continue;
//			}
			
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
	
	private static void quit(){
		if(Serial.isPortOpen())
			serial.closePort();
		Console.pln("Exiting application!");
		System.exit(0);
	}

}
