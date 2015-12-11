package com.dynamicperception.nmxcommandline.models;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.dynamicperception.nmxcommandline.helpers.Consts;

public class Command {
		
	private static int addr = 3;
	private static boolean debug = false;
	private static List<Command> generalList = new ArrayList<Command>();
	private static List<Command> motorList = new ArrayList<Command>();
	private static List<Command> cameraList = new ArrayList<Command>();
	private static List<Command> keyFrameList = new ArrayList<Command>();
	
	private static boolean listsInitialized = false;
	private String name;	
	private Type type;
	private int subaddr;
	private int command;
	private Class<?> dataType;
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
			static final String SET_FPS								= "setFPS";			
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
			static final String GET_JOYSTICK						= "getJoystick";	
			static final String GET_PINGPONG						= "getPingpong";
			static final String GET_WATCHDOG						= "getWatchdog";
			static final String GET_PCT_COMP						= "getPctComplete";
			static final String GET_MOT_ATTCH						= "getMotorAttach";
			static final String GET_TOTAL_RUN_TIME					= "getTotalRunTime";
			static final String IS_PROGRAM_COMPLETE					= "isProgramComplete";
			static final String GET_FPS 							= "getFPS";			
			static final String GET_MOTOR_RUNNING_STATES			= "getMotorRunningStates";
			static final String IS_PROGRAM_VALID					= "isProgramValid";
			static final String GET_MOTOR_SLEEP_STATES				= "getMotorSleepStates";
			static final String IS_GRAFFIK							= "isGraffik";	
			
			static final String GET_FREE_MEMORY	 					= "getFreeMemory";				
			
