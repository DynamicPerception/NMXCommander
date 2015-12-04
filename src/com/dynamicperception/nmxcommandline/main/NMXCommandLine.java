package com.dynamicperception.nmxcommandline.main;

import com.dynamicperception.nmxcommandline.helpers.Console;

public class NMXCommandLine {

	private static boolean execute;
	
	public static void main(String[] args) {
		execute = true;
		
		while(execute){
			Console.p("Cmd: ");
			String cmd = Console.getString();
			Console.pln("");
			parseCommand(cmd);
			Console.pln("Done!");
		}		
	}
	
	public static void parseCommand(String input){
		
		final String DELIMITER = "=";
		
		// Find the command type
		int endOfType = input.indexOf(" ");
		String type = input.substring(0, endOfType);
		Console.pln("Type found: " + type);
		
		// Find the command
		String tmpCmd = input.substring(endOfType+1, input.length());
		int endOfCmd = tmpCmd.indexOf(" ");
		String cmd = tmpCmd.substring(0, endOfCmd);
		Console.pln("Command found: " + type);
		
		boolean searching = true;
		tmpCmd = input.substring(endOfCmd+1, input.length());
		Console.pln(tmpCmd);
		int argCount = 0;
		
		// Find the number of arguments
		while(searching){			
			int i = tmpCmd.indexOf(DELIMITER);			
			Console.pln("Index found: " + i);
			if(i == -1){
				// Reset the substring
				tmpCmd = input.substring(endOfType, input.length());
				Console.pln("Done searching -- arg count: " + argCount);
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
		float[] args = new float[argCount];
		for(int i = 0; i < argCount; i++){
			
			int firstIndex = tmpCmd.indexOf(DELIMITER) + 1;
			int secondIndex = i == argCount-1 ? tmpCmd.length() : tmpCmd.indexOf(DELIMITER, firstIndex);
			String arg = tmpCmd.substring(firstIndex, secondIndex);
			Console.pln("Argument found: " + arg);
			args[i] = Float.parseFloat(arg);
			
			// Trim off the extracted argument
			tmpCmd = tmpCmd.substring(secondIndex, tmpCmd.length());
			Console.pln(tmpCmd);
		}
	}
	
	
}
