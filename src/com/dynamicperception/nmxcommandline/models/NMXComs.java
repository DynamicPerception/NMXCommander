package com.dynamicperception.nmxcommandline.models;

import java.util.ArrayList;
import java.util.List;

import com.dynamicperception.nmxcommandline.coms.Serial;
import com.dynamicperception.nmxcommandline.helpers.Consts;

public class NMXComs {

    private static final int    ERROR              = (int) -1e9;
    private static final String ERROR_STR          = "ERROR";

    // Serial object
    private static Serial       serial;

    // Debugging
    private static boolean      serialDetail       = false;

    // Command handling thread
    private static Thread       commandThread;

    // State monitoring
    private static long         lastCommandTime    = 0;
    private static boolean      sendingCommand     = false;
    private static boolean      NMXComsBusy        = false;

    // Packet vars
    private static String       commandPacket      = null;
    private static String       manualData         = "";

    // Response handling
    private static boolean      responseOn         = true;
    private static String       response           = "";
    private static int          responseVal;
    private static int          responseTimeout    = 500;

    private static int          emptyResponseCount = 0;

    // ******** Setters and Getters ******** //

    /**
     * @param enabled
     *            Whether serial detail should be printed for ever packet sent
     *            and received
     */
    public static void setSerialDetail(boolean enabled) {
        serialDetail = enabled;
    }

    public static boolean getSerialDetail() {
        return serialDetail;
    }

    /**
     * Sets any manually configured data that will be added before the main data
     * segment of the packet. This is used for a few commands that require
     * direction byte before other data
     * 
     * @param data
     */
    public static void setManualData(String data) {
        manualData = data;
    }

    /**
     * Sets the serial object for this class to use and starts the command
     * detection thread
     * 
     * @param _serial
     */
    public static void setSerialObject(Serial _serial) {
        serial = _serial;
        commandThread = new Thread(new CommandThread());
        commandThread.start();
    }

    /**
     * This differs from the {@link #isSendingCommand()} method in that isBusy()
     * returns true once the command has starting being processed, while
     * {@link #isSendingCommand()} returns true only once the command thread has
     * been flagged and has started sending the command to the controller.
     * 
     * @return Whether the command sending process is in process. T
     */
    public static boolean isBusy() {
        return NMXComsBusy;
    }

    /**
     * See {@link #isBusy()}() for more clarification
     * 
     * @return Whether the command thread is busy
     */
    public static boolean isSendingCommand() {
        return sendingCommand;
    }

    /**
     * Sets the response timeout
     * 
     * @param timeout
     *            response timeout in milliseconds
     */
    public static void setResponseTimeout(int timeout) {
        responseTimeout = timeout;
    }

    /**
     * Gets the response timeout
     * 
     * @return response timeout in milliseconds
     */
    public static int getResponseTimeout() {
        return responseTimeout;
    }

    /**
     * @return The value associated with the response to the last command sent
     */
    protected static int getResponseVal() {
        int ret = responseVal;
        if (ret == ERROR) {
            System.out.println("COM error caused by command packet: " + commandPacket);
            // Exception e = new UnsupportedOperationException();
            // e.printStackTrace();
        }
        // Set the response value to the error value so if there is a problem
        // with the next response, the error value will be returned
        responseVal = ERROR;
        return ret;
    }

    // ******** Communications Methods ******** //

    /**
     * An NMX command that does not include any additional data
     * 
     * @param subAddr
     *            The sub-address indicates which type of command is being sent.
     *            0 - General command, 1-3 - command to specific motor, 4 -
     *            camera command, 5 - key frame command;
     * @param command
     *            NMX command number (DEC)
     */
    public static String cmd(int addr, int subAddr, int command) {
        return cmd(addr, subAddr, command, 0, 0);
    }

    public static String cmd(int addr, int subAddr, int command, boolean getException) throws InterruptedException {
        return cmd(addr, subAddr, command, 0, 0, true, true);
    }

