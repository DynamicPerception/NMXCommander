package com.dynamicperception.nmxcommandline.models;

import java.util.List;
import java.util.concurrent.TimeUnit;

import com.dynamicperception.nmxcommandline.helpers.Consts;

public class NMXCmd {
	
	private static boolean debug = true;
	
	private static final int memTraceSize = 20;
	private static int[] memTrace = new int[memTraceSize]; 
	
	private static final float FLOAT_CONVERSION = 100f;
		
	private static int currentKFAxis = 0;
	
	private static String NMX_COM_FAIL = "*************** NMX COMS FAILURE!!! ************************";
	
	private static int addr = 3;
	
	public static void updateMemTrace(){
		
		int freeMem = NMXCmd.General.getFreeMemory();
		
		// Shift the values one space in the array
		for(int i = 0; i < memTrace.length - 1; i++){
			memTrace[i+1] = memTrace[i];
		}
		
		memTrace[0] = freeMem;			
	}
	
	public static void printMemTrace(){
		System.out.println("Memory Trace: ");
		for(int i = memTrace.length-1; i >= 0; i--){
			System.out.println(memTrace[i]);
		}
	}
	
	public static void setDebug(boolean enabled){
		debug = enabled;
	}

	public static class CmdType{
		public static final String GENERAL = "g";
		public static final String MOTOR = "m";
		public static final String CAMERA = "c";
		public static final String KEYFRAME = "k";
	}
	
	public static void execute(String type, String cmd, List<Integer> args){
		int data = 0;
		if(args.size() > 0){
			data = args.get(0);
		}
		if(type.equals(CmdType.GENERAL)){			
			General.command(cmd, data);
		}
		else if(type.equals(CmdType.MOTOR)){
			Motor.command(cmd, args);
		}
		else if(type.equals(CmdType.CAMERA)){
			Camera.command(cmd, data);
		}
		else if(type.equals(CmdType.KEYFRAME)){
			KeyFrame.command(cmd, data);
		}
	}
	
	// General commands
	public static class General {
		
		public static class Names{
			// General settings
			static final String SET_ADDRESS 	= "setaddress";
			static final String SET_DEBUG		= "setdebug";
			static final String TOGGLE_LED	 	= "led";			
			static final String SET_JOYSTICK	= "setjoystick";
			static final String SET_GRAFFIK		= "setgraffik";			
			
			// General queries
			static final String GET_FIRMWARE	= "getfirmware";
			static final String GET_MEMORY	 	= "getmemory";
			static final String GET_VOLTAGE 	= "getvoltage";
			static final String GET_CURRENT 	= "getcurrent";
			static final String GET_MOT_ATTCH	=  "getmotorattach";			
			static final String GET_JOYSTICK	 =  "getjoystickmode";			
			static final String GET_WATCHDOG	=  "getwatchdog";
			
			// Program settings
			static final String SET_MODE		= "setmode";
			static final String SET_PINGPONG 	= "setpingpong";
			static final String SET_FPS			= "setfps";			
			
			// Program control
			static final String START			= "start";
			static final String PAUSE			= "pause";			
			static final String STOP 			= "stop	";
			
			// Program queries
			static final String GET_PROGRAM_MODE 	=  "getprogrammode";			
			static final String GET_FPS 			= "getfps";			
			static final String GET_PROGRAM_DELAY 	=  "getprogramdelay";
			static final String GET_PROGRAM_TIME 	=  "getprogramtime";
			static final String GET_PINGPONG		=  "getpingpong";
			static final String GET_RUN_STATUS 		= "getrunstatus";
			static final String GET_PCT_COMP		=  "getpercentcomplete";		
			

			
			public static void help(){
				System.out.println("\n***** General Command Directory *****\n");
				System.out.println("General settings: \n" + SET_ADDRESS + "\n" + SET_DEBUG + "\n" + TOGGLE_LED + "\n" + SET_JOYSTICK + "\n" + SET_GRAFFIK + "\n");
				System.out.println("General queries: \n" + GET_FIRMWARE + "\n" + GET_MEMORY + "\n" + GET_VOLTAGE + "\n" + GET_CURRENT + "\n" + GET_MOT_ATTCH + "\n" + GET_JOYSTICK + "\n" + GET_WATCHDOG + "\n");
				System.out.println("Program settings: \n" + SET_MODE + "\n" + SET_PINGPONG + "\n" + SET_FPS + "\n");
				System.out.println("Program control: \n" + START + "\n" + PAUSE + "\n" + STOP + "\n");
				System.out.println("Program queries: \n" + GET_PROGRAM_MODE + "\n" + GET_FPS + "\n" + GET_PROGRAM_DELAY + "\n" + GET_PROGRAM_TIME +
						"\n" + GET_PINGPONG + "\n" + GET_RUN_STATUS + "\n" + GET_PCT_COMP);	
			}			
		}		
		
		public static void command(String cmd, int data){
			
			if(cmd.equals(Names.SET_ADDRESS)){
				General.setAddress(data);
			}
			else if(cmd.equals(Names.SET_DEBUG)){
				General.toggleDebugMode(data);
			}
			else if(cmd.equals(Names.TOGGLE_LED)){
				General.toggleLED();
			}
			else if(cmd.equals(Names.SET_JOYSTICK)){
				boolean outVal = data == 0 ? false : true;
				General.setJoystickMode(outVal);
			}
			else if(cmd.equals(Names.SET_GRAFFIK)){
				boolean outVal = data == 0 ? false : true;
				General.setGraffikMode(outVal);
				
			}
			else if(cmd.equals(Names.GET_FIRMWARE)){
				General.getFirmwareVersion();
			}
			else if(cmd.equals(Names.GET_MEMORY)){
				General.getFreeMemory();
			}
			else if(cmd.equals(Names.GET_VOLTAGE)){
				General.getVoltage();
			}
			else if(cmd.equals(Names.GET_CURRENT)){
				General.getCurrent();
			}
			else if(cmd.equals(Names.GET_MOT_ATTCH)){
				General.getMotorAttachment();
			}
			else if(cmd.equals(Names.GET_JOYSTICK)){
				General.getJoystickMode();
			}
			else if(cmd.equals(Names.GET_WATCHDOG)){
				General.getWatchdogMode();
			}
			else if(cmd.equals(Names.SET_MODE)){				
				General.setProgramMode(data);
			}
			else if(cmd.equals(Names.SET_PINGPONG)){
				boolean outVal = data == 0 ? false : true;
				General.setPingPongFlag(outVal);
				
			}
			else if(cmd.equals(Names.SET_FPS)){
				General.setFPS(data);
			}
			else if(cmd.equals(Names.START)){
				General.startProgram();
			}
			else if(cmd.equals(Names.PAUSE)){
				General.pauseProgram();
			}
			else if(cmd.equals(Names.STOP)){
				General.stopProgram();
			}
			else if(cmd.equals(Names.GET_PROGRAM_MODE)){
				General.getProgramMode();
			}
			else if(cmd.equals(Names.GET_FPS)){
				General.getFPS();
			}
			else if(cmd.equals(Names.GET_PROGRAM_DELAY)){
				General.getProgramDelay();
			}
			else if(cmd.equals(Names.GET_PROGRAM_TIME)){
				General.getProgramRunTime();
			}
			else if(cmd.equals(Names.GET_PINGPONG)){
				General.getPingpongMode();
			}
			else if(cmd.equals(Names.GET_RUN_STATUS)){
				General.getRunStatus();
			}
			else if(cmd.equals(Names.GET_PCT_COMP)){
				General.getPercentComplete();
			}
			

		}

		public static void startProgram() {
			
			// If a command is currently being sent, wait
			waitForNMX();
			

			int subaddr = 0;
			int command = 2;
			if(debug)
				System.out.println("Starting legacy program");
			NMXComs.cmd(addr, subaddr, command);
		}

		public static void pauseProgram() {
			
			// If a command is currently being sent, wait
			waitForNMX();
			
			
			int subaddr = 0;
			int command = 3;
			if(debug)
				System.out.println("Pausing legacy program");
			NMXComs.cmd(addr, subaddr, command);
		}

		public static void stopProgram() {
			
			// If a command is currently being sent, wait
			waitForNMX();
			

			int subaddr = 0;
			int command = 4;
			if(debug)
				System.out.println("Stopping legacy program");
			NMXComs.cmd(addr, subaddr, command);
		}

		public static void toggleLED() {
			
			// If a command is currently being sent, wait
			waitForNMX();
			

			int subaddr = 0;
			int command = 5;
			if(debug)
				System.out.println("Toggling debug LED");
			NMXComs.cmd(addr, subaddr, command);
		}

		public static void setTimingMaster() {
			
			// If a command is currently being sent, wait
			waitForNMX();
			

			int subaddr = 0;
			int command = 6;
			if(debug)
				System.out.println("Setting timing master");
			NMXComs.cmd(addr, subaddr, command);
		}

		/*
		 * public static void setStoredName(String _name){
		 * 
		 * int subaddr = 0; int command = 7; byte[] data = _name.getBytes();
		 * 
		 * 
		 * NMXComs.cmd(addr, subaddr, command); }
		 */

		public static void setAddress(int address) {
			
			// If a command is currently being sent, wait
			waitForNMX();
			

			int subaddr = 0;
			int command = 8;
			int length = Consts.BYTE_SIZE;
			byte data = (byte) address;
			if(debug)
				System.out.println("Setting address: " + address);
			NMXComs.cmd(addr, subaddr, command, length, data);
		}

		public static void setStepPulseCommonLine(int line) {
			
			// If a command is currently being sent, wait
			waitForNMX();
			

			int subaddr = 0;
			int command = 9;
			int length = Consts.BYTE_SIZE;
			byte data = (byte) line;
			if(debug)
				System.out.println("Setting step pulsing common line: " + line);
			NMXComs.cmd(addr, subaddr, command, length, data);
		}

		public static void sendAllMotorsHome() {
			
			if(debug)
				System.out.println("Sending all motors home");
			
			for(int i = 0; i < Consts.MOTOR_COUNT; i++)
				NMXCmd.Motor.sendToHome(i);			
		}

