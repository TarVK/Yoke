package com.yoke.connection.messages;

import com.yoke.connection.Message;

public class OpenProgramCmd extends Message {
	// Serialization ID
	private static final long serialVersionUID = 1734343171548498878L;

	// The path to the program to be opened
	public String path;
	
	public OpenProgramCmd(String path) {
		this.path = path;
	}
}
