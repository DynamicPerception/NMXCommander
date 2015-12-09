package com.dynamicperception.nmxcommandline.models;
import java.util.concurrent.TimeUnit;
import com.dynamicperception.nmxcommandline.helpers.Consts;

public class Command {
	
	private static int addr = 3;
	private static boolean debug = true;
	
	private String name;	
	private Type type;
	private int subaddr;
	private int command;
	private int dataLength;	
	private Class<?> returnType;
	
	static enum Type{
		GENERAL, MOTOR, CAMERA, KEYFRAME;
	}
	
	/* Constructor and Initialization Method */
	
	// Commands that transmit one or more data bytes
	Command(String name, Command.Type type, int command, int dataLength, Class<?> returnType){
		this.init(name, type, command, dataLength, returnType);
	}
	
	// Commands that transmit no additional data
	Command(String name, Command.Type type, int command, Class<?> returnType){
		this.init(name, type, command, 0, returnType);
	}	
	
	private void init(String name, Command.Type type, int command, int dataLength, Class<?> returnType){
		this.name = name;
		this.type = type;
		this.command = command;			
		this.returnType = returnType;
		this.dataLength = dataLength;	
		if(type == Command.Type.GENERAL){
			this.subaddr = 0;
		}	
		else if(type == Command.Type.MOTOR){
			this.subaddr = 1;
		}
		else if(type == Command.Type.CAMERA){
			this.subaddr = 4;			
		}
		else if(type == Command.Type.KEYFRAME){
			this.subaddr = 5;
		}
	}

	
	/* Static Methods */
	
	public static void setDebug(boolean debug){
		Command.debug = debug;
	}
	public static void setAddr(int addr){
		Command.addr = addr;
	}	
	
	
	/* Non-Static Methods */
	
	public String getName(){
		return name;
	}
	
	public <T>T execute(){
		if(this.type == Command.Type.MOTOR){
			System.out.println("This is a motor command; the motor number must be specified to execute");			
			throw new UnsupportedOperationException();
		}
		else{
			return execute(this.subaddr, 0, false);			
		}
	}
	
	public <T>T execute(int dataOrMotor){
		if(this.type == Command.Type.MOTOR){
			int tempSubaddr = dataOrMotor + 1;
			return execute(tempSubaddr, 0, false);
		}
		else{
			return execute(this.subaddr, dataOrMotor, true);
		}
	}
	
	public <T>T execute(int motor, int data){
		if(this.type == Command.Type.MOTOR){
			int tempSubaddr = motor + 1;
			return execute(tempSubaddr, data, true);
		}	
		else{
			System.out.println("This is a non-motor command; a motor number may not be specified");			
			throw new UnsupportedOperationException();				
		} 
	}
	
	@SuppressWarnings("unchecked")
	private <T>T execute(int subaddr, int data, boolean hasData){
		
		// Notify if data is attached to a command that does not take additional data
		if(dataLength == 0 && hasData){			
			System.out.println("This command does not send additional data");			
			throw new UnsupportedOperationException();
		}
		
		// Wait for the NMX to clear
		waitForNMX();	
		
		// Send the command to the NMX
		if(hasData){
			NMXComs.cmd(addr, subaddr, command, dataLength, data);
		}
		else{
			NMXComs.cmd(addr, subaddr, command);			
		}
		
		// Cast the return value to the proper response type
		T ret = null;
		if(returnType == Integer.class){
			ret = (T) returnType.cast(NMXComs.getResponseVal());
		}
		else if(returnType == Float.class){
			ret = (T) returnType.cast((float) NMXComs.getResponseVal() / Consts.FLOAT_CONVERSION);
		}
		else if(returnType == Boolean.class){
			ret = (T) returnType.cast(NMXComs.getResponseVal() == 0 ? false : true);			
		}
		// Void return type
		else{			
			ret = (T) Void.class.cast(ret);
		}
		
		// Print debug if necessary
		if(debug)
			System.out.println("Command: " + this.name + " Respons Val: " + ret);
		
		// Return the value
		return ret;
	}
	
	private static void waitForNMX(){
		// If a command is currently being sent, wait
		while(NMXComs.isSendingCommand()){	
			try {
				TimeUnit.MILLISECONDS.sleep(50);
			} catch (InterruptedException e) {
				System.out.println("Interrupted while sending command to NMX");
				e.printStackTrace();
			}
		}		
	}	

}