		public static void setMaxMotorStepRate(int maxStepRate) {
			
			// If a command is currently being sent, wait
			waitForNMX();
			

			int subaddr = 0;
			int command = 11;
			int length = Consts.INT_SIZE;
			int data = maxStepRate;
			if(debug)
				System.out.println("Setting max step rate for all motors: " + maxStepRate + " steps/s");
			NMXComs.cmd(addr, subaddr, command, length, data);
		}
		
		public static void setProgramMode(int mode) {
			
			// If a command is currently being sent, wait
			waitForNMX();
			

			int subaddr = 0;
			int command = 22;
			int length = Consts.BYTE_SIZE;
			int data = mode;
			if(debug)
				System.out.println("Setting program mode: " + data);
			NMXComs.cmd(addr, subaddr, command, length, data);
		}

		/**
		 * Sets on which edge of the input signal the auxiliary action is
		 * executed
		 * 
		 * @param edge
		 *            Acceptable inputs: <b>0</b> = Rising, <b>1</b> = Falling,
		 *            <b>2</b> = Change
		 */
		public static void setAuxInputEdge(int edge) {
			
			// If a command is currently being sent, wait
			waitForNMX();
			

			// Reject illegal settings
			if (edge < 0 || edge > 2)
				throw new IllegalArgumentException("edge must be 0, 1, or 2");

			int subaddr = 0;
			int command = 12;
			int length = Consts.BYTE_SIZE;
			byte data = (byte) edge;
			String which = edge == 0 ? "RISING" : edge == 1 ? "FALLING" : "CHANGE";
			if(debug)
				System.out.println("Setting aux input edge: " + which);
			NMXComs.cmd(addr, subaddr, command, length, data);
		}

		public static void setJoystickMode(boolean enable_state) {
			
			// If a command is currently being sent, wait
			waitForNMX();			

			int subaddr = 0;
			int command = 23;
			int size = Consts.BYTE_SIZE;
			byte data = enable_state ? (byte) 1 : 0;
			if(debug)
				System.out.println("Setting joystick mode: " + enable_state);			
			NMXComs.cmd(addr, subaddr, command, size, data);
		}
		
		public static void setPingPongFlag(boolean enable_state) {
			
			// If a command is currently being sent, wait
			waitForNMX();			

			int subaddr = 0;
			int command = 24;
			int size = Consts.BYTE_SIZE;
			byte data = enable_state ? (byte) 1 : 0;			
			if(debug)
				System.out.println("Setting ping-pong mode: " + enable_state);			
			NMXComs.cmd(addr, subaddr, command, size, data);
		}
		
		public static void sendAllMotorsToStart() {
						
			// If a command is currently being sent, wait
			waitForNMX();			

			int subaddr = 0;
			int command = 25;			
			if(debug)
				System.out.println("Sending all motors to program start");			
			NMXComs.cmd(addr, subaddr, command);
		}
		
		/**
		 * Sets frames per second flag on controller
		 */
		public static void setFPS(int fps) {
			
			if(fps != 24 && fps != 25 && fps != 30){
				System.out.println("Invalid frames per second setting");
				return;
			}
			
			// If a command is currently being sent, wait
			waitForNMX();			

			int subaddr = 0;
			int command = 28;			
			int size = Consts.BYTE_SIZE;
			int data = fps == 30 ? 0 : fps == 24 ? 1 : 2;
			if(debug)
				System.out.println("Setting FPS to " + fps + " -- Flag val: " + data);			
			NMXComs.cmd(addr, subaddr, command, size, data);
		}
				
		public static void setGraffikMode(boolean enable_state) {
			
			// If a command is currently being sent, wait
			waitForNMX();			

			int subaddr = 0;
			int command = 50;
			int size = Consts.BYTE_SIZE;
			byte data = enable_state ? (byte) 1 : 0;			
			if(debug)
				System.out.println("Setting Graffik mode: " + enable_state);			
			NMXComs.cmd(addr, subaddr, command, size, data);
		}
				
		public static int getFirmwareVersion(){
			waitForNMX();
			
			int subaddr = 0;
			int command = 100;
			
			NMXComs.cmd(addr, subaddr, command);
			int ret = NMXComs.getResponseVal();
			if(debug)
				System.out.println("Querying firmware version -- Addr: " + addr + " Version: " + ret);
			return ret;
		}
		
		public static int getRunStatus(){
			waitForNMX();
			
			int subaddr = 0;
			int command = 101;
			
			NMXComs.cmd(addr, subaddr, command);
			int ret = NMXComs.getResponseVal();
			if(debug)
				System.out.println("Querying run status -- Addr: " + addr + " Status: " + ret);
			return ret;
		}
		
		public static float getVoltage(){
			waitForNMX();
			
			int subaddr = 0;
			int command = 107;
			
			NMXComs.cmd(addr, subaddr, command);
			float ret = (float)NMXComs.getResponseVal() / FLOAT_CONVERSION;
			if(debug)
				System.out.println("Querying controller voltage -- Addr: " + addr + " Version: " + ret + "V");
			return ret;
		}
		
		public static float getCurrent(){
			waitForNMX();
			
			int subaddr = 0;
			int command = 108;
			
			NMXComs.cmd(addr, subaddr, command);
			float ret = (float)NMXComs.getResponseVal() / FLOAT_CONVERSION;
			if(debug)
				System.out.println("Querying current voltage -- Addr: " + addr + " Current: " + ret + "A");
			return ret;
		}
		
		public static float getProgramDelay(){
			waitForNMX();
			
			int subaddr = 0;
			int command = 117;
			
			NMXComs.cmd(addr, subaddr, command);
			int ret = NMXComs.getResponseVal();
			if(debug)
				System.out.println("Querying program delay-- Addr: " + addr + " Delay: " + ret + "ms");
			return ret;
		}
		
		public static int getProgramMode(){
			// If a command is currently being sent, wait
			waitForNMX();			

			int subaddr = 0;
			int command = 118;
					
			NMXComs.cmd(addr, subaddr, command);
			waitForNMX();
			int ret = NMXComs.getResponseVal();
			if(debug){
				System.out.print("Querying program mode: ");			
				switch(ret){
					case 0:
						System.out.println("SMS");
						break;
					case 1:
						System.out.println("Cont TL");
						break;
					case 2:
						System.out.println("Vid");
						break;
				}
			}
			return ret;
		}
		
		public static boolean getJoystickMode(){
			waitForNMX();
			
			int subaddr = 0;
			int command = 120;
			
			NMXComs.cmd(addr, subaddr, command);
			boolean ret = NMXComs.getResponseVal() == 0 ? false : true;
			if(debug)
				System.out.println("Querying joystick mode -- Addr: " + addr + " Mode: " + ret);
			return ret;
		}
		
		public static boolean getPingpongMode(){
			waitForNMX();
			
			int subaddr = 0;
			int command = 121;
			
			NMXComs.cmd(addr, subaddr, command);
			boolean ret = NMXComs.getResponseVal() == 0 ? false : true;
			if(debug)
				System.out.println("Querying pingpong mode -- Addr: " + addr + " Mode: " + ret);
			return ret;
		}
		
		public static boolean getWatchdogMode(){
			waitForNMX();
			
			int subaddr = 0;
			int command = 122;
			
			NMXComs.cmd(addr, subaddr, command);
			boolean ret = NMXComs.getResponseVal() == 0 ? false : true;
			if(debug)
				System.out.println("Querying watchdog mode -- Addr: " + addr + " Mode: " + ret);
			return ret;
		}
		
		public static int getPercentComplete(){
			waitForNMX();
			
			int subaddr = 0;
			int command = 123;
			
			NMXComs.cmd(addr, subaddr, command);
			int ret = NMXComs.getResponseVal();
			if(debug)
				System.out.println("Querying percent complete -- Addr: " + addr + " Completion: " + ret + "%");
			return ret;
		}
		
		public static int getMotorAttachment(){
			waitForNMX();
			
			int subaddr = 0;
			int command = 124;
			
			NMXComs.cmd(addr, subaddr, command);
			int ret = NMXComs.getResponseVal();
			if(debug)
				System.out.println("Querying motor attachment -- Addr: " + addr + " Attachment code: " + ret);
			return ret;
		}						
		
		public static int getProgramRunTime(){
			waitForNMX();
			
			int subaddr = 0;
			int command = 125;
			
			NMXComs.cmd(addr, subaddr, command);
			int ret = NMXComs.getResponseVal();
			if(debug)
				System.out.println("Querying program run time -- Addr: " + addr + " Run time: " + ret + "ms");
			return ret;
		}
		
		public static boolean getProgramComplete() {
							
			// If a command is currently being sent, wait
			waitForNMX();

			int subaddr = 0;
			int command = 126;			
			NMXComs.cmd(addr, subaddr, command);
			waitForNMX();
			boolean ret;
			if(NMXComs.getResponseVal() > 0)
				ret = true;
			else
				ret = false;
			
			if(debug)
				System.out.println("Querying program complete -- Addr: " + addr + " Complete: " + ret);
			return ret;
		}
		
		public static int getFPS(){
			// If a command is currently being sent, wait
			waitForNMX();			

			int subaddr = 0;
			int command = 127;
					
			NMXComs.cmd(addr, subaddr, command);
			waitForNMX();
			int resp = NMXComs.getResponseVal();
			int ret = 0;
			switch(resp){
				case 0:
					ret = 30;
					break;
				case 1:
					ret = 24;
					break;
				case 2:
					ret = 25;
					break;
			}
			if(debug)
				System.out.println("Querying FPS: " + ret + " -- Flag val: " + resp);
			return ret;
		}
		
		public static int getFreeMemory(){
			// If a command is currently being sent, wait
			waitForNMX();			

			int subaddr = 0;
			int command = 200;
			
			if(debug)
				System.out.println("Querying NMX free memory");
			NMXComs.cmd(addr, subaddr, command);
			waitForNMX();
			return NMXComs.getResponseVal();			
		}

		public static void toggleDebugMode(int mode) {
			
			// If a command is currently being sent, wait
			waitForNMX();
			

			int subaddr = 0;
			int command = 254;
			int length = Consts.BYTE_SIZE;
			byte data = (byte) mode;
			if(debug)
				System.out.println("Toggleing debug mode");
			NMXComs.cmd(addr, subaddr, command, length, data);

		}

		public static class Debug {
			public static final int COMM = 1;
			public static final int STEPS = 2;
			public static final int MOTOR = 4;
			public static final int GEN = 8;
			public static final int FUNCTION = 16;
			public static final int CONFIRM = 32;
		}

	}

