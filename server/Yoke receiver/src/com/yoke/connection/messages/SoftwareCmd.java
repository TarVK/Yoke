package com.yoke.connection.messages;

import com.yoke.connection.Message;
import com.yoke.connection.messages.softwareCmds.ToggleRecordingCmd;

public abstract class SoftwareCmd extends Message {
	// Serialization ID
	private static final long serialVersionUID = 7562666274942589417L;
	
	// All available computer commands 
	protected static SoftwareCmd[] commands = new SoftwareCmd[] {
		new ToggleRecordingCmd(),
	};
	
	public SoftwareCmd() {}
	
	/**
	 * Retrieves the name of this specific command
	 * @return The command's name
	 */
	public abstract String getName();
	
	/**
	 * Retrieves all available software commands
	 * @return All available commands
	 */
	public static SoftwareCmd[] getCommands() {
		return commands;
	}
}
