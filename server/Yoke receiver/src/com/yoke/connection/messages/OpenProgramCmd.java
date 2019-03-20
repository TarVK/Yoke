package com.yoke.connection.messages;

import com.yoke.connection.Message;

/**
 * A command representing opening a computer program
 */
public class OpenProgramCmd extends Message {
	// Serialization ID
	private static final long serialVersionUID = 1734343171548498878L;

	// The path to the program to be opened
	public String path;
	
	/**
	 * Creates an open program command
	 * @param path  The path to the progra to open
	 */
	public OpenProgramCmd(String path) {
		this.path = path;
	}
}