    /**
     * An NMX command that includes additional data
     * 
     * @param addr
     *            The address of the controller. Default is 3.
     * 
     * @param subAddr
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
    public static String cmd(int addr, int subAddr, int command, int length, int data) {
        try {
            return cmd(addr, subAddr, command, length, data, true);
        } catch (InterruptedException e) {
            System.out.println("NMX Command interrupted during waiting period");
            e.printStackTrace();
            return "ERROR";
        }
    }

    public static String cmd(int addr, int subAddr, int command, int length, int data, boolean getResponse,
            boolean getException) throws InterruptedException {
        return cmd(addr, subAddr, command, length, data, getResponse);
    }

    /**
     * An NMX command that includes additional data. Optional no response
     * parameter
     * 
     * @param addr
     *            The address of the controller. Default is 3.
     * 
     * @param subAddr
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
    public static String cmd(int addr, int subAddr, int command, int length, int data, boolean getResponse)
            throws InterruptedException {

        responseOn = getResponse;

        // Lock the NMX communications class
        NMXComsBusy = true;

        // Assemble command packet
        String header = "0000000000FF";
        String address = Integer.toHexString(addr);
        address = address.length() == 2 ? address : "0" + address; // Append
                                                                   // leading
                                                                   // zero if
                                                                   // necessary
        String subAddrStr = subAddr <= 15 ? "0" + Integer.toHexString(subAddr) : Integer.toHexString(subAddr);
        String commandStr = command <= 15 ? "0" + Integer.toHexString(command) : Integer.toHexString(command);
        String lengthStr = length <= 15 ? "0" + Integer.toHexString(length) : Integer.toHexString(length);
        String dataStr = Integer.toHexString(data).length() % 2 != 0 ? "0" + Integer.toHexString(data)
                : Integer.toHexString(data);
        commandPacket = header + address + subAddrStr + commandStr + lengthStr;

        // If the length is non-zero, then append the data
        if (length != 0) {
            // Make sure the data has any necessary leading zeros
            if (dataStr.length() / 2 != length) {
                int leadingZeroByes = length - (dataStr.length() / 2);
                for (int i = 0; i < leadingZeroByes; i++) {
                    dataStr = "00" + dataStr;
                }
            }
            // Append manual data (this is used for a few commands that require
            // a direction byte and steps in the same packet)
            commandPacket += manualData;
            // Append to the packet
            commandPacket += data;
            manualData = "";
        }

        // Set command ready flag to trigger command thread
        sendingCommand = true;

        return commandPacket;
    }

    // ******** Helper Methods ******** //

    /**
     * @param s
     *            A hex string
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

    public static int getEmptyResponseCount() {
        return emptyResponseCount;
    }

    /**
     * This method extracts any value passed from the controller in its response
     * packet. It should not be called until enough time has elapsed since
     * sending the last command for the command to finish executing on the
     * controller side and to send a complete response packet. The timing may
     * vary from command to command, so the timing delay may be adjusted with
     * the setTimingDelay() method.
     */
    private static void parseResponse() {

        if (serialDetail)
            System.out.println("NMX raw response: " + response);

        if (!responseOn) {
            responseVal = Consts.ERROR;
            return;
        }

        // This is an "OK" confirmation code, ignore it
        if (response.equals("0000000000ff00000100"))
            return;
        if (response.equals("")) {

            if (serialDetail)
                System.out.println("Empty response!");
            responseVal = Consts.ERROR;
            emptyResponseCount++;
            return;
        }
        if (response.equals(ERROR_STR)) {
            System.out.println("~~~RESPONSE TIMEOUT ERROR~~~");
            responseVal = Consts.ERROR;
            return;
        }
        emptyResponseCount = 0;

        // int length = Integer.decode("0x" + response.substring(18, 20));
        int dataType = 7;
        long data = ERROR;
        try {
            dataType = Integer.decode("0x" + response.substring(20, 22));
            try {
                data = Long.decode("0x" + response.substring(22, response.length()));
                try {
                    // Handle negative longs
                    if (dataType == 3 && Integer.decode("0x" + response.substring(22, 24)) == 255)
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
            if (serialDetail)
                System.out.println("Response value out of bounds!!!");
        }

        responseVal = (int) data;
    }

    // ******** Command Monitoring Thread ******** //

    /**
     * This class is a thread that handles the timing of sending commands to the
     * NMX controller. In order to avoid lagging of the UI on the EventDispach
     * thread, waiting for responses from the controller is handled here.
     * 
     * @author Michael
     *
     */
    private static class CommandThread implements Runnable {

        volatile boolean execute;

        int verifyPacketHeader(List<Integer> inBytes) {
            final String HEADER = "0000000000ff";
            String inByteStr = "";
            for (int x : inBytes) {
                String thisByte = x <= 15 ? "0" + Integer.toHexString(x) : Integer.toHexString(x);
                inByteStr += thisByte;
            }
            return inByteStr.indexOf(HEADER);
        }

        public void run() {
            execute = true;
            while (execute) {

                // Wait a while before checking if there a command is to be sent
                try {
                    Thread.sleep(2);
                } catch (InterruptedException e) {
                    System.out.println("NMX Command thread interrupted!");
                    e.printStackTrace();
                }

                if (sendingCommand) {

                    // Convert hex string to byte array and send to NMX
                    byte[] outCommand = hexStringToByteArray(commandPacket);

                    // Log the command time
                    long commandTime = System.currentTimeMillis();
                    final int MIN_TIME = 90;
                    long timeDiff = commandTime - lastCommandTime;
                    if (timeDiff < MIN_TIME) {
                        try {
                            // System.out.println("Sleeping for " + (MIN_TIME -
                            // timeDiff) + " millis");
                            Thread.sleep(MIN_TIME - timeDiff);
                        } catch (InterruptedException e) {
                            System.out.println("NMX Command thread interrupted!");
                            e.printStackTrace();
                        }
                    }
                    serial.write(outCommand);
                    lastCommandTime = commandTime;

                    // Retrieve response buffer
                    List<Integer> inBytes = new ArrayList<Integer>();

                    // Clear old response string and populate with chars from
                    // buffer
                    response = "";
                    boolean waitingForPreamble = true;
                    int index = 0;
                    int dataRemaining = 0;

                    // Wait for full packet before proceeding
                    long waitStart = System.currentTimeMillis();
                    // Give up if the first byte isn't received before the start
                    // timeout,
                    // or the packet isn't finished before the final timeout
                    final int DATA_LENGTH_BYTE = 9;
                    while (waitingForPreamble || dataRemaining > 0) {
                        if (serial.available() > 0) {
                            inBytes.add(serial.read());
                            if (index == DATA_LENGTH_BYTE) {
                                /*
                                 * Make sure the header is at the beginning of
                                 * the data. If it's not, decrement the index,
                                 * remove the first byte, then try again. If the
                                 * previous packet timed out, there's a good
                                 * chance the last byte will get appended to the
                                 * beginning of the current packet, so this
                                 * check accounts for that
                                 */
                                int headerLocation = verifyPacketHeader(inBytes);
                                if (headerLocation != 0) {
                                    index--;
                                    inBytes.remove(0);
                                }
                                else {
                                    dataRemaining = inBytes.get(index);
                                    waitingForPreamble = false;
                                }
                            }
                            else if (index > DATA_LENGTH_BYTE) {
                                dataRemaining--;
                            }
                            index++;
                        }
                        // Check for timeouts
                        long curTime = System.currentTimeMillis();
                        if (curTime - waitStart > responseTimeout) {
                            response = ERROR_STR;
                            serial.clear();
                            break;
                        }
                    }

                    if (!response.equals(ERROR_STR)) {
                        for (int x : inBytes) {
                            String debug = x <= 15 ? "0" + Integer.toHexString(x) : Integer.toHexString(x);
                            response = response + debug;
                        }
                    }

                    // Extract the data from the response packet
                    parseResponse();

                    // Cancel the command sending condition
                    sendingCommand = false;

                    // Unlock the NMX communications class
                    NMXComsBusy = false;
                }

            }
        };
    }

}
