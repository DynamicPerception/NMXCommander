package com.dynamicperception.nmxcommandline.main;

import java.util.ArrayList;
import java.util.List;

import com.dynamicperception.nmxcommandline.coms.Serial;
import com.dynamicperception.nmxcommandline.helpers.Console;
import com.dynamicperception.nmxcommandline.models.NMXCmd;
import com.dynamicperception.nmxcommandline.models.NMXComs;

public class NMXCommandLine {

	private static boolean execute;
	private static Serial serial;
	
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
		
		final String DELIMITER = " ";
		
		// Find the command type
		int endOfType = input.indexOf(" ");
		endOfType = endOfType == -1 ? input.length() : endOfType;
		String type = input.substring(0, endOfType);
		
		if(type.equals("exit")){
			quit();
		}
		
		// Find the command
		String tmpCmd;
		try{
			tmpCmd = input.substring(endOfType+1, input.length());		
		}catch(StringIndexOutOfBoundsException e){
			// If there is no command, but there is a valid type, print the help for that type
			if(type.equals("m")){
				NMXCmd.Motor.Names.help();
			}
			else if(type.equals("g")){
				NMXCmd.General.Names.help();
			}
			// Otherwise, this was a garbage command, so ignore it
			return;
		}
		int endOfCmd = tmpCmd.indexOf(" ");
		endOfCmd = endOfCmd == -1 ? tmpCmd.length() : endOfCmd;
		String cmd = tmpCmd.substring(0, endOfCmd);
		
		if(endOfCmd == -1){			
			NMXCmd.execute(type, cmd, new ArrayList<Integer>());
		}
		
		
		// Find the number of arguments
		boolean searching = true;
		tmpCmd = input.substring(endOfCmd+2, input.length());
		int argCount = 0;		

		while(searching){			
			int i = tmpCmd.indexOf(DELIMITER);			
			if(i == -1){
				// Reset the substring
				tmpCmd = input.substring(endOfCmd+2, input.length());
				searching = false;				
			}
			else{
				// Add to the argument count
				argCount++;				
				// Trim the string
				tmpCmd = tmpCmd.substring(i+1, tmpCmd.length());				
			}
		}
		
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
		NMXCmd.execute(type, cmd, args);
	}
	
	private static void quit(){
		if(Serial.isPortOpen())
			serial.closePort();
		Console.pln("Exiting application!");
		System.exit(0);
	}
	
}
