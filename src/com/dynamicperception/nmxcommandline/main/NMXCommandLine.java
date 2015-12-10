package com.dynamicperception.nmxcommandline.main;

import java.util.ArrayList;
import java.util.List;

import com.dynamicperception.nmxcommandline.coms.Serial;
import com.dynamicperception.nmxcommandline.helpers.Console;
import com.dynamicperception.nmxcommandline.models.Command;
import com.dynamicperception.nmxcommandline.models.Command.Type;
import com.dynamicperception.nmxcommandline.models.NMXCmd;
import com.dynamicperception.nmxcommandline.models.NMXComs;

public class NMXCommandLine {
	
	private static boolean execute;
	private static Serial serial;
	final static String DELIMITER = " ";
	
	
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
		
		String[] testArgs = getArgs(input);		
		
		// Find the command type
		int endOfType = input.indexOf(" ");
		endOfType = endOfType == -1 ? input.length() : endOfType;
		String typeStr = input.substring(0, endOfType);
		
		// Request to quit
		if(testArgs[0].equals("exit")){
			quit();
		}
		// Request to repeat command
		else if(testArgs[0].equals("r")){			
			input = input.substring(2, input.length());
			int count = Integer.parseInt(input.substring(0, input.indexOf(DELIMITER)));			
			input = input.substring(input.indexOf(DELIMITER)+1, input.length());			
			for(int i = 0; i < count; i++){
				parseCommand(input);
			}			
			return;
		}
		// Find command name
		else if(testArgs[0].equals("find")){
			String type = testArgs[1];
			String term = testArgs[2];
			if(type.equals("g")){
				Command.Names.General.find(term);
				return;
			}
			else if(type.equals("m")){
				Command.Names.Motor.find(term);
				return;
			}
			else if(type.equals("c")){
				return;
			}
			else if(type.equals("k")){
				return;
			}
		}
		
		// Find the command
		String tmpCmd;
		try{
			tmpCmd = input.substring(endOfType+1, input.length());		
		}catch(StringIndexOutOfBoundsException e){
			// If there is no command, but there is a valid type, print the help for that type
			if(typeStr.equals("m")){
				Command.Names.Motor.help();
			}
			else if(typeStr.equals("g")){
				Command.Names.General.help();
			}
			// Otherwise, this was a garbage command, so ignore it
			return;
		}
		int endOfCmd = tmpCmd.indexOf(" ");
		endOfCmd = endOfCmd == -1 ? tmpCmd.length() : endOfCmd;
		String cmd = tmpCmd.substring(0, endOfCmd);
		
		if(endOfCmd == -1){			
			NMXCmd.execute(typeStr, cmd, new ArrayList<Integer>());
		}
		
		
		// Find the number of arguments
		tmpCmd = input.substring(endOfCmd+2, input.length());
		int argCount = input.length() - input.replace(" ", "").length();	
		
		// Extract the arguments
		List<Integer> args = new ArrayList<Integer>();
		for(int i = 0; i < argCount; i++){
			
			int firstIndex = tmpCmd.indexOf(DELIMITER) + 1;
			int secondIndex = i == argCount-1 ? tmpCmd.length() : tmpCmd.indexOf(DELIMITER, firstIndex);
			String arg = tmpCmd.substring(firstIndex, secondIndex);
			try{
				Integer argVal = Integer.parseInt(arg);
				args.add(argVal);				
				// Trim off the extracted argument
				tmpCmd = tmpCmd.substring(secondIndex, tmpCmd.length());
			}catch(NumberFormatException e){
				// There was an extra space. We're done here.				
				break;
			}
		}
		
		// Execute the command
		//NMXCmd.execute(typeStr, cmd, args);
		
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
			Command thisCommand = Command.get(type, cmd);
						
			if(args.size() == 1 && args.get(0) == -1){
				thisCommand.printInfo();			
				return;
			}
			
			if(args.size() == 0){
				thisCommand.execute();
			}
			else if(args.size() == 1){
				thisCommand.execute(args.get(0));
			}
			else if(args.size() == 2){
				thisCommand.execute(args.get(0), args.get(1));
			}			
		}catch(UnsupportedOperationException e){
			Console.pln("Not a valid command");
			return;
		}		
	}
	
	private static String[] getArgs(String input){
		int argCount = input.length() - input.replace(DELIMITER, "").length() + 1;
		String[] args = new String[argCount];
		for(int i = 0; i < argCount; i++){
			if(i == argCount-1){
				args[i] = input;
			}
			else{
				int index = input.indexOf(DELIMITER);
				args[i] = input.substring(0, index);
				input = input.substring(index + 1, input.length());
			}
		}
		return args;		
	}
	
	private static void quit(){
		if(Serial.isPortOpen())
			serial.closePort();
		Console.pln("Exiting application!");
		System.exit(0);
	}

}
