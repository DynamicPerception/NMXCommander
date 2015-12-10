package com.dynamicperception.nmxcommandline.main;

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
		parseCommand(getArgs(input));
	}

	public static void parseCommand(List<String> args){		
		
		
		String typeStr = "";
		String cmdStr = "";
		
		// Request to quit
		if(args.get(0).equals("exit")){
			quit();
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
			if(args.size() < 3){
				Console.pln("Invalid search syntax");
				return;
			}
		
			
			typeStr = args.get(1);
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
		// Help list
		else if(args.size()== 1){
			typeStr = args.get(0);
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
		else{
			typeStr = args.get(0);
			
			if(args.size() > 1)
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
	
	private static List<String> getArgs(String input){		
		int argCount = input.length() - input.replace(DELIMITER, "").length() + 1;
		List<String> args = new ArrayList<String>();
		for(int i = 0; i < argCount; i++){
			if(i == argCount-1){
				args.add(input);
			}
			else{
				int index = input.indexOf(DELIMITER);
				args.add(input.substring(0, index));
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
