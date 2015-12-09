package com.dynamicperception.nmxcommandline.models;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.dynamicperception.nmxcommandline.helpers.Consts;

public class Command {
		
	private static int addr = 3;
	private static boolean debug = false;
	private static List<Command> commandList = new ArrayList<Command>();
	
	private static boolean listInitialized = false;
	private String name;	
	private Type type;
	private int subaddr;
	private int command;
	private int dataLength;	
	private Class<?> returnType;
	
	public static class Names{
		public static class General {
 
			// Commands
			static final String START_PROGRAM						= "start";
			static final String PAUSE_PROGRAM						= "pause";			
			static final String STOP_PROGRAM			 			= "stop	";
			static final String TOGGLE_LED	 						= "toggleLed";
			static final String SET_TIMING_MASTER 					= "setTimingMaster";
			static final String SET_NAME 							= "setName";			
			static final String SET_ADDRESS 						= "setAddress";
			static final String SET_COMMON_LINE 	 				= "setCommonLine";
			static final String SEND_ALL_MOTORS_HOME 				= "sendAllHome";
			static final String SET_MAX_STEP_RATE					= "setMaxStepRate";
			
			static final String SET_INPUT_EDGE						= "setInputEdge";
			static final String SET_ALT_IO_MODE						= "setAltIOMode";
			static final String SET_WATCHDOG						= "setWatchdog";
			
			static final String SET_ALT_OUT_B4_SHOT_DELAY_TIME 		= "setAltOutBeforeShotDelayTime";
			static final String SET_ALT_OUT_AFTER_SHOT_DELAY_TIME 	= "setAltOutAfterShotDelayTime";
			static final String SET_ALT_OUT_B4_SHOT_TIME 			= "setAltOutBeforeShotTime";
			static final String SET_ALT_OUT_AFTER_SHOT_TIME 		= "setAltOutAfterShotTime";			
			static final String SET_ALT_OUT_TRIGGER_LEVEL			= "setAltOutTriggerLevel";
			
			static final String SET_MAX_PROGRAM_TIME				= "setMaxProgramTime";
			static final String SET_PROGRAM_MODE					= "setProgramMode"; 
			static final String SET_JOYSTICK						= "setJoystick";
			static final String SET_PINGPONG 						= "setPingpong";			
			static final String SEND_ALL_MOTORS_START				= "sendAllStart";
			static final String SET_START_HERE						= "setStartHere";
			static final String SET_STOP_HERE						= "setStopHere";
			static final String REVERSE_START_STOP					= "reverseStartStop";
			static final String SET_FPS								= "setFps";			
			static final String SET_GRAFFIK							= "setGraffik";
			static final String SET_APP								= "setApp";
			static final String SET_PROGRAM_DELAY					= "setProgramDelay";			
			static final String SET_DEBUG							= "setDebug";
						