	// Motor commands
	public static class Motor {
		
		public static class Names{
			static final String SET_SLEEP 		= "setsleep";
			static final String SET_ENABLE 		= "setenable";			
			static final String SET_BACKLASH 	= "setbacklash";
			static final String SET_MICROSTEPS 	= "setms";			
			
			static final String RESET_LIMITS 	= "resetlimits";
			static final String SET_HOME 		= "sethome";		
			static final String SET_END_HERE 	= "setendhere";
			static final String SET_START_HERE	= "setstarthere";
			static final String SET_STOP_HERE 	= "setstophere";
			
			static final String SEND_HOME 		= "sendhome";
			static final String SEND_END 		= "sendend";
			static final String SEND_START 		= "sendstart";
			static final String SEND_STOP		= "sendstop";
			static final String SEND_TO 		= "sendto";
			
			static final String STOP_MOTOR 		= "stop";
			static final String SET_MAXSPEED 	= "setmaxspeed";
			static final String SET_DIR 		= "setdir";
			static final String SET_SPEED		= "setspeed";
			static final String SET_ACCEL		= "setaccel";						
			
			static final String IS_RUNNING 		= "isrunning";
			static final String GET_ENABLE 		= "isenabled";			
			static final String GET_BACKLASH 	= "getbacklash";
			static final String GET_MS 			= "getms";
			static final String GET_END 		= "getend";
			static final String GET_POS 		= "getpos";			
			static final String GET_SPEED 		= "getspeed";
			static final String GET_ACCEL 		= "getaccel";
			static final String GET_START 		= "getstart";
			static final String GET_STOP 		= "getstop";
			static final String GET_TRAVEL_TIME = "gettraveltime";
			static final String GET_SLEEP 		= "getsleep";
			
			public static void help(){
				System.out.println("\n***** Motor Command Directory *****\n");
				System.out.println("General settings: \n" + SET_SLEEP + "\n" + SET_ENABLE + "\n" + SET_BACKLASH + "\n" + SET_MICROSTEPS + "\n");
				System.out.println("Limits settings: \n" + RESET_LIMITS + "\n" + SET_HOME + "\n" + SET_END_HERE + "\n" + SET_START_HERE + "\n" + SET_STOP_HERE + "\n");
				System.out.println("Go to commands: \n" + SEND_HOME + "\n" + SEND_END + "\n" + SEND_START + "\n" + SEND_STOP + "\n" + SEND_TO +	"\n");
				System.out.println("Movement commands: \n" + STOP_MOTOR + "\n" + SET_MAXSPEED + "\n" + SET_DIR + "\n" + SET_SPEED + "\n" + SET_ACCEL + "\n");
				System.out.println("Status queries: \n" + IS_RUNNING + "\n" + GET_ENABLE + "\n" + GET_BACKLASH + "\n" + GET_MS + "\n" + GET_END + "\n" + GET_POS + 
						"\n" + GET_SPEED + "\n" + GET_ACCEL + "\n" + GET_START + "\n" + GET_STOP + "\n" + GET_TRAVEL_TIME + "\n" + GET_SLEEP);	
			}			
		}

		
		public static void command(String cmd, List<Integer> args){
			final int ALL_MOTORS = 3;
						
			float floatMot = -1;
			if(args.size() > 0){
				floatMot = args.get(0);
				// Trim the motor value from the arguments list
				args.remove(0);
			}
			int mot = (int) floatMot;	
			
			// Don't allow illegal motor vals					
			mot = mot > 2 ? mot = 3 : mot < -1 ? mot = -1 : mot;

			// If all motors were requested, repeat the command for each one
			int commandCount = 1;
			if(mot == ALL_MOTORS){
				commandCount = Consts.MOTOR_COUNT;
				for(int i = 0; i < commandCount; i++){
					command(cmd, i, args);
				}
			}
			else{
				command(cmd, mot, args);
			}
		}
		
