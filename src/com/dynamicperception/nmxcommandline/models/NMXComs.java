package com.dynamicperception.nmxcommandline.models;

import com.dynamicperception.nmxcommandline.helpers.Consts;
import com.dynamicperception.nmxcommandline.helpers.ThreadManagement;
import com.dynamicperception.nmxcommandline.coms.Serial;

public class NMXComs {
	
	// Serial object
	private static Serial serial;

	// Debugging
	private static boolean serialDetail = false;
	
	// Command handling thread
	private static Thread commandThread;
	
	// State monitoring
	private static long lastCommandTime = 0;
	private static boolean sendingCommand = false;
	private static boolean NMXComsBusy = false;	
	
	// Packet vars	
	private static String commandPacket = null;	
	private static String manualData = "";
	
	// Response handling
	private static boolean responseOn = true;		
	private static int responseDelay = 50;	
	private static String response = "";
	private static int responseVal;
	
	private static int emptyResponseCount = 0;
			
	// Response delay options
	protected static class ResponseTiming{		
		public final static int DEFAULT = 80;
		public final static int NO_RESPONSE = 30;
		public final static int EEPROM = 300;	
		public final static int LONG_WAIT = 1500;
	}
	
	
	// ******** Setters and Getters ******** // 
	
	/**
	 * @param enabled Whether serial detail should be printed for ever packet sent and received
	 */
	public static void setSerialDetail(boolean enabled){
		serialDetail = enabled;
	}	
	
	/**
	 * Sets how long the command thread should wait before trying to fetch a response packet
	 * @param millis Time in milliseconds
	 */
	public static void setResponseDelay(int millis){
		responseDelay = millis;
	}
	
	/**
	 * Sets any manually configured data that will be added before the main data segment of the
	 * packet. This is used for a few commands that require direction byte before other data
	 * @param data
	 */
	public static void setManualData(String data){
		manualData = data;
	}
	
	/**
	 * Sets the serial object for this class to use and starts the command detection thread
	 * @param _serial
	 */
	public static void setSerialObject(Serial _serial) {
		serial = _serial;
		commandThread = new Thread(new CommandThread());
		commandThread.start();
	}
	
	/**
	 * This differs from the isSendingCommand() method in that isBusy() returns true once 
	 * the command has starting being processed, while isSendingCommand() returns true only once 
	 * the command thread has been flagged and has started sending the command to the controller.
	 * 
	 * @return Whether the command sending process is in process. T
	 */
	public static boolean isBusy(){
		return NMXComsBusy;
	}

	/** 
	 * See isBusy() Javadoc for more clarification
	 * @return Whether the command thread is busy
	 */
	public static boolean isSendingCommand(){
		return sendingCommand;
	}
	
	/** 
	 * @return The value associated with the response to the last command sent
	 */
	protected static int getResponseVal(){
		int ret = responseVal;		
		if(ret == Consts.ERROR ){
			System.out.println("COM error caused by command packet: " + commandPacket);
		}
		// Set the response value to the error value so if there is a problem
		// with the next response, the error value will be returned		
		responseVal = Consts.ERROR;
		return ret;
	}
	
	// ******** Communications Methods ******** // 
	
	/**
	 * An NMX command that does not include any additional data
	 * 
	 * @param sub_addr
	 *            The sub-address indicates which type of command is being sent.
	 *            0 - General command, 1-3 - command to specific motor, 4 -
	 *            camera command, 5 - key frame command;
	 * @param command
	 *            NMX command number (DEC)
	 */
	public static void cmd(int addr, int subAddr, int command) {
		cmd(addr, subAddr, command, 0, 0);
	}
	