			// Queries		
			static final String GET_FIRMWARE						= "getFirmware";
			static final String GET_RUN_STATUS 						= "getRunStatus";
			static final String GET_RUN_TIME 						= "getRunTime";
			static final String IS_EXPOSING							= "isExposing";
			static final String IS_TIMING_MASTER 					= "isTimingMaster";
			static final String GET_NAME							= "getName";
			static final String GET_MAX_STEP_RATE 					= "getMaxStepRate";
			static final String GET_VOLTAGE 						= "getVoltage";
			static final String GET_CURRENT 						= "getCurrent";
			static final String GET_ALT_INPUT_EDGE					= "getAltInputEdge";
			static final String GET_ALT_IO_MODE						= "getAltIOMode";			
			static final String GET_LIMIT_SWITCH_STATE				= "getLimitSwitchState";
			static final String GET_ALT_OUT_B4_SHOT_DELAY_TIME 		= "getAltOutBeforeShotDelayTime";
			static final String GET_ALT_OUT_AFTER_SHOT_DELAY_TIME 	= "getAltOutAfterShotDelayTime";
			static final String GET_ALT_OUT_B4_SHOT_TIME 			= "getAltOutBeforeShotTime";
			static final String GET_ALT_OUT_AFTER_SHOT_TIME 		= "getAltOutAfterShotTime";			
			static final String GET_ALT_OUT_TRIGGER_LEVEL			= "getAltOutTriggerLevel";
			static final String GET_PROGRAM_DELAY					= "getProgramDelay";
			static final String GET_PROGRAM_MODE 					= "getProgramMode";
			static final String GET_POWER_CYCLED					= "getPowerCyled";	
			static final String GET_JOYSTICK						= "getJoystickmode";	
			static final String GET_PINGPONG						= "getPingpong";
			static final String GET_WATCHDOG						= "getWatchdog";
			static final String GET_PCT_COMP						= "getPctComplete";
			static final String GET_MOT_ATTCH						= "getMotorAttach";
			static final String GET_TOTAL_RUN_TIME					= "getTotalRunTime";
			static final String IS_PROGRAM_COMPLETE					= "isProgramComplete";
			static final String GET_FPS 							= "getFps";			
			static final String GET_MOTOR_RUNNING_STATES			= "getMotorRunningStates";
			static final String IS_PROGRAM_VALID					= "isProgramValid";
			static final String GET_MOTOR_SLEEP_STATES				= "getMotorSleepStates";
			static final String IS_GRAFFIK							= "isGraffik";	
			
			static final String GET_FREE_MEMORY	 					= "getFreeMemory";					
		}
		public static class Motor {
			static final String SET_SLEEP 		= "setSleep";
			static final String SET_ENABLE 		= "setEnable";			
			static final String SET_BACKLASH 	= "setBacklash";
			static final String SET_MICROSTEPS 	= "setMS";			
			
			static final String RESET_LIMITS 	= "resetLimits";
			static final String SET_HOME 		= "setHome";		
			static final String SET_END_HERE 	= "setEndhere";
			
			static final String SET_START_HERE	= "setStarthere";
			static final String SET_STOP_HERE 	= "setStophere";
			static final String SET_START		= "setStart";
			static final String SET_STOP		= "setStop";			
			static final String SET_EASING		= "setEasing";
			static final String SET_LEAD_IN		= "setLeadIn";
			static final String SET_TRAVEL		= "setTravel";
			static final String SET_PROG_ACCEL	= "setProgramAccel";
			static final String SET_PROG_DECEL	= "setProgramDecel";
			static final String SET_LEAD_OUT	= "setLeadOut";
			static final String AUTO_SET_MS		= "autoSetMS";
			
			static final String SET_POS 		= "setPos";
			
			static final String SEND_HOME 		= "sendHome";
			static final String SEND_END 		= "sendEnd";
			static final String SEND_START 		= "sendStart";
			static final String SEND_STOP		= "sendStop";
			static final String SEND_TO 		= "sendTo";
			
			static final String STOP_MOTOR 		= "stop";
			static final String SET_MAX_SPEED 	= "setMaxSpeed";
			static final String SET_DIR 		= "setDir";
			static final String SET_SPEED		= "setSpeed";
			static final String SET_ACCEL		= "setAccel";						
			
			static final String IS_RUNNING 		= "isRunning";
			static final String GET_ENABLE 		= "isEnabled";			
			static final String GET_BACKLASH 	= "getBacklash";
			static final String GET_MS 			= "getMS";
			static final String GET_END 		= "getEnd";
			static final String GET_POS 		= "getPos";			
			static final String GET_SPEED 		= "getSpeed";
			static final String GET_ACCEL 		= "getAccel";
			static final String GET_START 		= "getStart";
			static final String GET_STOP 		= "getStop";
			static final String GET_TRAVEL		= "getTravel";
			static final String GET_SLEEP 		= "getSleep";
			static final String GET_DIR 		= "getDir";
			static final String GET_MAX_STEP_RATE = "getMaxStepRate";		
			static final String GET_EASING		= "getEasing";
			static final String GET_LEAD_IN		= "getLeadIn";
			static final String GET_PROG_ACCEL	= "getProgramAccel";
			static final String GET_PROG_DECEL	= "getProgramDecel";
			static final String GET_LEAD_OUT	= "getLeadOut";
			static final String IS_SPEED_VALID	= "isSpeedValid";
			
		}
		public static class Camera {
			// General settings			
			static final String SET_ENABLE 		= "setEnable";			
			static final String SET_TEST_MODE 	= "setTestMode";			
			static final String SET_KEEPALIVE	= "setKeepAlive";			
			static final String EXPOSE_NOW 		= "expose";
			
