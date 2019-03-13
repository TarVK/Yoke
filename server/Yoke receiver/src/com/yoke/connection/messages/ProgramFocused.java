package com.yoke.connection.messages;

import com.yoke.connection.Message;

public class ProgramFocused extends Message {
	// Serialization ID
	private static final long serialVersionUID = 8893099537907701879L;
	
	// The path to the program that got focused
	public String programPath;
	
	public ProgramFocused(String programPath) {
		this.programPath = programPath;
	}
}
