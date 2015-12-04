package com.dynamicperception.nmxcommandline.models;

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

	// General commands
	public static class General {

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
		
		public static void motorCommand(String cmd, float[] args){
			if(cmd.equals(SETSLEEP)){
				boolean sleep = args[1] == 0 ? false : true;
				setSleep((int) args[0], sleep);
			}
		}

		public static void sendAllMotorsHome() {
			
			NMXCmd.General.sendAllMotorsHome();
		}
		
		private static String SETSLEEP = "setSleep";
		
		public static void setSleep(){
			System.out.println("setSleep(int motor, boolean sleep_state)");
		}

		public static void setSleep(int motor, boolean sleep_state) {
			
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
			// If a command is currently being sent, wait
			waitForNMX();
			
			int subaddr = motor + 1;
			int command = 23;			
			if(debug)
				System.out.println("Sending motor " + motor + " to start");
			NMXComs.cmd(addr, subaddr, command);			
		}
		
		public static void sendToStop(int motor){
			// If a command is currently being sent, wait
			waitForNMX();
			
			int subaddr = motor + 1;
			int command = 24;			
			if(debug)
				System.out.println("Sending motor " + motor + " to stop");
			NMXComs.cmd(addr, subaddr, command);			
		}
		
		public static void resetLimits(int motor){
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
			// If a command is currently being sent, wait
			waitForNMX();
			
			int subaddr = motor + 1;
			int command = 29;			
			if(debug)
				System.out.println("Setting motor " + motor + " start here");
			NMXComs.cmd(addr, subaddr, command);			
		}
		
		public static void setStopHere(int motor){
			// If a command is currently being sent, wait
			waitForNMX();
			
			int subaddr = motor + 1;
			int command = 30;			
			if(debug)
				System.out.println("Setting motor " + motor + " stop here");
			NMXComs.cmd(addr, subaddr, command);			
		}
		
		public static void sendToRaw(int motor, int pos){
			
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
			if(debug)
				System.out.println("Querying motor " + motor + " postion");
			NMXComs.cmd(addr, subaddr, command, true);
			waitForNMX();
			int ret = NMXComs.getResponseVal();			
			if(debug)
				System.out.println("Querying motor " + motor + " current postion: " + ret);
			return ret;
		}
		
		public static boolean isRunning(int motor) throws InterruptedException {
			
			// If a command is currently being sent, wait
			waitForNMX();

			validateMotor(motor);

			int subaddr = motor + 1;
			int command = 107;
			if(debug)
				System.out.println("Querying motor " + motor + " running status");
			NMXComs.cmd(addr, subaddr, command, true);
			waitForNMX();			
			if(NMXComs.getResponseVal() > 0)
				return true;
			else
				return false;
		}
		
		public static float getContinuousSpeed(int motor) {
			
			// If a command is currently being sent, wait
			waitForNMX();
			
			validateMotor(motor);

			int subaddr = motor + 1;
			int command = 108;
			if(debug)
				System.out.println("Querying motor " + motor + " continuous speed (steps/sec)");
			NMXComs.cmd(addr, subaddr, command);
			waitForNMX();
			return (NMXComs.getResponseVal() / FLOAT_CONVERSION);
		}
		
		public static float getAccelDecel(int motor) {
			
			// If a command is currently being sent, wait
			waitForNMX();			

			validateMotor(motor);

			int subaddr = motor + 1;
			int command = 109;
			if(debug)
				System.out.println("Querying motor " + motor + " accel/decel rate (steps/sec^2)");
			NMXComs.cmd(addr, subaddr, command);
			waitForNMX();
			
			float ret = (NMXComs.getResponseVal() / FLOAT_CONVERSION);
			return ret;
		}
		
		/**
		 * @param motor Which motor to query
		 * @return The program start position in 16th microsteps
		 */
		public static int getStart(int motor) {
			
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
		 * @return The program stop position in 16th microsteps
		 */

		public static int getStop(int motor) {
			
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
