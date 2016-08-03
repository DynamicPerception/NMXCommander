package com.dynamicperception.nmxcommandline.models;

/**
 * This interface allows for construction of custom command execution behaviors.
 * An example would be a compound command that does not exist in the firmware,
 * but which is constructed of several firmware commands. <br>
 * <br>
 * For example, the following allows setting a motor to a constant velocity and
 * running it until stopped:<br>
 * <code><pre>
 * class MoveAtVelExecute implements ExecuteStructure{
 * 	Command thisCommand;
 *	public MoveAtVelExecute(Command thisCommand){
 *		this.thisCommand = thisCommand;
 *	}
 *	public &ltT&gt T executeThis() {
 *		System.out.println("Invalid command structure");
 *		thisCommand.help();
 *		return null;
 *	}
 *	public &ltT&gt T executeThis(String dataOrMotor) {
 *		System.out.println("Invalid command structure");
 *		thisCommand.help();
 *		return null;
 *	}
 *	public &ltT&gt T executeThis(String motor, String data) {
 *		Command.execute(Names.General.SET_GRAFFIK, true);
 *		Command.execute(Names.General.SET_JOYSTICK, true);
 *		Command.execute(Names.Motor.SET_VEL, motor, data);
 *		return null;
 *	}			
 * }
 * </pre></code>
 * 
 * @author Michael
 *
 */
public interface ExecuteStructure {
    <T> T executeThis();

    <T> T executeThis(String dataOrMotor);

    <T> T executeThis(String motor, String data);
}