	public static void cmd(int addr, int subAddr, int command, boolean getException) throws InterruptedException{
		cmd(addr, subAddr, command, 0, 0, true, true);
	}

	
	/**
	 * An NMX command that includes additional data
	 * 
	 * @param sub_addr
	 *            The sub-address indicates which type of command is being sent.
	 *            0 - General command, 1-3 - command to specific motor, 4 -
	 *            camera command, 5 - key frame command;
	 * @param command
	 *            NMX command number (DEC)
	 * @param length
	 *            Number of bytes of data to be included in the packet
	 * @param data
	 *            Data to be included in the packet
	 */
	public static void cmd(int addr, int subAddr, int command, int length, int data) {
		try {
			cmd(addr, subAddr, command, length, data, true);
		} catch (InterruptedException e) {
			System.out.println("NMX Command interrupted during waiting period");
			e.printStackTrace();
		}
	}
	
	public static void cmd(int addr, int subAddr, int command, int length, int data, boolean getResponse, boolean getException) throws InterruptedException{
		cmd(addr, subAddr, command, length, data, getResponse);
	}

	/**
	 * An NMX command that includes additional data. Optional no response
	 * parameter
	 * 
	 * @param sub_addr
	 *            The sub-address indicates which type of command is being sent.
	 *            0 - General command, 1-3 - command to specific motor, 4 -
	 *            camera command, 5 - key frame command;
	 * @param command
	 *            NMX command number (DEC)
	 * @param length
	 *            Number of bytes of data to be included in the packet
	 * @param data
	 *            Data to be included in the packet
	 * @param response
	 *            If this parameter is false, the program will not wait for a
	 *            response from the NMX
	 */
	public static void cmd(int addr, int _subAddr, int _command, int _length, int _data, boolean getResponse) throws InterruptedException {
		
		responseOn = getResponse;
		
		// Lock the NMX communications class
		NMXComsBusy = true;

		// Assemble command packet
		String header = "0000000000FF";
		String address = Integer.toHexString(addr);		
		address = address.length() == 2 ? address : "0" + address;	// Append leading zero if necessary
		String sub_addr = _subAddr <= 15 ? "0" + Integer.toHexString(_subAddr) : Integer.toHexString(_subAddr);
		String command = _command <= 15 ? "0" + Integer.toHexString(_command) : Integer.toHexString(_command);
		String length = _length <= 15 ? "0" + Integer.toHexString(_length) : Integer.toHexString(_length);
		String data = Integer.toHexString(_data).length() % 2 != 0 ? "0" + Integer.toHexString(_data)
				: Integer.toHexString(_data);
		commandPacket = header + address + sub_addr + command + length;

		// If the length is non-zero, then append the data
		if (_length != 0) {
			// Make sure the data has any necessary leading zeros
			if (data.length() / 2 != _length) {
				int leading_zero_byes = _length - (data.length() / 2);
				for (int i = 0; i < leading_zero_byes; i++) {
					data = "00" + data;
				}
			}
			// Append manual data (this is used for a few commands that require a direction byte and steps in the same packet)
			commandPacket += manualData;
			// Append to the packet
			commandPacket += data;
			manualData = "";			
		}
		
		// Set command ready flag to trigger command thread		
		sendingCommand = true;
	}

	
	// ******** Helper Methods ******** // 
	
