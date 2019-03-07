package com.yoke.connection.messages;

import com.yoke.connection.Message;

public abstract class ComputerCmd extends Message{
	// All available computer commands 
	protected static ComputerCmd[] commands = new ComputerCmd[] {};
	
	public ComputerCmd() {}
	
	/**
	 * Retrieves the name of this specific command
	 * @return The command's name
	 */
	public abstract String getName();
	
	/**
	 * Retrieves all available computer commands
	 * @return All available commands
	 */
	public static ComputerCmd[] getCommands() {
		return commands;
	}
}
