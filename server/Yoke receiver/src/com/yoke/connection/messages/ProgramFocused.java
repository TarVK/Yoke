package com.yoke.connection.messages;

import com.yoke.connection.Message;

/**
 * A message representing a program getting focused on the desktop
 */
public class ProgramFocused extends Message {
	// Serialization ID
	private static final long serialVersionUID = 8893099537907701879L;
	
	// The path to the program that got focused
	public String programPath;
	
	/**
	 * Creates a program focused message
	 * @param programPath  The path of the program that received focus
	 */
	public ProgramFocused(String programPath) {
		this.programPath = programPath;
	}
}