		private static void command(String cmd, int motor, List<Integer> args){			
			
			int data = 0;
			if(args.size() > 0)
				data = args.get(0);
			
			if(cmd.equals(Names.SET_SLEEP)){
				boolean sleep = true;				
				sleep = data == 0 ? false : true;				
				Motor.setSleep(motor, sleep);
			}
			else if(cmd.equals(Names.GET_SLEEP)){
				Motor.getSleepState(motor);
			}
			else if(cmd.equals(Names.SET_ENABLE)){
				boolean enable = true;
				enable = data == 0 ? false : true;
				Motor.setEnable(motor, enable);
			}
			else if(cmd.equals(Names.GET_ENABLE)){
				Motor.getEnableStatus(motor);
			}
			else if(cmd.equals(Names.STOP_MOTOR)){
				Motor.stopMotor(motor);
			}
			else if(cmd.equals(Names.SET_BACKLASH)){				
				Motor.setBacklash(motor, data);
			}
			else if(cmd.equals(Names.SET_MICROSTEPS)){
				Motor.setMicrosteps(motor, data);
			}
			else if(cmd.equals(Names.SET_MAXSPEED)){
				Motor.setMaxSpeed(motor, data);
			}
			else if(cmd.equals(Names.SET_DIR)){
				Motor.setDir(motor, data);
			}
			else if(cmd.equals(Names.SET_HOME)){
				Motor.setHomeHere(motor);
			}
			else if(cmd.equals(Names.SET_END_HERE)){
				Motor.setEndHere(motor);
			}
			else if(cmd.equals(Names.SEND_HOME)){
				Motor.sendToHome(motor);
			}
			else if(cmd.equals(Names.SEND_END)){
				Motor.sendToEnd(motor);
			}
			else if(cmd.equals(Names.SET_SPEED)){
				try {
					Motor.setSpeed(motor, data);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else if(cmd.equals(Names.SET_ACCEL)){
				Motor.setContAccelDecel(motor, data);
			}
			else if(cmd.equals(Names.SEND_START)){
				Motor.sendToStart(motor);
			}
			else if(cmd.equals(Names.SEND_STOP)){
				Motor.sendToStop(motor);
			}
			else if(cmd.equals(Names.RESET_LIMITS)){
				Motor.resetLimits(motor);
			}
			else if(cmd.equals(Names.SET_START_HERE)){
				Motor.setStartHere(motor);
			}
			else if(cmd.equals(Names.SET_STOP_HERE)){
				Motor.setStopHere(motor);
			}
			else if(cmd.equals(Names.SEND_TO)){
				Motor.sendToRaw(motor, data);
			}
			else if(cmd.equals(Names.GET_BACKLASH)){
				Motor.getBacklashSteps(motor);
			}
			else if(cmd.equals(Names.GET_MS)){
				Motor.getMicrosteps(motor);
			}
			else if(cmd.equals(Names.GET_END)){
				Motor.getEndPosition(motor);
			}
			else if(cmd.equals(Names.GET_POS)){
				Motor.getCurrentPosition(motor);
			}
			else if(cmd.equals(Names.IS_RUNNING)){
				try {
					Motor.isRunning(motor);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}			
			else if(cmd.equals(Names.GET_SPEED)){
				Motor.getContinuousSpeed(motor);
			}
			else if(cmd.equals(Names.GET_ACCEL)){
				Motor.getAccelDecel(motor);
			}
			else if(cmd.equals(Names.GET_START)){
				Motor.getStart(motor);
			}
			else if(cmd.equals(Names.GET_STOP)){
				Motor.getStop(motor);
			}
			else if(cmd.equals(Names.GET_TRAVEL_TIME)){
				Motor.getTravelTime(motor);
			}			
		}

		public static void sendAllMotorsHome() {
			
			NMXCmd.General.sendAllMotorsHome();
		}
						
		public static void setSleep(int motor, boolean sleep_state) {
			
			if(motor == -1){
				System.out.println("setSleep(int motor, boolean sleep_state)");
				return;
			}
			
			// If a command is currently being sent, wait
			waitForNMX();
			

			validateMotor(motor);

			int subaddr = motor + 1;
			int command = 2;
			int size = Consts.BYTE_SIZE;
			byte data = sleep_state ? (byte) 1 : 0;
			if(debug)
				System.out.println("Setting motor " + motor + " sleep state: " + sleep_state);
			NMXComs.cmd(addr, subaddr, command, size, data);
		}
				
		public static void setEnable(int motor, boolean enable_state) {
			
			if(motor == -1){
				System.out.println("setEnable(int motor, boolean enable_state)");
				return;
			}
			
			// If a command is currently being sent, wait
			waitForNMX();
			

			validateMotor(motor);

			int subaddr = motor + 1;
			int command = 3;
			int size = Consts.BYTE_SIZE;
			byte data = enable_state ? (byte) 1 : 0;
			if(debug)
				System.out.println("Setting motor " + motor + " enable state: " + enable_state);
			NMXComs.cmd(addr, subaddr, command, size, data);
		}
		
		public static void stopMotor(int motor) {
			
			if(motor == -1){				
				System.out.println("stopMotor(int motor)");				
				return;
			}
			
			// If a command is currently being sent, wait
			waitForNMX();
			

			validateMotor(motor);

			int subaddr = motor + 1;
			int command = 4;
			if(debug)
				System.out.println("Stopping motor " + motor);
			NMXComs.cmd(addr, subaddr, command);
		}
				
		public static void setBacklash(int motor, int backlash) {
			
			if(motor == -1){				
				System.out.println("setBacklash(int motor, int backlash)");				
				return;
			}
			
			// If a command is currently being sent, wait
			waitForNMX();
			

			validateMotor(motor);

			int subaddr = motor + 1;
			int command = 5;
			int size = Consts.INT_SIZE;
			int data = backlash;
			if(debug)
				System.out.println("Setting motor " + motor + " backlash: " + backlash + " steps");
			NMXComs.cmd(addr, subaddr, command, size, data);			
		}
				
		public static void setMicrosteps(int motor, int ms) {
			
			if(motor == -1){				
				System.out.println("setMicrosteps(int motor, int ms)");				
				return;
			}
			
			// If a command is currently being sent, wait
			waitForNMX();
			

			// Reject illegal settings
			if (ms != 4 && ms != 8 && ms != 16)
				throw new IllegalArgumentException(
						"Requested microsteps: " + ms + " Valid microstep settings are 4, 8, and 16");
			validateMotor(motor);

			int subaddr = motor + 1;
			int command = 6;
			int size = Consts.BYTE_SIZE;
			byte data = (byte) ms;
			if(debug)
				System.out.println("Setting motor " + motor + " microsteps: " + ms);
			NMXComs.cmd(addr, subaddr, command, size, data);
			waitForNMX();
		}
				
		public static void setMaxSpeed(int motor, float max_speed) {
			
			if(motor == -1){				
				System.out.println("setMaxSpeed(int motor, float max_speed)");				
				return;
			}
			
			// If a command is currently being sent, wait
			waitForNMX();
			

			validateMotor(motor);

			int subaddr = motor + 1;
			int command = 7;
			int size = Consts.INT_SIZE;
			int data = (int) max_speed;
			if(debug)
				System.out.println("Setting motor " + motor + " max speed: " + max_speed + " steps/s");
			NMXComs.cmd(addr, subaddr, command, size, data);
		}
				
		public static void setDir(int motor, int dir) {
			
			if(motor == -1){				
				System.out.println("setDir(int motor, int dir)");				
				return;
			}
			
			// If a command is currently being sent, wait
			waitForNMX();
			

			validateMotor(motor);

			int subaddr = motor + 1;
			int command = 8;
			int size = Consts.BYTE_SIZE;
			byte data = (byte) dir;
			if(debug)
				System.out.println("Setting motor " + motor + " direction: " + dir);
			NMXComs.cmd(addr, subaddr, command, size, data);
		}
		
		public static void setHomeHere(int motor){
			
			if(motor == -1){				
				System.out.println("setHomeHere(int motor)");				
				return;
			}
			
			// This command causes the NMX to write to EEPROM, which takes longer,
			// so give a longer response time
			setHomeHere(motor, NMXComs.ResponseTiming.EEPROM);
		}

		public static void setHomeHere(int motor, int waitTime) {
			
			// If a command is currently being sent, wait
			waitForNMX();
			

			validateMotor(motor);

			int subaddr = motor + 1;
			int command = 9;
			
			NMXComs.setResponseDelay(waitTime);
			
			if(debug)
				System.out.println("Setting motor " + motor + " home here");			
			NMXComs.cmd(addr, subaddr, command);
			waitForNMX();
		}

		public static void setEndHere(int motor) {
			
			if(motor == -1){				
				System.out.println("setEndHere(int motor)");				
				return;
			}
			
			// If a command is currently being sent, wait
			waitForNMX();
			

			validateMotor(motor);

			// TODO Identify problem causing controller to lock after setting end point
			int subaddr = motor + 1;
			int command = 10;
			
			NMXComs.setResponseDelay(NMXComs.ResponseTiming.EEPROM);
			
			if(debug)
				System.out.println("Setting motor " + motor + " end here");
			NMXComs.cmd(addr, subaddr, command);
			waitForNMX();
		}
		
		public static void sendToHome(int motor) {
			
			if(motor == -1){				
				System.out.println("sendToHome(int motor)");				
				return;
			}
			
			// If a command is currently being sent, wait
			waitForNMX();
			

			validateMotor(motor);

			int subaddr = motor + 1;
			int command = 11;
			if(debug)
				System.out.println("Sending motor " + motor + " home limit");
			NMXComs.cmd(addr, subaddr, command);
		}
		
		public static void sendToEnd(int motor) {
			
			if(motor == -1){				
				System.out.println("sendToEnd(int motor)");				
				return;
			}
			
			// If a command is currently being sent, wait
			waitForNMX();
			

			validateMotor(motor);

			int subaddr = motor + 1;
			int command = 12;
			if(debug)
				System.out.println("Setting motor " + motor + " to end limit");
			NMXComs.cmd(addr, subaddr, command);
		}
		
		/**
		 * Sets motor's continuous speed. If running in joystick mode, use
		 * positive and negative numbers to set direction
		 * 
		 * @param motor
		 *            Motor number <b>0</b>, <b>1</b>, or <b>2</b>
		 * @param speed
		 *            Speed in steps/s.
		 */				
		public static void setSpeed(int motor, float speed) throws InterruptedException {
			
			if(motor == -1){				
				System.out.println("setSpeed(int motor, float speed)");				
				return;
			}
			
			// If a command is currently being sent, wait
			waitForNMX();
			

			validateMotor(motor);

			int subaddr = motor + 1;
			int command = 13;
			int size = Consts.FLOAT_SIZE;
			int data = Float.floatToIntBits(speed);
			if(debug)
				System.out.println("Setting motor " + motor + " speed: " + speed + " steps/s");			
			NMXComs.cmd(addr, subaddr, command, size, data, false, true);
			waitForNMX();
		}
		
		/**
		 * Accel/decel used for smoothing speed changes for continuous motion
		 * (e.g. joystick mode)
		 * 
		 * @param motor
		 *            Motor number 0, 1, or 2
		 * @param accel
		 *            Rate in steps/sec^2
		 */
		public static void setContAccelDecel(int motor, float accel_decel) {
			
			if(motor == -1){				
				System.out.println("setContAccelDecel(int motor, float accel_decel)");				
				return;
			}
			
			// If a command is currently being sent, wait
			waitForNMX();
			
			validateMotor(motor);

			int subaddr = motor + 1;
			int command = 14;
			int size = Consts.FLOAT_SIZE;
			int data = Float.floatToIntBits(accel_decel);
			if(debug)
				System.out.println("Setting motor " + motor + " accel/decel: " + accel_decel + " steps/s^2");
			NMXComs.cmd(addr, subaddr, command, size, data);
		}
		
		public static void simpleMove(int motor, boolean forward, int steps){			
			
			// If a command is currently being sent, wait
			waitForNMX();
			
			validateMotor(motor);

			int subaddr = motor + 1;
			int command = 14;
			int size = Consts.BYTE_SIZE + Consts.LONG_SIZE;
			int data = steps;
			// This command require a data packet with 1 byte indicating direction followed by a 4 byte long
			// integer indicating the number of steps to move. The direction byte needs to be inserted using the
			// "manualData" string.
			if(forward)
				NMXComs.setManualData("01");
			else
				NMXComs.setManualData("00");
			if(debug)
				System.out.println("Starting motor " + motor + " simple move -- Forward?: " + forward + " Steps: " + steps);
			NMXComs.cmd(addr, subaddr, command, size, data);
		}
				
		public static void sendToStart(int motor){
			
			if(motor == -1){				
				System.out.println("sendToStart(int motor)");				
				return;
			}
			
			// If a command is currently being sent, wait
			waitForNMX();
			
			int subaddr = motor + 1;
			int command = 23;			
			if(debug)
				System.out.println("Sending motor " + motor + " to start");
			NMXComs.cmd(addr, subaddr, command);			
		}
				
		public static void sendToStop(int motor){
			
			if(motor == -1){				
				System.out.println("sendToStop(int motor)");				
				return;
			}
			
			// If a command is currently being sent, wait
			waitForNMX();
			
			int subaddr = motor + 1;
			int command = 24;			
			if(debug)
				System.out.println("Sending motor " + motor + " to stop");
			NMXComs.cmd(addr, subaddr, command);			
		}
				
		public static void resetLimits(int motor){
			
			if(motor == -1){				
				System.out.println("resetLimits(int motor)");				
				return;
			}
			
			// If a command is currently being sent, wait
			waitForNMX();
			NMXComs.setResponseDelay(NMXComs.ResponseTiming.EEPROM);
			int subaddr = motor + 1;
			int command = 27;			
			if(debug)
				System.out.println("Resetting motor " + motor + " limits");
			NMXComs.cmd(addr, subaddr, command);			
		}
		
		public static void autoSetMicrosteps(int motor){
			// If a command is currently being sent, wait
			waitForNMX();
			
			int subaddr = motor + 1;
			int command = 28;			
			if(debug)
				System.out.println("Autosetting motor " + motor + " microsteps");
			NMXComs.cmd(addr, subaddr, command);			
		}
				
		public static void setStartHere(int motor){
			
			if(motor == -1){				
				System.out.println("setStartHere(int motor)");				
				return;
			}
			
			// If a command is currently being sent, wait
			waitForNMX();
			
			int subaddr = motor + 1;
			int command = 29;			
			if(debug)
				System.out.println("Setting motor " + motor + " start here");
			NMXComs.cmd(addr, subaddr, command);			
		}
				
		public static void setStopHere(int motor){
			
			if(motor == -1){				
				System.out.println("setStopHere(int motor)");				
				return;
			}
			
			// If a command is currently being sent, wait
			waitForNMX();
			
			int subaddr = motor + 1;
			int command = 30;			
			if(debug)
				System.out.println("Setting motor " + motor + " stop here");
			NMXComs.cmd(addr, subaddr, command);			
		}
				
		public static void sendToRaw(int motor, int pos){
			
			if(motor == -1){				
				System.out.println("sendToRaw(int motor, int pos) -- Note: This does not correct for microstep setting. Make sure you know what your MS setting is!");				
				return;
			}
			
			// If the position is 0, use the sendToHome command instead
			if(pos == 0){
				sendToHome(motor);
				return;
			}
			
			// If a command is currently being sent, wait
			waitForNMX();
			
			int subaddr = motor + 1;
			int command = 31;
			int size = Consts.LONG_SIZE;
			int data = pos; 
			if(debug)
				System.out.println("Sending motor " + motor + " to position: " + pos);
			NMXComs.cmd(addr, subaddr, command, size, data);
		}

		private static void validateMotor(int _motor) {
			if (_motor < 0 || _motor >= Consts.MOTOR_COUNT)
				throw new IllegalArgumentException("Not a valid motor");
		}
				
		public static boolean getEnableStatus(int motor) {
			
			if(motor == -1){				
				System.out.println("getEnableStatus(int motor)");				
				return false;
			}
			
			// If a command is currently being sent, wait
			waitForNMX();			

			validateMotor(motor);

			int subaddr = motor + 1;
			int command = 100;
			if(debug)
				System.out.println("Querying motor " + motor + " enable status");
			NMXComs.cmd(addr, subaddr, command);
			waitForNMX();
			if(NMXComs.getResponseVal() == 0)
				return false;
			else
				return true;					
		}
				
		public static int getBacklashSteps(int motor) {
			
			if(motor == -1){				
				System.out.println("getBacklashSteps(int motor)");				
				return 0;
			}			
			
			// If a command is currently being sent, wait
			waitForNMX();			

			validateMotor(motor);

			int subaddr = motor + 1;
			int command = 101;
			if(debug)
				System.out.println("Querying motor " + motor + " backlash");
			NMXComs.cmd(addr, subaddr, command);
			waitForNMX();
			return NMXComs.getResponseVal();
		}
				
		public static int getMicrosteps(int motor){
			if(motor == -1){				
				System.out.println("getMicrosteps(int motor) -- Returns: Motor's current microstep setting");				
				return 0;
			}
			
			return getMicrosteps(motor, 0);
		}
		
		private static int getMicrosteps(int motor, int iteration) {
			
			// If a command is currently being sent, wait
			waitForNMX();			

			validateMotor(motor);

			int subaddr = motor + 1;
			int command = 102;			
			NMXComs.cmd(addr, subaddr, command);
			waitForNMX();
			int ret = NMXComs.getResponseVal();
			
			if(ret == Consts.COARSE || ret == Consts.MED || ret == Consts.FINE){			
				if(debug)
					System.out.println("Querying motor " + motor + " microsteps: " + ret);
				return ret;
			}
			else{
				if(iteration > 3){
					System.out.println(NMX_COM_FAIL);
					return 0;
				}
				else{
					System.out.println("*************** GETTING MICROSTEPS AGAIN!!! ************************");
					return getMicrosteps(motor, iteration + 1);
				}
			}
			
		}

		/**
		 * @param motor Relative motor number
		 * @return The maximum step rate of the specified motor in steps/second
		 */
		public static int getMaxStepRate(int motor) {
			
			if(motor == -1){				
				System.out.println("getMaxStepRate(int motor) -- Returns: Motor's max step rate in current microsteps/sec^2");				
				return 0;
			}
			
			// If a command is currently being sent, wait
			waitForNMX();
			

			validateMotor(motor);

			int subaddr = motor + 1;
			int command = 105;
			if(debug)
				System.out.println("Querying motor " + motor + " end postion");
			NMXComs.cmd(addr, subaddr, command);
			waitForNMX();
			return NMXComs.getResponseVal();
		}
				
		public static int getEndPosition(int motor) {
			
			if(motor == -1){				
				System.out.println("getEndPosition(int motor) -- Returns: Motor's end limit in current microsteps");				
				return 0;
			}
			
			// If a command is currently being sent, wait
			waitForNMX();
			

			validateMotor(motor);

			int subaddr = motor + 1;
			int command = 105;
			if(debug)
				System.out.println("Querying motor " + motor + " end postion");
			NMXComs.cmd(addr, subaddr, command);
			waitForNMX();
			return NMXComs.getResponseVal();
		}
				
		public static int getCurrentPosition(int motor){
			
			if(motor == -1){				
				System.out.println("getCurrentPosition(int motor) -- Returns: Motor's position in current microsteps");				
				return 0;
			}
			
			return getCurrentPosition(motor, 0);
		}

		private static int getCurrentPosition(int motor, int iteration) {		
			
			// If a command is currently being sent, wait
			waitForNMX();			

			validateMotor(motor);

			int subaddr = motor + 1;
			int command = 106;			
			NMXComs.cmd(addr, subaddr, command);
			waitForNMX();
			int ret = NMXComs.getResponseVal();
			if(debug)
				System.out.println("Querying motor " + motor + " current postion: " + ret);
			if(ret != Consts.ERROR){
				return ret;
			}
			else{
				System.out.println("Trying to get motor " + motor + " position again!");
				iteration++;
				if(iteration > 3){
					System.out.println(NMX_COM_FAIL);	
					return Consts.ERROR;
				}
				return getCurrentPosition(motor, iteration);
			}
		}
		
		public static int getCurrentPosition(int motor, boolean getException) throws InterruptedException {
			
			// If a command is currently being sent, wait
			waitForNMX();			

			validateMotor(motor);

			int subaddr = motor + 1;
			int command = 106;

			NMXComs.cmd(addr, subaddr, command, true);
			waitForNMX();
			int ret = NMXComs.getResponseVal();			
			if(debug)
				System.out.println("Querying motor " + motor + " current postion: " + ret);
			return ret;
		}
	
		public static boolean isRunning(int motor) throws InterruptedException {
			
			if(motor == -1){				
				System.out.println("isRunning(int motor) -- Returns: Whether the motor is currently moving");				
				return false;
			}
			
			// If a command is currently being sent, wait
			waitForNMX();

			validateMotor(motor);

			int subaddr = motor + 1;
			int command = 107;			
			NMXComs.cmd(addr, subaddr, command, true);
			waitForNMX();
			boolean ret;
			if(NMXComs.getResponseVal() > 0)
				ret = true;
			else
				ret = false;
			
			if(debug)
				System.out.println("Querying motor " + motor + " running status: " + ret);
			return ret;
		}
		
		public static float getContinuousSpeed(int motor) {
			
			if(motor == -1){				
				System.out.println("getContinuousSpeed(int motor) -- Returns: The motor speed in current microsteps/sec");				
				return 0;
			}
			
			// If a command is currently being sent, wait
			waitForNMX();
			
			validateMotor(motor);

			int subaddr = motor + 1;
			int command = 108;			
			NMXComs.cmd(addr, subaddr, command);
			waitForNMX();
			float ret = NMXComs.getResponseVal() / FLOAT_CONVERSION;
			if(debug)
				System.out.println("Querying motor " + motor + " continuous speed: " + ret + " steps/sec");
			return ret;
		}
				
		public static float getAccelDecel(int motor) {
			
			if(motor == -1){				
				System.out.println("getAccelDecel(int motor) -- Returns: The motor accel in current microsteps/sec^2");				
				return 0;
			}
			
			// If a command is currently being sent, wait
			waitForNMX();			

			validateMotor(motor);

			int subaddr = motor + 1;
			int command = 109;
			
			NMXComs.cmd(addr, subaddr, command);
			waitForNMX();
			
			float ret = (NMXComs.getResponseVal() / FLOAT_CONVERSION);
			if(debug)
				System.out.println("Querying motor " + motor + " accel/decel rate: " + ret + " steps/sec^2");
			return ret;
		}
				
		/**
		 * @param motor Which motor to query
		 * @return The program start position in current microsteps
		 */
		public static int getStart(int motor) {
			
			if(motor == -1){				
				System.out.println("getStart(int motor) -- Returns: The motor start position in current microsteps");				
				return 0;
			}	
			
			// If a command is currently being sent, wait
			waitForNMX();			

			validateMotor(motor);

			int subaddr = motor + 1;
			int command = 111;			
			NMXComs.cmd(addr, subaddr, command);
			waitForNMX();
			
			int ret = NMXComs.getResponseVal();
			if(debug)
				System.out.println("Querying motor " + motor + " start position (Graffik steps): " + ret);
			return ret;
		}
				
		/**
		 * @param motor Which motor to query
		 * @return The program stop position in current microsteps
		 */

		public static int getStop(int motor) {
			
			if(motor == -1){				
				System.out.println("getStop(int motor) -- Returns: The motor stop position in current microsteps");				
				return 0;
			}	
			
			// If a command is currently being sent, wait
			waitForNMX();			

			validateMotor(motor);

			int subaddr = motor + 1;
			int command = 112;			
			NMXComs.cmd(addr, subaddr, command);			
			waitForNMX();		
			
			int ret = NMXComs.getResponseVal();
			if(debug)
				System.out.println("Querying motor " + motor + " stop position (Graffik steps): " + ret);
			return ret;
		}
		
		/**
		 * @param motor Which motor to query
		 * @return The program travel time in either frames or milliseconds,
		 * depending on the current program mode
		 */
		public static int getTravelTime(int motor) {
			
			if(motor == -1){				
				System.out.println("getTravelTime(int motor)");				
				return 0;
			}		
			
			// If a command is currently being sent, wait
			waitForNMX();			

			validateMotor(motor);

			int subaddr = motor + 1;
			int command = 113;			
			NMXComs.cmd(addr, subaddr, command);
			waitForNMX();
			int res = NMXComs.getResponseVal();
			if(debug)
				System.out.println("Querying motor " + motor + " travel time (frames or milliseconds): " + res);
			return res;
		}
					
		public static boolean getSleepState(int motor) {
			
			if(motor == -1){				
				System.out.println("getSleepState(int motor)");				
				return false;
			}		
			
			// If a command is currently being sent, wait
			waitForNMX();			

			validateMotor(motor);

			int subaddr = motor + 1;
			int command = 117;			
			NMXComs.cmd(addr, subaddr, command);
			waitForNMX();
			boolean res;
			if(NMXComs.getResponseVal() > 0)
				res = true;
			else
				res = false;
			if(debug)
				System.out.println("Querying motor " + motor + " sleep state: " + res);
			return res;			
		}

	}

	// Camera commands
	public static class Camera {
		
		public static class Names{
			// General settings			
			static final String SET_ENABLE 		= "setenable";			
			static final String SET_TEST_MODE 	= "settestmode";			
			static final String SET_KEEPALIVE	= "setkeepalive";			
			static final String EXPOSE_NOW 		= "expose";
			
			// General queries
			static final String GET_ENABLED		= "isenabled";
			static final String GET_TEST_MODE	= "gettestmode";
			static final String GET_KEEPALIVE	= "getkeepalive";
			static final String GET_EXPOSING	= "isexposing";
			
			// Exposure settings
			static final String SET_INTERVAL	= "setinterval";
			static final String SET_TRIGGER 	= "settrigger";
			static final String SET_FOCUS 		= "setfocus";				
			static final String SET_DELAY	 	= "setdelay";
			static final String SET_MAX_SHOTS	= "setmaxshots";		
			static final String SET_MUP		 	= "setmup";			
			
			// Exposure queries			
			static final String GET_SHOTS		= "getshots";	
			static final String GET_INTERVAL	= "getinterval";
			static final String GET_TRIGGER		= "gettrigger";
			static final String GET_FOCUS 		= "getfocus";
			static final String GET_DELAY	 	= "getdelay";
			static final String GET_MAX_SHOTS 	= "getmaxshots";			
			static final String GET_MUP 		= "getmup";
													
			public static void help(){
				System.out.println("\n***** Camera Command Directory *****\n");
				System.out.println("General settings: \n" + SET_ENABLE + "\n" + SET_TEST_MODE + "\n" + SET_KEEPALIVE + "\n" + EXPOSE_NOW + "\n");
				System.out.println("General queries: \n" + GET_ENABLED + "\n" + GET_TEST_MODE + "\n" + GET_KEEPALIVE + "\n" + GET_EXPOSING + "\n");
				System.out.println("Exposure settings: \n" + SET_INTERVAL + "\n" + SET_TRIGGER + "\n" + SET_FOCUS + "\n" + SET_DELAY + "\n" + SET_MAX_SHOTS + "\n" + SET_MUP + "\n");
				System.out.println("Exposure queries: \n" + GET_INTERVAL + "\n" + GET_TRIGGER + "\n" + GET_FOCUS + "\n" + GET_DELAY + "\n" + GET_MAX_SHOTS + "\n" + GET_MUP + "\n");				
					
			}			
		}

		
		public static void command(String cmd, int data){
			if(cmd.equals(Names.SET_ENABLE)){
				boolean outVal = data == 0 ? false : true;
				Camera.setEnabled(outVal);
			}
			else if(cmd.equals(Names.SET_TEST_MODE)){
				boolean outVal = data == 0 ? false : true;
				Camera.setTestMode(outVal);
			}
			else if(cmd.equals(Names.SET_KEEPALIVE)){
				boolean outVal = data == 0 ? false : true;
				Camera.setKeepAlive(outVal);
			}
			else if(cmd.equals(Names.EXPOSE_NOW)){
				Camera.exposeNow();
			}
			else if(cmd.equals(Names.GET_ENABLED)){
				Camera.isEnabled();
			}
			else if(cmd.equals(Names.GET_TEST_MODE)){
				Camera.getTestMode();
			}
			else if(cmd.equals(Names.GET_KEEPALIVE)){
				Camera.getKeepAlive();
			}
			else if(cmd.equals(Names.GET_EXPOSING)){
				Camera.isExposingNow();
			}
			else if(cmd.equals(Names.SET_INTERVAL)){
				Camera.setIntervalTime(data);
			}
			else if(cmd.equals(Names.SET_TRIGGER)){
				Camera.setTriggerTime(data);
			}
			else if(cmd.equals(Names.SET_FOCUS)){
				Camera.setFocusTime(data);
			}
			else if(cmd.equals(Names.SET_DELAY)){
				Camera.setDelay(data);
			}
			else if(cmd.equals(Names.SET_MAX_SHOTS)){
				Camera.setMaxShots(data);
			}
			else if(cmd.equals(Names.SET_MUP)){
				boolean outVal = data == 0 ? false : true;
				Camera.setMirrorUp(outVal);
			}
			else if(cmd.equals(Names.GET_SHOTS)){
				Camera.getCurrentShots();
			}
			else if(cmd.equals(Names.GET_INTERVAL)){
				Camera.getIntervalTime();
			}
			else if(cmd.equals(Names.GET_TRIGGER)){
				Camera.getTriggerTime();
			}
			else if(cmd.equals(Names.GET_FOCUS)){
				Camera.getFocusTime();
			}
			else if(cmd.equals(Names.GET_DELAY)){
				Camera.getDelay();
			}
			else if(cmd.equals(Names.GET_MAX_SHOTS)){
				Camera.getMaxShots();
			}
			else if(cmd.equals(Names.GET_MUP)){
				Camera.getMirrorUp();
			}
			

		}
		
		public static void setEnabled(boolean enabled) {
			
			// If a command is currently being sent, wait
			waitForNMX();

			int subaddr = 4;
			int command = 2;
			int size = Consts.BYTE_SIZE;
			byte data = enabled ? (byte) 1 : 0;
			System.out.println("Setting camera enable: " + enabled);
			NMXComs.cmd(addr, subaddr, command, size, data);
		}

		public static void exposeNow() {
			
			// If a command is currently being sent, wait
			waitForNMX();		

			int subaddr = 4;
			int command = 3;
			if(debug)
				System.out.println("Triggering camera");
			NMXComs.cmd(addr, subaddr, command);
		}
		
		public static void setTriggerTime(int time){
			
			// If a command is currently being sent, wait
			waitForNMX();
			
			int subaddr = 4;
			int command = 4;
			int size = Consts.LONG_SIZE;
			int data = time; 
			if(debug)
				System.out.println("Setting trigger time:  " + time + " ms");
			NMXComs.cmd(addr, subaddr, command, size, data);
		}
		
		public static void setFocusTime(int time){
			
			// If a command is currently being sent, wait
			waitForNMX();
			
			int subaddr = 4;
			int command = 5;
			int size = Consts.INT_SIZE;
			int data = time; 
			if(debug)
				System.out.println("Setting focus time:  " + time + " ms");
			NMXComs.cmd(addr, subaddr, command, size, data);
		}
		
		public static void setMaxShots(int shots){
			
			// If a command is currently being sent, wait
			waitForNMX();
			
			int subaddr = 4;
			int command = 6;
			int size = Consts.INT_SIZE;
			int data = shots; 
			if(debug)
				System.out.println("Setting max shots:  " + shots);
			NMXComs.cmd(addr, subaddr, command, size, data);
		}
		
		public static void setDelay(int time){
			
			// If a command is currently being sent, wait
			waitForNMX();
			
			int subaddr = 4;
			int command = 7;
			int size = Consts.INT_SIZE;
			int data = time; 
			if(debug)
				System.out.println("Setting delay:  " + time + " ms");
			NMXComs.cmd(addr, subaddr, command, size, data);
		}
		
		public static void setFocusWithShutter(boolean enabled) {
			
			// If a command is currently being sent, wait
			waitForNMX();

			int subaddr = 4;
			int command = 8;
			int size = Consts.BYTE_SIZE;
			byte data = enabled ? (byte) 1 : 0;
			System.out.println("Setting camera enable: " + enabled);
			NMXComs.cmd(addr, subaddr, command, size, data);
		}
		
		public static void setMirrorUp(boolean enabled) {
			
			// If a command is currently being sent, wait
			waitForNMX();

			int subaddr = 4;
			int command = 9;
			int size = Consts.BYTE_SIZE;
			byte data = enabled ? (byte) 1 : 0;
			System.out.println("Setting mirror up enable: " + enabled);
			NMXComs.cmd(addr, subaddr, command, size, data);
		}
		
		public static void setIntervalTime(int time){
			
			// If a command is currently being sent, wait
			waitForNMX();
			
			int subaddr = 4;
			int command = 10;
			int size = Consts.LONG_SIZE;
			int data = time; 
			if(debug)
				System.out.println("Setting interval time:  " + time + " ms");
			NMXComs.cmd(addr, subaddr, command, size, data);
		}
		
		public static void setTestMode(boolean enabled) {
			
			// If a command is currently being sent, wait
			waitForNMX();

			int subaddr = 4;
			int command = 11;
			int size = Consts.BYTE_SIZE;
			byte data = enabled ? (byte) 1 : 0;
			System.out.println("Setting camera test mode: " + enabled);
			NMXComs.cmd(addr, subaddr, command, size, data);
		}
		
		public static void setKeepAlive(boolean enabled) {
			
			// If a command is currently being sent, wait
			waitForNMX();

			int subaddr = 4;
			int command = 12;
			int size = Consts.BYTE_SIZE;
			byte data = enabled ? (byte) 1 : 0;
			System.out.println("Setting mirror up enable: " + enabled);
			NMXComs.cmd(addr, subaddr, command, size, data);
		}
		
		public static boolean isEnabled() {
			
			// If a command is currently being sent, wait
			waitForNMX();			

			int subaddr = 4;
			int command = 100;			
			NMXComs.cmd(addr, subaddr, command);
			waitForNMX();
			boolean res = NMXComs.getResponseVal() == 0 ? false : true;
			if(debug)
				System.out.println("Querying camera enable status: " + res);
			return res;
		}
		
		public static boolean isExposingNow() {
			
			// If a command is currently being sent, wait
			waitForNMX();			

			int subaddr = 4;
			int command = 101;			
			NMXComs.cmd(addr, subaddr, command);
			waitForNMX();
			boolean res = NMXComs.getResponseVal() == 0 ? false : true;
			if(debug)
				System.out.println("Exposing now? : " + res);
			return res;
		}
		
		public static int getTriggerTime() {
			
			// If a command is currently being sent, wait
			waitForNMX();			

			int subaddr = 4;
			int command = 102;			
			NMXComs.cmd(addr, subaddr, command);
			waitForNMX();
			int res = NMXComs.getResponseVal();
			if(debug)
				System.out.println("Querying trigger time: " + res);
			return res;
		}
		
		public static int getFocusTime() {
			
			// If a command is currently being sent, wait
			waitForNMX();			

			int subaddr = 4;
			int command = 103;			
			NMXComs.cmd(addr, subaddr, command);
			waitForNMX();
			int res = NMXComs.getResponseVal();
			if(debug)
				System.out.println("Querying focus time: " + res);
			return res;
		}
		
		public static int getMaxShots() {
			
			// If a command is currently being sent, wait
			waitForNMX();			

			int subaddr = 4;
			int command = 104;			
			NMXComs.cmd(addr, subaddr, command);
			waitForNMX();
			int res = NMXComs.getResponseVal();
			if(debug)
				System.out.println("Querying max shots: " + res);
			return res;
		}
		
		public static int getDelay() {
			
			// If a command is currently being sent, wait
			waitForNMX();			

			int subaddr = 4;
			int command = 105;			
			NMXComs.cmd(addr, subaddr, command);
			waitForNMX();
			int res = NMXComs.getResponseVal();
			if(debug)
				System.out.println("Querying delay time: " + res);
			return res;
		}
		
		public static boolean isFocusingWithShutter() {
			
			// If a command is currently being sent, wait
			waitForNMX();			

			int subaddr = 4;
			int command = 106;			
			NMXComs.cmd(addr, subaddr, command);
			waitForNMX();
			boolean res = NMXComs.getResponseVal() == 0 ? false : true;
			if(debug)
				System.out.println("Focusing with shutter? : " + res);
			return res;
		}
		
		public static boolean getMirrorUp() {
			
			// If a command is currently being sent, wait
			waitForNMX();			

			int subaddr = 4;
			int command = 107;			
			NMXComs.cmd(addr, subaddr, command);
			waitForNMX();
			boolean res = NMXComs.getResponseVal() == 0 ? false : true;
			if(debug)
				System.out.println("Mirror up? : " + res);
			return res;
		}
		
		public static int getIntervalTime() {
			
			// If a command is currently being sent, wait
			waitForNMX();			

			int subaddr = 4;
			int command = 108;			
			NMXComs.cmd(addr, subaddr, command);
			waitForNMX();
			int res = NMXComs.getResponseVal();
			if(debug)
				System.out.println("Querying interval time: " + res);
			return res;
		}
		
		public static int getCurrentShots() {
			
			// If a command is currently being sent, wait
			waitForNMX();			

			int subaddr = 4;
			int command = 109;			
			NMXComs.cmd(addr, subaddr, command);
			waitForNMX();
			int res = NMXComs.getResponseVal();
			if(debug)
				System.out.println("Querying current shots: " + res);
			return res;
		}
		
		public static boolean getTestMode() {
			
			// If a command is currently being sent, wait
			waitForNMX();			

			int subaddr = 4;
			int command = 110;			
			NMXComs.cmd(addr, subaddr, command);
			waitForNMX();
			boolean res = NMXComs.getResponseVal() == 0 ? false : true;
			if(debug)
				System.out.println("Camera test mode? : " + res);
			return res;
		}
		
		public static boolean getKeepAlive() {
			
			// If a command is currently being sent, wait
			waitForNMX();			

			int subaddr = 4;
			int command = 111;			
			NMXComs.cmd(addr, subaddr, command);
			waitForNMX();
			boolean res = NMXComs.getResponseVal() == 0 ? false : true;
			if(debug)
				System.out.println("Keep alive? : " + res);
			return res;
		}		
	}

	// Key frame commands
	public static class KeyFrame {
		
		public static class Names{
			// General settings
			static final String SET_AXIS		= "setaxos";
			static final String SET_KF_COUNT	= "setkfcount";
			static final String SET_ABSC	 	= "setabsc";			
			static final String SET_POS			= "setpos";
			static final String SET_VEL			= "setvel";			
			
			// General queries
			static final String GET_FIRMWARE	= "getfirmware";
			static final String GET_MEMORY	 	= "getmemory";
			static final String GET_VOLTAGE 	= "getvoltage";
			static final String GET_CURRENT 	= "getcurrent";
			static final String GET_MOT_ATTCH	=  "getmotorattach";			
			static final String GET_JOYSTICK	 =  "getjoystickmode";			
			static final String GET_WATCHDOG	=  "getwatchdog";
			
			// Program settings
			static final String SET_MODE		= "setmode";
			static final String SET_PINGPONG 	= "setpingpong";
			static final String SET_FPS			= "setfps";			
			
			// Program control
			static final String START			= "start";
			static final String PAUSE			= "pause";			
			static final String STOP 			= "stop	";
			
			// Program queries
			static final String GET_PROGRAM_MODE 	=  "getprogrammode";			
			static final String GET_FPS 			= "getfps";			
			static final String GET_PROGRAM_DELAY 	=  "getprogramdelay";
			static final String GET_PROGRAM_TIME 	=  "getprogramtime";
			static final String GET_PINGPONG		=  "getpingpong";
			static final String GET_RUN_STATUS 		= "getrunstatus";
			static final String GET_PCT_COMP		=  "getpercentcomplete";		
			

			
			public static void help(){
				System.out.println("\n***** General Command Directory *****\n");
				System.out.println("General settings: \n" + SET_ADDRESS + "\n" + SET_DEBUG + "\n" + TOGGLE_LED + "\n" + SET_JOYSTICK + "\n" + SET_GRAFFIK + "\n");
				System.out.println("General queries: \n" + GET_FIRMWARE + "\n" + GET_MEMORY + "\n" + GET_VOLTAGE + "\n" + GET_CURRENT + "\n" + GET_MOT_ATTCH + "\n" + GET_JOYSTICK + "\n" + GET_WATCHDOG + "\n");
				System.out.println("Program settings: \n" + SET_MODE + "\n" + SET_PINGPONG + "\n" + SET_FPS + "\n");
				System.out.println("Program control: \n" + START + "\n" + PAUSE + "\n" + STOP + "\n");
				System.out.println("Program queries: \n" + GET_PROGRAM_MODE + "\n" + GET_FPS + "\n" + GET_PROGRAM_DELAY + "\n" + GET_PROGRAM_TIME +
						"\n" + GET_PINGPONG + "\n" + GET_RUN_STATUS + "\n" + GET_PCT_COMP);	
			}			
		}
		
		public static void command(String cmd, int data){
			if(cmd.equals(Names.anObject)){
				
			}
			else if(cmd.equals(Names.anObject)){
				
			}
			else if(cmd.equals(Names.anObject)){
				
			}
			else if(cmd.equals(Names.anObject)){
				
			}
			else if(cmd.equals(Names.anObject)){
				
			}
			else if(cmd.equals(Names.anObject)){
				
			}
			else if(cmd.equals(Names.anObject)){
				
			}
			else if(cmd.equals(Names.anObject)){
				
			}
			else if(cmd.equals(Names.anObject)){
				
			}
			else if(cmd.equals(Names.anObject)){
				
			}
			else if(cmd.equals(Names.anObject)){
				
			}
			else if(cmd.equals(Names.anObject)){
				
			}
			else if(cmd.equals(Names.anObject)){
				
			}
			else if(cmd.equals(Names.anObject)){
				
			}
			else if(cmd.equals(Names.anObject)){
				
			}
			else if(cmd.equals(Names.anObject)){
				
			}
			else if(cmd.equals(Names.anObject)){
				
			}
			else if(cmd.equals(Names.anObject)){
				
			}
			else if(cmd.equals(Names.anObject)){
				
			}
			else if(cmd.equals(Names.anObject)){
				
			}
			else if(cmd.equals(Names.anObject)){
				
			}
			else if(cmd.equals(Names.anObject)){
				
			}
			else if(cmd.equals(Names.anObject)){
				
			}
			else if(cmd.equals(Names.anObject)){
				
			}
			else if(cmd.equals(Names.anObject)){
				
			}
			else if(cmd.equals(Names.anObject)){
				
			}
			

		}
		
		private static void verifyCommand(int data){
			
			float checkVal = (float)NMXComs.getResponseVal();
			System.out.println("Graffik val: " + data + " NMX Val: " + checkVal);
			float comparison = Math.abs(data-checkVal);
			
			if(comparison > 0.1){
				System.out.println("*~*~*~*~*~*~*~Command Verification Failed~*~*~*~*~*~*~*");
			}
			else{
				System.out.println("Command OK!");
			}
		}
		
		private static void verifyCommand(float data){
			
			float checkVal = (float)NMXComs.getResponseVal() / FLOAT_CONVERSION; 
			
			System.out.println("Graffik val: " + data + " NMX Val: " + checkVal);
			
			if(Math.abs(data-checkVal) > 0.5)
				System.out.println("*~*~*~*~*~*~*~Command Verification Failed~*~*~*~*~*~*~*");			
			else{
				System.out.println("Command OK!");
			}
		}

		public static void setCurrentAxis(int axis) {
			
			// If a command is currently being sent, wait
			waitForNMX();
			currentKFAxis = axis;
			int subaddr = 5;
			int command = 10;
			int size = Consts.INT_SIZE;
			int data = axis;
			if(debug)
				System.out.println("Setting key frame axis " + axis);
			NMXComs.cmd(addr, subaddr, command, size, data);
			waitForNMX();		
			verifyCommand(axis);
		}
		
		public static void setKeyFrameCount(int count) {
			
			// If a command is currently being sent, wait
			waitForNMX();
						
			int subaddr = 5;
			int command = 11;
			int size = Consts.INT_SIZE;
			int data = count;
			if(debug)
				System.out.println("Setting key frame count: " + count);
			NMXComs.cmd(addr, subaddr, command, size, data);
			waitForNMX();			
			verifyCommand(count);			
		}

		public static void setNextAbscissa(float abscissa) {
			
			// If a command is currently being sent, wait
			waitForNMX();
						
			int subaddr = 5;
			int command = 12;
			int size = Consts.FLOAT_SIZE;
			int data = Float.floatToIntBits(abscissa);
			if(debug)
				System.out.println("Setting key frame abscissa: " + abscissa);
			NMXComs.cmd(addr, subaddr, command, size, data);
			waitForNMX();			
			verifyCommand(abscissa);
		}
		
		public static void setNextPosition(float pos) {
			
			// If a command is currently being sent, wait
			waitForNMX();
			
			int subaddr = 5;
			int command = 13;
			int size = Consts.FLOAT_SIZE;
			int data = Float.floatToIntBits(pos);
			if(debug)
				System.out.println("Setting key frame position (controller steps): " + pos);
			NMXComs.cmd(addr, subaddr, command, size, data);
			waitForNMX();		
			verifyCommand(pos);
		}
		
		public static void setNextVelocity(float vel) {
			
			// If a command is currently being sent, wait
			waitForNMX();
			
			int subaddr = 5;
			int command = 14;
			int size = Consts.FLOAT_SIZE;
			int data = Float.floatToIntBits(vel);
			if(debug)
				System.out.println("Setting key frame velocity (controller steps/s): " + vel);
			NMXComs.cmd(addr, subaddr, command, size, data);
			waitForNMX();	
			verifyCommand(vel);
		}
		
		/** 
		 * @param rate How often the controller should update motor continuous speeds
		 * in milliseconds
		 */
		public static void setUpdateRate(int rate) {
			
			// If a command is currently being sent, wait
			waitForNMX();
						
			int subaddr = 5;
			int command = 15;
			int size = Consts.INT_SIZE;
			int data = rate;
			if(debug)
				System.out.println("Setting key frame update rate: " + rate);
			NMXComs.cmd(addr, subaddr, command, size, data);
			waitForNMX();			
		}
		
		public static void endTransmission() {
			
			// If a command is currently being sent, wait
			waitForNMX();
						
			int subaddr = 5;
			int command = 16;
			
			if(debug)
				System.out.println("Ending KF axis transmission -- setting start/stop points");
			NMXComs.cmd(addr, subaddr, command);
			waitForNMX();					
		}

		/** 
		 * @param rate The total program time for continuous video mode in milliseconds
		 */
		public static void setContVidTime(int time) {
			
			// If a command is currently being sent, wait
			waitForNMX();
						
			int subaddr = 5;
			int command = 17;
			int size = Consts.LONG_SIZE;
			int data = time;
			if(debug)
				System.out.println("Setting continuous video time: " + time);
			NMXComs.cmd(addr, subaddr, command, size, data);
			waitForNMX();			
		}
		
		public static void startKeyFrameProgram(){
			
			// If a command is currently being sent, wait
			waitForNMX();			
			
			// Need to wait a long time to allow for backlash take-up
			NMXComs.setResponseDelay(NMXComs.ResponseTiming.LONG_WAIT);
			
			int subaddr = 5;
			int command = 20;
			if(debug)
				System.out.println("Starting key frame program");
			NMXComs.cmd(addr, subaddr, command);
			waitForNMX();
		}
		
		public static void pauseKeyFrameProgram(){
			
			// If a command is currently being sent, wait
			waitForNMX();			
						
			int subaddr = 5;
			int command = 21;
			if(debug)
				System.out.println("Pausing key frame program");
			NMXComs.cmd(addr, subaddr, command);
			waitForNMX();			
		}

		public static void stopKeyFrameProgram(){
			
			// If a command is currently being sent, wait
			waitForNMX();			
						
			int subaddr = 5;
			int command = 22;
			if(debug)
				System.out.println("Stopping key frame program");
			NMXComs.cmd(addr, subaddr, command);
			waitForNMX();			
		}
		
		public static void takeUpBacklash(){
			
			// If a command is currently being sent, wait
			waitForNMX();			
			
			// Need to wait a long time to allow for backlash take-up
			NMXComs.setResponseDelay(NMXComs.ResponseTiming.LONG_WAIT);
						
			int subaddr = 5;
			int command = 23;
			if(debug)
				System.out.println("Stopping key frame program");
			NMXComs.cmd(addr, subaddr, command);
			waitForNMX();			
		}
		
		/*** Key Frame Query Commands ***/
		
		public static void printKeyFrameData(){
			
			// If a command is currently being sent, wait
			waitForNMX();
			
			int subaddr = 5;
			int command = 99;
			if(debug)
				System.out.println("Printing key frame data from NMX to USB");
			NMXComs.cmd(addr, subaddr, command);			
		}
		
		public static int getKeyFrameCount(){
			
			// If a command is currently being sent, wait
			waitForNMX();
			
			int subaddr = 5;
			int command = 100;		
			NMXComs.cmd(addr, subaddr, command);
			waitForNMX();
			int ret = NMXComs.getResponseVal();
			if(debug)
				System.out.println("Querying key frame count for axis " + currentKFAxis + ": " + ret);
			return ret;
		}
		
		public static int getUpdateRate(){
			
			// If a command is currently being sent, wait
			waitForNMX();
			
			int subaddr = 5;
			int command = 101;
			if(debug)
				System.out.println("Querying key frame update rate");
			NMXComs.cmd(addr, subaddr, command);
			waitForNMX();
			return NMXComs.getResponseVal();
		}

		public static float getPosition(float x){
			
			// If a command is currently being sent, wait
			waitForNMX();
			
			int subaddr = 5;
			int command = 102;
			int size = Consts.FLOAT_SIZE;
			int data = Float.floatToIntBits(x);
			if(debug)
				System.out.println("Querying position at x = " + x);
			NMXComs.cmd(addr, subaddr, command, size, data);
			waitForNMX();

			float ret = (float) NMXComs.getResponseVal() / 100;
			return ret;
		}
		
		public static float getVelocity(float x){
			
			// If a command is currently being sent, wait
			waitForNMX();
			
			int subaddr = 5;
			int command = 103;
			int size = Consts.FLOAT_SIZE;
			int data = Float.floatToIntBits(x);
			if(debug)
				System.out.println("Querying velocity at x = " + x);
			NMXComs.cmd(addr, subaddr, command, size, data);
			waitForNMX();

			float ret = (float) NMXComs.getResponseVal() / 100;
			return ret;
		}

		public static float getAccel(float x){
			
			// If a command is currently being sent, wait
			waitForNMX();
			
			int subaddr = 5;
			int command = 104;
			int size = Consts.FLOAT_SIZE;
			int data = Float.floatToIntBits(x);
			if(debug)
				System.out.println("Querying acceleration at x = " + x);
			NMXComs.cmd(addr, subaddr, command, size, data);
			waitForNMX();

			float ret = (float) NMXComs.getResponseVal() / 100;
			return ret;
		}
		
		public static boolean validateVel() {
			
			// If a command is currently being sent, wait
			waitForNMX();			

			int subaddr = 5;
			int command = 105;			
			NMXComs.cmd(addr, subaddr, command);
			waitForNMX();
			boolean ret = NMXComs.getResponseVal() == 0 ? false : true;
			if(debug)
				System.out.println("Validating motor velocity: " + ret);
			return ret;
		}

		public static boolean validateAccel() {
			
			// If a command is currently being sent, wait
			waitForNMX();			

			int subaddr = 5;
			int command = 106;			
			NMXComs.cmd(addr, subaddr, command);
			waitForNMX();
			boolean ret = NMXComs.getResponseVal() == 0 ? false : true;
			if(debug)
				System.out.println("Validating motor accel: " + ret);
			return ret;
		}
		
		public static int getContVidTime(){
			
			// If a command is currently being sent, wait
			waitForNMX();
			
			int subaddr = 5;
			int command = 107;			
			NMXComs.cmd(addr, subaddr, command);
			waitForNMX();

			int ret = NMXComs.getResponseVal();
			if(debug)
				System.out.println("Querying key frame continuous video duration: " + ret + " ms");
			return ret;
		}
		
		
		public static int getRunState(){
			
			// If a command is currently being sent, wait
			waitForNMX();
			
			int subaddr = 5;
			int command = 120;		
			
			NMXComs.cmd(addr, subaddr, command);
			waitForNMX();

			int ret = NMXComs.getResponseVal();
			
			if(ret != Consts.NMXState.STOPPED && ret != Consts.NMXState.RUNNING && ret != Consts.NMXState.PAUSED)
				ret = Consts.NMXState.INVALID;
			
			String state = "";
			switch(ret){
				case Consts.NMXState.STOPPED:
					state = "Stopped";
					break;
				case Consts.NMXState.RUNNING:
					state = "Running";
					break;
				case Consts.NMXState.PAUSED:
					state = "Paused";
					break;
				case Consts.NMXState.INVALID:
					state = "Invalid *** Probably a COM error";
					break;
			}
			
			if(debug)
				System.out.println("Querying run state: " + state);
			
			return ret;
		}
		
		public static int getCurrentRunTime(){
			
			// If a command is currently being sent, wait
			waitForNMX();
			
			
			int subaddr = 5;
			int command = 121;		
			
			NMXComs.cmd(addr, subaddr, command);
			waitForNMX();

			int ret = NMXComs.getResponseVal();

			if(debug)
				System.out.println("Querying current run time: " + ret + " ms");
			
			return ret;			
		}
		
		public static int getMaxRunTime(){
			return getMaxRunTime(0);
		}
		
		public static int getMaxRunTime(int iteration){
			
			// If a command is currently being sent, wait
			waitForNMX();
			
			
			int subaddr = 5;
			int command = 122;		
			
			NMXComs.cmd(addr, subaddr, command);
			waitForNMX();

			int ret = NMXComs.getResponseVal();
			
			if(ret != Consts.ERROR){
				if(debug)
					System.out.println("Querying max run time: " + ret + " ms");
				return ret;
			}
			else{
				if(iteration > 3){
					System.out.println("*************** NMX COMS FAILURE!!! ************************");
					return Consts.ERROR;
				}
				else{
					System.out.println("*************** GETTING MAX RUN TIME AGAIN!!! ************************");
					return getMaxRunTime(iteration + 1);
				}
			}			
		}
		
		public static int getProgramPctComplete(){
			
			// If a command is currently being sent, wait
			waitForNMX();
			
			int subaddr = 5;
			int command = 123;		
			
			NMXComs.cmd(addr, subaddr, command);
			waitForNMX();

			int ret = NMXComs.getResponseVal();

			if(debug)
				System.out.println("Querying program percent complete: " + ret + "%");
			
			return ret;			
		}
		
		public static int getKeyFrameTime(int which){
			return getKeyFrameTime(which, 0);
		}
		
		private static int getKeyFrameTime(int which, int iteration){
			
			// If a command is currently being sent, wait
			waitForNMX();
			
			int subaddr = 5;
			int command = 130;
			int length = Consts.INT_SIZE;		
			int data = which;		
			
			NMXComs.cmd(addr, subaddr, command, length, data);
			waitForNMX();
			
			int ret = NMXComs.getResponseVal();
			
			if(debug)
				System.out.println("Querying axis " + currentKFAxis + " KF " + which + " time: " + ret + " ms");
			return ret;
		}
		
		public static int getKeyFramePos(int which){
			
			// If a command is currently being sent, wait
			waitForNMX();
			
			int subaddr = 5;
			int command = 131;		
			int length = Consts.INT_SIZE;		
			int data = which;		
			
			NMXComs.cmd(addr, subaddr, command, length, data);
			waitForNMX();
			
			int ret = NMXComs.getResponseVal();

			if(debug)
				System.out.println("Querying axis " + currentKFAxis + " KF " + which + " position: " + ret);
			return ret;			
		}

	}
	
	
	/**
	 * This method pauses the calling thread and causes it to wait till the NMXComs
	 * class is done processing its current command and is available again
	 */
	public static void commandWait(){
		commandWait(10);
	}
	
	public static void commandWait(int time){
		//System.out.println("Waiting for NMXComs to free");
		while(NMXComs.isBusy()){			
			// Wait till the NMX communications class is free again before proceeding
			try {
				Thread.sleep(time);
			} catch (InterruptedException e) {
				System.out.println("Interrupted while waiting for NMX response");
				e.printStackTrace();
			}
		}
		//System.out.println("NMXComs now available!");
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