			public static void help(){
				Command.help(Type.GENERAL);
			}
			public static void find(String term){
				Command.find(Type.GENERAL, term);
			}
		}
		public static class Motor {
			//Commands
			static final String SET_SLEEP 		= "setSleep";
			static final String SET_ENABLE 		= "setEnable";			
			static final String SET_BACKLASH 	= "setBacklash";
			static final String SET_MICROSTEPS 	= "setMS";			
			
			static final String RESET_LIMITS 	= "resetLimits";
			static final String SET_HOME 		= "setHome";		
			static final String SET_END_HERE 	= "setEndHere";
			
			static final String SET_START_HERE	= "setStartHere";
			static final String SET_STOP_HERE 	= "setStopHere";
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
			
			// Queries
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
			
			public static void help(){
				Command.help(Type.MOTOR);
			}
			public static void find(String term){
				Command.find(Type.MOTOR, term);
			}
		}
		public static class Camera {
			//Commands
			static final String SET_ENABLE 				= "setEnable";			
			static final String EXPOSE_NOW 				= "expose";
			static final String SET_TRIGGER 			= "setTrigger";
			static final String SET_FOCUS 				= "setFocus";
			static final String SET_MAX_SHOTS			= "setMaxShots";
			static final String SET_DELAY	 			= "setDelay";
			static final String SET_FOCUS_WITH_SHUTTER	= "setFocusWithShutter";
			static final String SET_INTERVAL			= "setInterval";		
			static final String SET_MUP		 			= "setMUP";
			static final String SET_TEST_MODE 			= "setTestMode";
			static final String SET_KEEPALIVE			= "setKeepAlive";
			
			// Queries
			static final String IS_ENABLED				= "isEnabled";
			static final String IS_EXPOSING				= "isExposing";
			static final String GET_TRIGGER				= "getTrigger";
			static final String GET_FOCUS 				= "getFocus";
			static final String GET_MAX_SHOTS 			= "getMaxShots";
			static final String GET_DELAY	 			= "getDelay";	
			static final String GET_FOCUS_WITH_SHUTTER	= "getFocusWithShutter";
			static final String GET_MUP 				= "getMUP";
			static final String GET_INTERVAL			= "getInterval";			
			static final String GET_SHOTS				= "getShots";	
			static final String GET_TEST_MODE			= "getTestMode";
			static final String GET_KEEPALIVE			= "getKeepAlive";
						
		}
		public  static class KeyFrame {
			// Commands
			static final String SET_AXIS 		= "setAxis";
			static final String SET_COUNT		= "setCount";			
			static final String SET_ABSCISSA 	= "setAbscissa";
			static final String SET_POS			= "setPos";
			static final String SET_VEL			= "setVel";		
			
			static final String SET_UPDATE_RATE 	= "setUpdateRate";
			static final String END_TRANSMISSION	= "endTransmission";		
			static final String SET_CONT_VID_TIME	= "setContVidTime";
			
			static final String START_PROGRAM	= "startProgram";
			static final String PAUSE_PROGRAM 	= "pauseProgram";
			static final String STOP_PROGRAM	= "stopProgram";
			
			// Queries
			static final String PRINT_INFO		= "printInfo";
			static final String GET_COUNT 		= "getCount";			
			static final String GET_UPDATE_RATE = "getUpdateRate";
			static final String GET_POS_AT		= "getPosAt";
			static final String GET_VEL_AT 		= "getVelAt";
			static final String GET_ACCEL_AT	= "getAccelAt";			
			static final String IS_VEL_VALID	= "isVelValid";
			static final String IS_ACCEL_VALID	= "isAccelValid";
			static final String IS_RUNNING 		= "isRunning";
			static final String GET_RUN_TIME	= "getRunTime";
			static final String GET_MAX_RUN_TIME = "getMaxRunTime";
			static final String GET_PCT_DONE	= "getPctDone";			
		}
	}
	
	public static enum Type{
		GENERAL, MOTOR, CAMERA, KEYFRAME;
	}
	
	public static enum Length{
		NULL, BYTE, INT, LONG, FLOAT;
	}
	
	/* Constructor and Initialization Method */
	
	// Commands that transmit one or more data bytes	
	Command(Command.Type type, int command, Class<?> returnType, String name, Class<?> dataType){		
		this.init(type, command, returnType, name, dataType);
	}
	
	Command(Command.Type type, int command, String name, Class<?> dataType){		
		this.init(type, command, Void.class, name, dataType);
	}
	
	// Commands that transmit no additional data
	Command(Command.Type type, int command, Class<?> returnType, String name){
		this.init(type, command, returnType, name, Void.class);
	}
	
	Command(Command.Type type, int command, String name){
		this.init(type, command, Void.class, name, Void.class);
	}	
	
	private void init(Command.Type type, int command, Class<?> returnType, String name, Class<?> dataType){
		this.name = name;
		this.type = type;
		this.command = command;			
		this.returnType = returnType;
		this.dataType = dataType;	
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
		this.dataType = dataType;
		this.dataLength = dataType == Byte.class ? 1 : dataType == Integer.class ? 2 : dataType == Long.class || dataType == Float.class ? 4 : 0;
	}

	
	/* Static Methods */
	
	public static void initalizeCommands(){
		initGeneralCommands();
		initMotorCommands();
		initCameraCommands();
		initKeyFrameCommands();
		listsInitialized = true;
	}
	
	private static void initGeneralCommands(){
		// Create the general commands		
		generalList.add(new Command(Command.Type.GENERAL, 2, Names.General.START_PROGRAM));
		generalList.add(new Command(Command.Type.GENERAL, 3, Names.General.PAUSE_PROGRAM));
		generalList.add(new Command(Command.Type.GENERAL, 4, Names.General.STOP_PROGRAM));
		generalList.add(new Command(Command.Type.GENERAL, 5, Names.General.TOGGLE_LED));
		generalList.add(new Command(Command.Type.GENERAL, 6, Names.General.SET_TIMING_MASTER));
		generalList.add(new Command(Command.Type.GENERAL, 7, Names.General.SET_NAME));
		generalList.add(new Command(Command.Type.GENERAL, 8, Names.General.SET_ADDRESS, Byte.class));
		generalList.add(new Command(Command.Type.GENERAL, 9, Names.General.SET_COMMON_LINE, Byte.class));
		generalList.add(new Command(Command.Type.GENERAL, 10, Names.General.SEND_ALL_MOTORS_HOME));
		generalList.add(new Command(Command.Type.GENERAL, 11, Names.General.SET_MAX_STEP_RATE, Integer.class));
		generalList.add(new Command(Command.Type.GENERAL, 12, Names.General.SET_INPUT_EDGE, Byte.class));
		generalList.add(new Command(Command.Type.GENERAL, 13, Names.General.SET_ALT_IO_MODE, Integer.class));
		generalList.add(new Command(Command.Type.GENERAL, 14, Names.General.SET_WATCHDOG, Byte.class));
		generalList.add(new Command(Command.Type.GENERAL, 15, Names.General.SET_ALT_OUT_B4_SHOT_DELAY_TIME, Integer.class));
		generalList.add(new Command(Command.Type.GENERAL, 16, Names.General.SET_ALT_OUT_AFTER_SHOT_DELAY_TIME, Integer.class));
		generalList.add(new Command(Command.Type.GENERAL, 17, Names.General.SET_ALT_OUT_B4_SHOT_TIME, Integer.class));
		generalList.add(new Command(Command.Type.GENERAL, 18, Names.General.SET_ALT_OUT_AFTER_SHOT_TIME, Integer.class));
		generalList.add(new Command(Command.Type.GENERAL, 19, Names.General.SET_ALT_OUT_TRIGGER_LEVEL, Byte.class));
		generalList.add(new Command(Command.Type.GENERAL, 20, Names.General.SET_MAX_PROGRAM_TIME, Long.class));
		generalList.add(new Command(Command.Type.GENERAL, 21, Names.General.SET_PROGRAM_DELAY, Long.class));
		generalList.add(new Command(Command.Type.GENERAL, 22, Names.General.SET_PROGRAM_MODE, Byte.class));
		generalList.add(new Command(Command.Type.GENERAL, 23, Names.General.SET_JOYSTICK, Byte.class));
		generalList.add(new Command(Command.Type.GENERAL, 24, Names.General.SET_PINGPONG, Byte.class));
		generalList.add(new Command(Command.Type.GENERAL, 25, Names.General.SEND_ALL_MOTORS_START));
		generalList.add(new Command(Command.Type.GENERAL, 26, Names.General.SET_START_HERE));
		generalList.add(new Command(Command.Type.GENERAL, 27, Names.General.SET_STOP_HERE));
		generalList.add(new Command(Command.Type.GENERAL, 28, Names.General.SET_FPS, Byte.class));
		generalList.add(new Command(Command.Type.GENERAL, 29, Names.General.REVERSE_START_STOP));
		generalList.add(new Command(Command.Type.GENERAL, 50, Names.General.SET_GRAFFIK, Byte.class));
		generalList.add(new Command(Command.Type.GENERAL, 51, Names.General.SET_APP, Byte.class));
		generalList.add(new Command(Command.Type.GENERAL, 52, Names.General.SET_PROGRAM_DELAY, Long.class));
		
		// Queries
		generalList.add(new Command(Command.Type.GENERAL, 100, Integer.class, Names.General.GET_FIRMWARE));
		generalList.add(new Command(Command.Type.GENERAL, 101, Integer.class, Names.General.GET_RUN_STATUS));
		generalList.add(new Command(Command.Type.GENERAL, 102, Integer.class, Names.General.GET_RUN_TIME));
		generalList.add(new Command(Command.Type.GENERAL, 103, Boolean.class, Names.General.IS_EXPOSING));
		generalList.add(new Command(Command.Type.GENERAL, 104, Boolean.class, Names.General.IS_TIMING_MASTER));
		generalList.add(new Command(Command.Type.GENERAL, 105, String.class, Names.General.GET_NAME));
		generalList.add(new Command(Command.Type.GENERAL, 106, Integer.class, Names.General.GET_MAX_STEP_RATE));
		generalList.add(new Command(Command.Type.GENERAL, 107, Float.class, Names.General.GET_VOLTAGE));
		generalList.add(new Command(Command.Type.GENERAL, 108, Float.class, Names.General.GET_CURRENT));
		generalList.add(new Command(Command.Type.GENERAL, 109, Integer.class, Names.General.GET_ALT_INPUT_EDGE));
		generalList.add(new Command(Command.Type.GENERAL, 110, Integer.class, Names.General.GET_ALT_IO_MODE));
		generalList.add(new Command(Command.Type.GENERAL, 111, Integer.class, Names.General.GET_LIMIT_SWITCH_STATE));
		generalList.add(new Command(Command.Type.GENERAL, 112, Integer.class, Names.General.GET_ALT_OUT_B4_SHOT_DELAY_TIME));
		generalList.add(new Command(Command.Type.GENERAL, 113, Integer.class, Names.General.GET_ALT_OUT_AFTER_SHOT_DELAY_TIME));
		generalList.add(new Command(Command.Type.GENERAL, 114, Integer.class, Names.General.GET_ALT_OUT_B4_SHOT_TIME));
		generalList.add(new Command(Command.Type.GENERAL, 115, Integer.class, Names.General.GET_ALT_OUT_AFTER_SHOT_TIME));
		generalList.add(new Command(Command.Type.GENERAL, 116, Integer.class, Names.General.GET_ALT_OUT_TRIGGER_LEVEL));
		generalList.add(new Command(Command.Type.GENERAL, 117, Integer.class, Names.General.GET_PROGRAM_DELAY));
		generalList.add(new Command(Command.Type.GENERAL, 118, Integer.class, Names.General.GET_PROGRAM_MODE));
		generalList.add(new Command(Command.Type.GENERAL, 119, Integer.class, Names.General.GET_POWER_CYCLED));
		generalList.add(new Command(Command.Type.GENERAL, 120, Boolean.class, Names.General.GET_JOYSTICK));
		generalList.add(new Command(Command.Type.GENERAL, 121, Boolean.class, Names.General.GET_PINGPONG));
		generalList.add(new Command(Command.Type.GENERAL, 122, Boolean.class, Names.General.GET_WATCHDOG));
		generalList.add(new Command(Command.Type.GENERAL, 123, Boolean.class, Names.General.GET_PCT_COMP));
		generalList.add(new Command(Command.Type.GENERAL, 124, Integer.class, Names.General.GET_MOT_ATTCH));
		generalList.add(new Command(Command.Type.GENERAL, 125, Integer.class, Names.General.GET_TOTAL_RUN_TIME));
		generalList.add(new Command(Command.Type.GENERAL, 126, Boolean.class, Names.General.IS_PROGRAM_COMPLETE));
		generalList.add(new Command(Command.Type.GENERAL, 127, Integer.class, Names.General.GET_FPS));
		generalList.add(new Command(Command.Type.GENERAL, 128, Integer.class, Names.General.GET_MOTOR_RUNNING_STATES));
		generalList.add(new Command(Command.Type.GENERAL, 129, Boolean.class, Names.General.IS_PROGRAM_VALID));
		generalList.add(new Command(Command.Type.GENERAL, 130, Integer.class, Names.General.GET_MOTOR_SLEEP_STATES));
		generalList.add(new Command(Command.Type.GENERAL, 150, Boolean.class, Names.General.IS_GRAFFIK));
	}
	
	private static void initMotorCommands(){
		// Create the general commands		
		motorList.add(new Command(Command.Type.MOTOR, 2, Names.Motor.SET_SLEEP, Byte.class));
		motorList.add(new Command(Command.Type.MOTOR, 3, Names.Motor.SET_ENABLE, Byte.class));
		motorList.add(new Command(Command.Type.MOTOR, 4, Names.Motor.STOP_MOTOR));
		motorList.add(new Command(Command.Type.MOTOR, 5, Names.Motor.SET_BACKLASH, Integer.class));
		motorList.add(new Command(Command.Type.MOTOR, 6, Names.Motor.SET_MICROSTEPS, Byte.class));
		motorList.add(new Command(Command.Type.MOTOR, 7, Names.Motor.SET_MAX_SPEED, Integer.class));
		motorList.add(new Command(Command.Type.MOTOR, 8, Names.Motor.SET_DIR, Byte.class));
		motorList.add(new Command(Command.Type.MOTOR, 9, Names.Motor.SET_HOME));
		motorList.add(new Command(Command.Type.MOTOR, 10, Names.Motor.SET_END_HERE));
		motorList.add(new Command(Command.Type.MOTOR, 11, Names.Motor.SEND_HOME));
		motorList.add(new Command(Command.Type.MOTOR, 12, Names.Motor.SEND_END));
		motorList.add(new Command(Command.Type.MOTOR, 13, Names.Motor.SET_SPEED, Float.class));
		motorList.add(new Command(Command.Type.MOTOR, 14, Names.Motor.SET_ACCEL, Float.class));
		
		motorList.add(new Command(Command.Type.MOTOR, 16, Names.Motor.SET_START, Long.class));
		motorList.add(new Command(Command.Type.MOTOR, 17, Names.Motor.SET_STOP, Long.class));
		motorList.add(new Command(Command.Type.MOTOR, 18, Names.Motor.SET_EASING, Byte.class));
		motorList.add(new Command(Command.Type.MOTOR, 19, Names.Motor.SET_LEAD_IN, Long.class));
		motorList.add(new Command(Command.Type.MOTOR, 20, Names.Motor.SET_TRAVEL, Long.class));
		motorList.add(new Command(Command.Type.MOTOR, 21, Names.Motor.SET_PROG_ACCEL, Long.class));
		motorList.add(new Command(Command.Type.MOTOR, 22, Names.Motor.SET_PROG_DECEL, Long.class));
		motorList.add(new Command(Command.Type.MOTOR, 23, Names.Motor.SEND_START));
		motorList.add(new Command(Command.Type.MOTOR, 24, Names.Motor.SEND_STOP));
		motorList.add(new Command(Command.Type.MOTOR, 25, Names.Motor.SET_LEAD_OUT, Long.class));
		
		motorList.add(new Command(Command.Type.MOTOR, 27, Names.Motor.RESET_LIMITS));
		motorList.add(new Command(Command.Type.MOTOR, 28, Integer.class, Names.Motor.AUTO_SET_MS));
		motorList.add(new Command(Command.Type.MOTOR, 29, Names.Motor.SET_START_HERE));
		motorList.add(new Command(Command.Type.MOTOR, 30, Names.Motor.SET_STOP_HERE, Byte.class));
		motorList.add(new Command(Command.Type.MOTOR, 31, Names.Motor.SEND_TO, Long.class));
		motorList.add(new Command(Command.Type.MOTOR, 51, Names.Motor.SET_POS, Long.class));
		
		// Queries
		motorList.add(new Command(Command.Type.MOTOR, 100, Boolean.class, Names.Motor.GET_ENABLE));
		motorList.add(new Command(Command.Type.MOTOR, 101, Integer.class, Names.Motor.GET_BACKLASH));
		motorList.add(new Command(Command.Type.MOTOR, 102, Integer.class, Names.Motor.GET_MS));
		motorList.add(new Command(Command.Type.MOTOR, 103, Integer.class, Names.Motor.GET_DIR));
		motorList.add(new Command(Command.Type.MOTOR, 104, Integer.class, Names.Motor.GET_MAX_STEP_RATE));
		motorList.add(new Command(Command.Type.MOTOR, 105, Integer.class, Names.Motor.GET_END));
		motorList.add(new Command(Command.Type.MOTOR, 106, Integer.class, Names.Motor.GET_POS));
		motorList.add(new Command(Command.Type.MOTOR, 107, Boolean.class, Names.Motor.IS_RUNNING));
		motorList.add(new Command(Command.Type.MOTOR, 108, Float.class, Names.Motor.GET_SPEED));
		motorList.add(new Command(Command.Type.MOTOR, 109, Float.class, Names.Motor.GET_ACCEL));
		motorList.add(new Command(Command.Type.MOTOR, 110, Integer.class, Names.Motor.GET_EASING));
		motorList.add(new Command(Command.Type.MOTOR, 111, Integer.class, Names.Motor.GET_START));
		motorList.add(new Command(Command.Type.MOTOR, 112, Integer.class, Names.Motor.GET_STOP));
		motorList.add(new Command(Command.Type.MOTOR, 113, Integer.class, Names.Motor.GET_TRAVEL));
		motorList.add(new Command(Command.Type.MOTOR, 114, Integer.class, Names.Motor.GET_LEAD_IN));
		motorList.add(new Command(Command.Type.MOTOR, 115, Integer.class, Names.Motor.GET_PROG_ACCEL));
		motorList.add(new Command(Command.Type.MOTOR, 116, Integer.class, Names.Motor.GET_PROG_DECEL));
		motorList.add(new Command(Command.Type.MOTOR, 117, Boolean.class, Names.Motor.GET_SLEEP));
		motorList.add(new Command(Command.Type.MOTOR, 118, Boolean.class, Names.Motor.IS_SPEED_VALID));
		motorList.add(new Command(Command.Type.MOTOR, 119, Integer.class, Names.Motor.GET_LEAD_OUT));
	}
	
	private static void initCameraCommands(){
		// Commands		
		cameraList.add(new Command(Command.Type.CAMERA, 2, Names.Camera.SET_ENABLE, Integer.class));
		cameraList.add(new Command(Command.Type.CAMERA, 3, Names.Camera.EXPOSE_NOW, Integer.class));
		cameraList.add(new Command(Command.Type.CAMERA, 4, Names.Camera.SET_TRIGGER, Integer.class));
		cameraList.add(new Command(Command.Type.CAMERA, 5, Names.Camera.SET_FOCUS, Integer.class));
		cameraList.add(new Command(Command.Type.CAMERA, 6, Names.Camera.SET_MAX_SHOTS, Integer.class));
		cameraList.add(new Command(Command.Type.CAMERA, 7, Names.Camera.SET_DELAY, Integer.class));
		cameraList.add(new Command(Command.Type.CAMERA, 8, Names.Camera.SET_FOCUS_WITH_SHUTTER));
		cameraList.add(new Command(Command.Type.CAMERA, 9, Names.Camera.SET_MUP, Integer.class));
		cameraList.add(new Command(Command.Type.CAMERA, 10, Names.Camera.SET_INTERVAL));
		cameraList.add(new Command(Command.Type.CAMERA, 11, Names.Camera.SET_TEST_MODE));
		cameraList.add(new Command(Command.Type.CAMERA, 12, Names.Camera.SET_KEEPALIVE));
		
		// Queries		
		cameraList.add(new Command(Command.Type.CAMERA, 100, Integer.class, Names.Camera.IS_ENABLED));
		cameraList.add(new Command(Command.Type.CAMERA, 101, Integer.class, Names.Camera.IS_EXPOSING));
		cameraList.add(new Command(Command.Type.CAMERA, 102, Integer.class, Names.Camera.GET_TRIGGER));
		cameraList.add(new Command(Command.Type.CAMERA, 103, Integer.class, Names.Camera.GET_FOCUS));
		cameraList.add(new Command(Command.Type.CAMERA, 104, Integer.class, Names.Camera.GET_MAX_SHOTS));
		cameraList.add(new Command(Command.Type.CAMERA, 105, Integer.class, Names.Camera.GET_DELAY));
		cameraList.add(new Command(Command.Type.CAMERA, 106, Integer.class, Names.Camera.GET_FOCUS_WITH_SHUTTER));
		cameraList.add(new Command(Command.Type.CAMERA, 107, Integer.class, Names.Camera.GET_MUP));		
		cameraList.add(new Command(Command.Type.CAMERA, 108, Integer.class, Names.Camera.GET_INTERVAL));
		cameraList.add(new Command(Command.Type.CAMERA, 109, Integer.class, Names.Camera.GET_SHOTS));
		cameraList.add(new Command(Command.Type.CAMERA, 110, Integer.class, Names.Camera.GET_TEST_MODE));
		cameraList.add(new Command(Command.Type.CAMERA, 111, Integer.class, Names.Camera.GET_KEEPALIVE));
	}
	
	private static void initKeyFrameCommands(){
		// Commands		
		keyFrameList.add(new Command(Command.Type.KEYFRAME, 10, Integer.class, Names.KeyFrame.SET_AXIS, Integer.class));
		keyFrameList.add(new Command(Command.Type.KEYFRAME, 11, Integer.class, Names.KeyFrame.SET_COUNT, Integer.class));
		keyFrameList.add(new Command(Command.Type.KEYFRAME, 12, Float.class, Names.KeyFrame.SET_ABSCISSA, Integer.class));
		keyFrameList.add(new Command(Command.Type.KEYFRAME, 13, Float.class, Names.KeyFrame.SET_POS, Integer.class));
		keyFrameList.add(new Command(Command.Type.KEYFRAME, 14, Float.class, Names.KeyFrame.SET_VEL, Integer.class));
		keyFrameList.add(new Command(Command.Type.KEYFRAME, 15, Integer.class, Names.KeyFrame.SET_UPDATE_RATE, Integer.class));
		keyFrameList.add(new Command(Command.Type.KEYFRAME, 16, Names.KeyFrame.END_TRANSMISSION));
		keyFrameList.add(new Command(Command.Type.KEYFRAME, 17, Float.class, Names.KeyFrame.SET_CONT_VID_TIME, Integer.class));
		keyFrameList.add(new Command(Command.Type.KEYFRAME, 20, Names.KeyFrame.START_PROGRAM));
		keyFrameList.add(new Command(Command.Type.KEYFRAME, 21, Names.KeyFrame.PAUSE_PROGRAM));
		keyFrameList.add(new Command(Command.Type.KEYFRAME, 22, Names.KeyFrame.STOP_PROGRAM));
		
		// Queries		
		keyFrameList.add(new Command(Command.Type.KEYFRAME, 99, Names.KeyFrame.PRINT_INFO));
		keyFrameList.add(new Command(Command.Type.KEYFRAME, 100, Names.KeyFrame.GET_COUNT, Integer.class));
		keyFrameList.add(new Command(Command.Type.KEYFRAME, 101, Names.KeyFrame.GET_UPDATE_RATE, Integer.class));
		keyFrameList.add(new Command(Command.Type.KEYFRAME, 102, Float.class, Names.KeyFrame.GET_POS_AT, Float.class));
		keyFrameList.add(new Command(Command.Type.KEYFRAME, 103, Float.class, Names.KeyFrame.GET_VEL_AT, Float.class));
		keyFrameList.add(new Command(Command.Type.KEYFRAME, 104, Float.class, Names.KeyFrame.GET_ACCEL_AT, Float.class));
		keyFrameList.add(new Command(Command.Type.KEYFRAME, 105, Names.KeyFrame.IS_VEL_VALID));
		keyFrameList.add(new Command(Command.Type.KEYFRAME, 106, Names.KeyFrame.IS_ACCEL_VALID, Boolean.class));
		keyFrameList.add(new Command(Command.Type.KEYFRAME, 120, Names.KeyFrame.IS_RUNNING, Boolean.class));
		
		keyFrameList.add(new Command(Command.Type.KEYFRAME, 121, Names.KeyFrame.GET_RUN_TIME, Long.class));
		keyFrameList.add(new Command(Command.Type.KEYFRAME, 122, Names.KeyFrame.GET_MAX_RUN_TIME, Long.class));
		keyFrameList.add(new Command(Command.Type.KEYFRAME, 123, Names.KeyFrame.GET_PCT_DONE, Integer.class));
	}
	
	/** 
	 * Fetch command list from type
	 * @param type The type of commands wanted
	 * @return The list of command objects associated with that type
	 */
	private static List<Command> getList(Type type){
		
		if(type == Type.GENERAL){
			return generalList;
		}
		else if(type == Type.MOTOR){
			return motorList;
		}
		else if(type == Type.CAMERA){
			return cameraList;
		}
		else if(type == Type.KEYFRAME){
			return keyFrameList;
		}
		else{
			System.out.println("That is not a supported command type");
			throw new UnsupportedOperationException();
		}
	}
	
	
	private static void help(Type type){				
		if(!Command.listsInitialized){
			Command.initalizeCommands();
		}
		List<Command> thisList = Command.getList(type);
		System.out.println("\n******** " + type + " COMMAND LIST ********");
		for(int i = 0; i < thisList.size(); i++){
			System.out.println(thisList.get(i).getName());
		}
	}
	
	private static void find(Type type, String term){
		term = term.toLowerCase();
		if(!Command.listsInitialized){
			Command.initalizeCommands();
		}
		List<Command> thisList = Command.getList(type);
		System.out.println("\n******** Matching commands ********");
		for(int i = 0; i < thisList.size(); i++){
			Command thisCommand = thisList.get(i);
			if(thisCommand.getName().toLowerCase().indexOf(term) >= 0){
				System.out.println(thisCommand.getName());
			}
		}
	}
	
	/**
	 * Fetch command using name
	 * @param type
	 * @param name
	 * @return
	 */
	public static Command get(Type type, String name) throws UnsupportedOperationException{
		if(!listsInitialized){
			Command.initalizeCommands();
		}
		List<Command> commandList = getList(type);
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
		if(!listsInitialized){
			Command.initalizeCommands();
		}
		List<Command> commandList = getList(type);
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
			return execute(this.subaddr, "0", false);			
		}
	}
	
	public <T>T execute(String dataOrMotor){
		if(this.type == Command.Type.MOTOR){
			int motor = Integer.parseInt(dataOrMotor);
			if(motor < 0 || motor > Consts.MOTOR_COUNT){
				System.out.println("Invalid motor number");
				throw new UnsupportedOperationException();				
			}
			int tempSubaddr = motor + 1;
			return execute(tempSubaddr, "0", false);
		}
		else{
			return execute(this.subaddr, dataOrMotor, true);
		}
	}
	
	public <T>T execute(String motor, String data){
		if(this.type == Command.Type.MOTOR){
			int motorNum = Integer.parseInt(motor);
			if(motorNum < 0 || motorNum > Consts.MOTOR_COUNT){
				System.out.println("Invalid motor number");
				throw new UnsupportedOperationException();				
			}
			int tempSubaddr = motorNum + 1;
			return execute(tempSubaddr, data, true);
		}	
		else{
			System.out.println("This is a non-motor command; a motor number may not be specified");			
			throw new UnsupportedOperationException();				
		} 
	}
	
	@SuppressWarnings("unchecked")
	private <T>T execute(int subaddr, String dataStr, boolean hasData){

		// Notify if data is attached to a command that does not take additional data
		if(dataLength == 0 && hasData){			
			System.out.println("This command does not send additional data");			
			throw new UnsupportedOperationException();
		}
		
		// Parse the data, if necessary
		int data = 0;		
		if(hasData){			
			
			if(dataStr.equals("rand")){
				float speed = (float)(Math.random() * 10000) - 5000;
				data = Float.floatToIntBits(speed);
				System.out.println("Setting speed to: " + speed + " steps/s");
			}
			else if(dataType == Float.class){
				data = Float.floatToIntBits(Float.parseFloat(dataStr));
			}
			else{
				data = (int) Math.round(Float.parseFloat(dataStr));
			}
		}		
	
		//System.out.println("Command out: " + addr + " " + subaddr + " " + command + " " + dataLength + " " + data);
		
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
		//if(debug){
			System.out.println("Command: " + this.name);
		//}
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