			// General queries
			static final String GET_ENABLED		= "isEnabled";
			static final String GET_TEST_MODE	= "getTestMode";
			static final String GET_KEEPALIVE	= "getKeepAlive";
			static final String GET_EXPOSING	= "isExposing";
			
			// Exposure settings
			static final String SET_INTERVAL	= "setInterval";
			static final String SET_TRIGGER 	= "setTrigger";
			static final String SET_FOCUS 		= "setFocus";				
			static final String SET_DELAY	 	= "setDelay";
			static final String SET_MAX_SHOTS	= "setMaxShots";		
			static final String SET_MUP		 	= "setMUP";			
			
			// Exposure queries			
			static final String GET_SHOTS		= "getShots";	
			static final String GET_INTERVAL	= "getInterval";
			static final String GET_TRIGGER		= "getTrigger";
			static final String GET_FOCUS 		= "getFocus";
			static final String GET_DELAY	 	= "getDelay";
			static final String GET_MAX_SHOTS 	= "getMaxShots";			
			static final String GET_MUP 		= "getMUP";
		}
		public  static class KeyFrame {
			
		}
	}
	
	public static enum Type{
		GENERAL, MOTOR, CAMERA, KEYFRAME;
	}
	
	/* Constructor and Initialization Method */
	
	// Commands that transmit one or more data bytes
	Command(Command.Type type, int command, Class<?> returnType, String name, int dataLength){		
		this.init(type, command, returnType, name, dataLength);
	}
	
	// Commands that transmit no additional data
	Command(Command.Type type, int command, Class<?> returnType, String name){
		this.init(type, command, returnType, name, 0);
	}	
	