	/** 
	 * @param s A hex string
	 * @return The byte array equivalent of the input hex string
	 */
	static byte[] hexStringToByteArray(String s) {
		int len = s.length();
		byte[] data = new byte[len / 2];
		for (int i = 0; i < len; i += 2) {
			data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i + 1), 16));
		}
		return data;
	}
	
	public static int getEmptyResponseCount(){
		return emptyResponseCount;
	}

	/**
	 * This method extracts any value passed from the controller in its response packet.
	 * It should not be called until enough time has elapsed since sending the last command
	 * for the command to finish executing on the controller side and to send a complete
	 * response packet. The timing may vary from command to command, so the timing delay may
	 * be adjusted with the setTimingDelay() method.
	 */
	private static void parseResponse() {		
		
		if(!responseOn){
			responseVal = Consts.ERROR;
			return;
		}

		if(serialDetail)
			System.out.println("Response before parsing: " + response);
		
		// This is an "OK" confirmation code, ignore it
		if(response.equals("0000000000ff00000100"))
			return;
		if(response.equals("")){
			if(serialDetail)
				System.out.println("Empty response!");
			responseVal = Consts.ERROR;
			emptyResponseCount++;
			return;
		}
		emptyResponseCount = 0;

		// int length = Integer.decode("0x" + response.substring(18, 20));
		int data_type = 7;
		long data = Consts.ERROR;
		try {
			data_type = Integer.decode("0x" + response.substring(20, 22));
			try {
				data = Long.decode("0x" + response.substring(22, response.length()));
				try {
					// Handle negative longs
					if (data_type == 3 && Integer.decode("0x" + response.substring(22, 24)) == 255)
						data = data - Long.decode("0xffffffff");
				} catch (NumberFormatException e) {
					System.out.println("Error handling negative data value");
				}

			} catch (NumberFormatException e) {
				System.out.println("Error parsing response: " + response);
			}
		} catch (NumberFormatException e) {
			System.out.println("Error parsing data type: " + response);
		} catch (StringIndexOutOfBoundsException e) {
			if(serialDetail)
				System.out.println("Out of bounds!!!");
		}
		
		responseVal = (int) data;
	}
	
	
	// ******** Command Monitoring Thread ******** // 
	
	/**
	 * This class is a thread that handles the timing of sending commands to
	 * the NMX controller. In order to avoid lagging of the UI on the EventDispach
	 * thread, waiting for responses from the controller is handled here. 
	 * @author Michael
	 *
	 */
	private static class CommandThread
	implements Runnable{
		
		volatile boolean execute;		
		
		public void run(){
			execute = true;
			while(execute){
				
				// Wait a while before checking if there a command is to be sent
				try{					
					Thread.sleep(2);					
				} catch (InterruptedException e) {
					System.out.println("NMX Command thread interrupted!");
					e.printStackTrace();
				}	
				
				if(sendingCommand){					
					
					if(serialDetail)
						ThreadManagement.message("******************************************************");
					
					// Print the command being sent
					if(serialDetail)					
						ThreadManagement.message("Command out: " + commandPacket);
	
					// Convert hex string to byte array and send to NMX
					byte[] outCommand = hexStringToByteArray(commandPacket);
					
					serial.write(outCommand);
					
					// Log the command time
					long commandTime = System.currentTimeMillis();
					if(serialDetail)
						ThreadManagement.message("Time since last command: " + (commandTime-lastCommandTime)  + "ms");
					lastCommandTime = commandTime;
					
					if(responseDelay == ResponseTiming.EEPROM || responseDelay == ResponseTiming.LONG_WAIT){
						// If the the timing has been set an allowable alternate value, don't change it
					}
					else if(responseOn){						
						responseDelay = ResponseTiming.DEFAULT;
					}
					else if(!responseOn){
						//ThreadManagement.message("Not waiting for response");
						responseDelay = ResponseTiming.NO_RESPONSE;
					}
					
					// Wait for response
					try {
						Thread.sleep(responseDelay);
					} catch (InterruptedException e) {
						ThreadManagement.message("NMX Command thread interrupted!");
						e.printStackTrace();
					}				
					
					// Change the response delay back to the default
					responseDelay = ResponseTiming.DEFAULT;
										
					// Retrieve response buffer					
					int size = serial.available();				
					int[] inByte = new int[size];
					
					// Clear old response string and populate with chars from buffer
					response = "";
					for (int i = 0; i < inByte.length; i++) {
						inByte[i] = serial.read();
						String debug = inByte[i] <= 15 ? "0" + Integer.toHexString(inByte[i]) : Integer.toHexString(inByte[i]);
						response = response + debug;
					}			
					// Extract the data from the response packet					
					parseResponse();
					
					if(serialDetail)
						ThreadManagement.message("******************************************************");
					
					// Cancel the command sending condition 
					sendingCommand = false;
					
					// Unlock the NMX communications class
					NMXComsBusy = false;
				}
				
			}				
		};
	}

}