	private void init(Command.Type type, int command, Class<?> returnType, String name, int dataLength){
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
	
	public static void initalizeCommands(){
		initGeneralCommands();
		initMotorCommands();
		initCameraCommands();
		initKeyFrameCommands();
	}
	
	private static void initGeneralCommands(){
		// Create the general commands		
		commandList.add(new Command(Command.Type.GENERAL, 2, Void.class, Names.General.START_PROGRAM));
		commandList.add(new Command(Command.Type.GENERAL, 3, Void.class, Names.General.PAUSE_PROGRAM));
		commandList.add(new Command(Command.Type.GENERAL, 4, Void.class, Names.General.STOP_PROGRAM));
		commandList.add(new Command(Command.Type.GENERAL, 5, Void.class, Names.General.TOGGLE_LED));
		commandList.add(new Command(Command.Type.GENERAL, 6, Void.class, Names.General.SET_TIMING_MASTER));
		commandList.add(new Command(Command.Type.GENERAL, 7, Void.class, Names.General.SET_NAME));
		commandList.add(new Command(Command.Type.GENERAL, 8, Void.class, Names.General.SET_ADDRESS, Consts.BYTE_SIZE));
		commandList.add(new Command(Command.Type.GENERAL, 9, Void.class, Names.General.SET_COMMON_LINE, Consts.BYTE_SIZE));
		commandList.add(new Command(Command.Type.GENERAL, 10, Void.class, Names.General.SEND_ALL_MOTORS_HOME));
		commandList.add(new Command(Command.Type.GENERAL, 11, Void.class, Names.General.SET_MAX_STEP_RATE, Consts.INT_SIZE));
		commandList.add(new Command(Command.Type.GENERAL, 12, Void.class, Names.General.SET_INPUT_EDGE, Consts.BYTE_SIZE));
		commandList.add(new Command(Command.Type.GENERAL, 13, Void.class, Names.General.SET_ALT_IO_MODE, Consts.INT_SIZE));
		commandList.add(new Command(Command.Type.GENERAL, 14, Void.class, Names.General.SET_WATCHDOG, Consts.BYTE_SIZE));
		commandList.add(new Command(Command.Type.GENERAL, 15, Void.class, Names.General.SET_ALT_OUT_B4_SHOT_DELAY_TIME, Consts.INT_SIZE));
		commandList.add(new Command(Command.Type.GENERAL, 16, Void.class, Names.General.SET_ALT_OUT_AFTER_SHOT_DELAY_TIME, Consts.INT_SIZE));
		commandList.add(new Command(Command.Type.GENERAL, 17, Void.class, Names.General.SET_ALT_OUT_B4_SHOT_TIME, Consts.INT_SIZE));
		commandList.add(new Command(Command.Type.GENERAL, 18, Void.class, Names.General.SET_ALT_OUT_AFTER_SHOT_TIME, Consts.INT_SIZE));
		commandList.add(new Command(Command.Type.GENERAL, 19, Void.class, Names.General.SET_ALT_OUT_TRIGGER_LEVEL, Consts.BYTE_SIZE));
		commandList.add(new Command(Command.Type.GENERAL, 20, Void.class, Names.General.SET_MAX_PROGRAM_TIME, Consts.LONG_SIZE));
		commandList.add(new Command(Command.Type.GENERAL, 21, Void.class, Names.General.SET_PROGRAM_DELAY, Consts.LONG_SIZE));
		commandList.add(new Command(Command.Type.GENERAL, 22, Void.class, Names.General.SET_PROGRAM_MODE, Consts.BYTE_SIZE));
		commandList.add(new Command(Command.Type.GENERAL, 23, Void.class, Names.General.SET_JOYSTICK, Consts.BYTE_SIZE));
		commandList.add(new Command(Command.Type.GENERAL, 24, Void.class, Names.General.SET_PINGPONG, Consts.BYTE_SIZE));
		commandList.add(new Command(Command.Type.GENERAL, 25, Void.class, Names.General.SEND_ALL_MOTORS_START));
		commandList.add(new Command(Command.Type.GENERAL, 26, Void.class, Names.General.SET_START_HERE));
		commandList.add(new Command(Command.Type.GENERAL, 27, Void.class, Names.General.SET_STOP_HERE));
		commandList.add(new Command(Command.Type.GENERAL, 28, Void.class, Names.General.SET_FPS, Consts.BYTE_SIZE));
		commandList.add(new Command(Command.Type.GENERAL, 29, Void.class, Names.General.REVERSE_START_STOP));
		commandList.add(new Command(Command.Type.GENERAL, 50, Void.class, Names.General.SET_GRAFFIK, Consts.BYTE_SIZE));
		commandList.add(new Command(Command.Type.GENERAL, 51, Void.class, Names.General.SET_APP, Consts.BYTE_SIZE));
		commandList.add(new Command(Command.Type.GENERAL, 52, Void.class, Names.General.SET_PROGRAM_DELAY, Consts.LONG_SIZE));
		
		// Queries
		commandList.add(new Command(Command.Type.GENERAL, 100, Integer.class, Names.General.GET_FIRMWARE));
		commandList.add(new Command(Command.Type.GENERAL, 101, Integer.class, Names.General.GET_RUN_STATUS));
		commandList.add(new Command(Command.Type.GENERAL, 102, Integer.class, Names.General.GET_RUN_TIME));
		commandList.add(new Command(Command.Type.GENERAL, 103, Boolean.class, Names.General.IS_EXPOSING));
		commandList.add(new Command(Command.Type.GENERAL, 104, Boolean.class, Names.General.IS_TIMING_MASTER));
		commandList.add(new Command(Command.Type.GENERAL, 105, String.class, Names.General.GET_NAME));
		commandList.add(new Command(Command.Type.GENERAL, 106, Integer.class, Names.General.GET_MAX_STEP_RATE));
		commandList.add(new Command(Command.Type.GENERAL, 107, Float.class, Names.General.GET_VOLTAGE));
		commandList.add(new Command(Command.Type.GENERAL, 108, Float.class, Names.General.GET_CURRENT));
		commandList.add(new Command(Command.Type.GENERAL, 109, Integer.class, Names.General.GET_ALT_INPUT_EDGE));
		commandList.add(new Command(Command.Type.GENERAL, 110, Integer.class, Names.General.GET_ALT_IO_MODE));
		commandList.add(new Command(Command.Type.GENERAL, 111, Integer.class, Names.General.GET_LIMIT_SWITCH_STATE));
		commandList.add(new Command(Command.Type.GENERAL, 112, Integer.class, Names.General.GET_ALT_OUT_B4_SHOT_DELAY_TIME));
		commandList.add(new Command(Command.Type.GENERAL, 113, Integer.class, Names.General.GET_ALT_OUT_AFTER_SHOT_DELAY_TIME));
		commandList.add(new Command(Command.Type.GENERAL, 114, Integer.class, Names.General.GET_ALT_OUT_B4_SHOT_TIME));
		commandList.add(new Command(Command.Type.GENERAL, 115, Integer.class, Names.General.GET_ALT_OUT_AFTER_SHOT_TIME));
		commandList.add(new Command(Command.Type.GENERAL, 116, Integer.class, Names.General.GET_ALT_OUT_TRIGGER_LEVEL));
		commandList.add(new Command(Command.Type.GENERAL, 117, Integer.class, Names.General.GET_PROGRAM_DELAY));
		commandList.add(new Command(Command.Type.GENERAL, 118, Integer.class, Names.General.GET_PROGRAM_MODE));
		commandList.add(new Command(Command.Type.GENERAL, 119, Integer.class, Names.General.GET_POWER_CYCLED));
		commandList.add(new Command(Command.Type.GENERAL, 120, Integer.class, Names.General.GET_JOYSTICK));
		commandList.add(new Command(Command.Type.GENERAL, 121, Integer.class, Names.General.GET_PINGPONG));
		commandList.add(new Command(Command.Type.GENERAL, 122, Integer.class, Names.General.GET_WATCHDOG));
		commandList.add(new Command(Command.Type.GENERAL, 123, Integer.class, Names.General.GET_PCT_COMP));
		commandList.add(new Command(Command.Type.GENERAL, 124, Integer.class, Names.General.GET_MOT_ATTCH));
		commandList.add(new Command(Command.Type.GENERAL, 125, Integer.class, Names.General.GET_TOTAL_RUN_TIME));
		commandList.add(new Command(Command.Type.GENERAL, 126, Boolean.class, Names.General.IS_PROGRAM_COMPLETE));
		commandList.add(new Command(Command.Type.GENERAL, 127, Integer.class, Names.General.GET_FPS));
		commandList.add(new Command(Command.Type.GENERAL, 128, Integer.class, Names.General.GET_MOTOR_RUNNING_STATES));
		commandList.add(new Command(Command.Type.GENERAL, 129, Boolean.class, Names.General.IS_PROGRAM_VALID));
		commandList.add(new Command(Command.Type.GENERAL, 130, Integer.class, Names.General.GET_MOTOR_SLEEP_STATES));
		commandList.add(new Command(Command.Type.GENERAL, 150, Boolean.class, Names.General.IS_GRAFFIK));
	}
	
	private static void initMotorCommands(){
		// Create the general commands		
		commandList.add(new Command(Command.Type.MOTOR, 2, Void.class, Names.Motor.SET_SLEEP, Consts.BYTE_SIZE));
		commandList.add(new Command(Command.Type.MOTOR, 3, Void.class, Names.Motor.SET_ENABLE, Consts.BYTE_SIZE));
		commandList.add(new Command(Command.Type.MOTOR, 4, Void.class, Names.Motor.STOP_MOTOR));
		commandList.add(new Command(Command.Type.MOTOR, 5, Void.class, Names.Motor.SET_BACKLASH, Consts.INT_SIZE));
		commandList.add(new Command(Command.Type.MOTOR, 6, Void.class, Names.Motor.SET_MICROSTEPS, Consts.BYTE_SIZE));
		commandList.add(new Command(Command.Type.MOTOR, 7, Void.class, Names.Motor.SET_MAX_SPEED, Consts.INT_SIZE));
		commandList.add(new Command(Command.Type.MOTOR, 8, Void.class, Names.Motor.SET_DIR, Consts.BYTE_SIZE));
		commandList.add(new Command(Command.Type.MOTOR, 9, Void.class, Names.Motor.SET_HOME));
		commandList.add(new Command(Command.Type.MOTOR, 10, Void.class, Names.Motor.SET_END_HERE));
		commandList.add(new Command(Command.Type.MOTOR, 11, Void.class, Names.Motor.SEND_HOME));
		commandList.add(new Command(Command.Type.MOTOR, 12, Void.class, Names.Motor.SEND_END));
		commandList.add(new Command(Command.Type.MOTOR, 13, Void.class, Names.Motor.SET_SPEED, Consts.FLOAT_SIZE));
		commandList.add(new Command(Command.Type.MOTOR, 14, Void.class, Names.Motor.SET_ACCEL, Consts.FLOAT_SIZE));
		
		commandList.add(new Command(Command.Type.MOTOR, 16, Void.class, Names.Motor.SET_START, Consts.LONG_SIZE));
		commandList.add(new Command(Command.Type.MOTOR, 17, Void.class, Names.Motor.SET_STOP, Consts.LONG_SIZE));
		commandList.add(new Command(Command.Type.MOTOR, 18, Void.class, Names.Motor.SET_EASING, Consts.BYTE_SIZE));
		commandList.add(new Command(Command.Type.MOTOR, 19, Void.class, Names.Motor.SET_LEAD_IN, Consts.LONG_SIZE));
		commandList.add(new Command(Command.Type.MOTOR, 20, Void.class, Names.Motor.SET_TRAVEL, Consts.LONG_SIZE));
		commandList.add(new Command(Command.Type.MOTOR, 21, Void.class, Names.Motor.SET_PROG_ACCEL, Consts.LONG_SIZE));
		commandList.add(new Command(Command.Type.MOTOR, 22, Void.class, Names.Motor.SET_PROG_DECEL, Consts.LONG_SIZE));
		commandList.add(new Command(Command.Type.MOTOR, 23, Void.class, Names.Motor.SEND_START));
		commandList.add(new Command(Command.Type.MOTOR, 24, Void.class, Names.Motor.SEND_STOP));
		commandList.add(new Command(Command.Type.MOTOR, 25, Void.class, Names.Motor.SET_LEAD_OUT, Consts.LONG_SIZE));
		
		commandList.add(new Command(Command.Type.MOTOR, 27, Void.class, Names.Motor.RESET_LIMITS));
		commandList.add(new Command(Command.Type.MOTOR, 28, Integer.class, Names.Motor.AUTO_SET_MS));
		commandList.add(new Command(Command.Type.MOTOR, 29, Void.class, Names.Motor.SET_START_HERE));
		commandList.add(new Command(Command.Type.MOTOR, 30, Void.class, Names.Motor.SET_STOP_HERE, Consts.BYTE_SIZE));
		commandList.add(new Command(Command.Type.MOTOR, 51, Void.class, Names.Motor.SET_POS, Consts.LONG_SIZE));
		
		// Queries
		commandList.add(new Command(Command.Type.MOTOR, 100, Boolean.class, Names.Motor.GET_ENABLE));
		commandList.add(new Command(Command.Type.MOTOR, 101, Integer.class, Names.Motor.GET_BACKLASH));
		commandList.add(new Command(Command.Type.MOTOR, 102, Integer.class, Names.Motor.GET_MS));
		commandList.add(new Command(Command.Type.MOTOR, 103, Integer.class, Names.Motor.GET_DIR));
		commandList.add(new Command(Command.Type.MOTOR, 104, Integer.class, Names.Motor.GET_MAX_STEP_RATE));
		commandList.add(new Command(Command.Type.MOTOR, 105, Integer.class, Names.Motor.GET_END));
		commandList.add(new Command(Command.Type.MOTOR, 106, Integer.class, Names.Motor.GET_POS));
		commandList.add(new Command(Command.Type.MOTOR, 107, Boolean.class, Names.Motor.IS_RUNNING));
		commandList.add(new Command(Command.Type.MOTOR, 108, Float.class, Names.Motor.SET_SPEED));
		commandList.add(new Command(Command.Type.MOTOR, 109, Float.class, Names.Motor.GET_ACCEL));
		commandList.add(new Command(Command.Type.MOTOR, 110, Integer.class, Names.Motor.GET_EASING));
		commandList.add(new Command(Command.Type.MOTOR, 111, Integer.class, Names.Motor.GET_START));
		commandList.add(new Command(Command.Type.MOTOR, 112, Integer.class, Names.Motor.GET_STOP));
		commandList.add(new Command(Command.Type.MOTOR, 113, Integer.class, Names.Motor.GET_TRAVEL));
		commandList.add(new Command(Command.Type.MOTOR, 114, Integer.class, Names.Motor.GET_LEAD_IN));
		commandList.add(new Command(Command.Type.MOTOR, 115, Integer.class, Names.Motor.GET_PROG_ACCEL));
		commandList.add(new Command(Command.Type.MOTOR, 116, Integer.class, Names.Motor.GET_PROG_DECEL));
		commandList.add(new Command(Command.Type.MOTOR, 117, Boolean.class, Names.Motor.GET_SLEEP));
		commandList.add(new Command(Command.Type.MOTOR, 118, Boolean.class, Names.Motor.IS_SPEED_VALID));
		commandList.add(new Command(Command.Type.MOTOR, 119, Integer.class, Names.Motor.GET_LEAD_OUT));
	}
	
	private static void initCameraCommands(){
		
	}
	
	private static void initKeyFrameCommands(){
		
	}
	
	/**
	 * Fetch command using name
	 * @param type
	 * @param name
	 * @return
	 */
	public static Command get(Type type, String name) throws UnsupportedOperationException{
		if(!listInitialized){
			Command.initalizeCommands();
		}
		for(int i = 0; i < commandList.size(); i++){
			Command thisCommand = commandList.get(i); 
			if(type == thisCommand.getType() && name.equals(thisCommand.getName())){
				return thisCommand;
			}
		}
		// If no command was thrown, it must have been invalid
		throw new UnsupportedOperationException();		
	}
	
	/**
	 * Fetch command using command number
	 * @param type
	 * @param command
	 * @return
	 */
	public static Command get(Type type, int command){		
		if(!listInitialized){
			Command.initalizeCommands();
		}
		for(int i = 0; i < commandList.size(); i++){
			Command thisCommand = commandList.get(i); 
			if(type == thisCommand.getType() && command == thisCommand.getCommandNum()){
				return thisCommand;
			}
		}
		// If no command was thrown, it must have been invalid
		throw new UnsupportedOperationException();		
	}
	
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
	
	public Type getType(){
		return this.type;
	}
	
	public int getCommandNum(){
		return this.command;
	}
	
	public void printInfo(){
		System.out.println("Command type: " + this.type);
		System.out.println("Number: " + this.command);
		System.out.println("Name: " + this.name);
		System.out.println("Return type: " + this.returnType.getName());		
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
		
		//System.out.println("Address: " + Command.addr + " Subaddr: " + subaddr + " Command: " + this.command + " Data: " + data);		
		
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
		
		// Wait for the NMX to clear
		waitForNMX();	
		
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
		if(debug){
			System.out.println("Command: " + this.name);
		}
		if(ret != null)
			System.out.println(ret);
		else
			System.out.println("OK!");
		
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
