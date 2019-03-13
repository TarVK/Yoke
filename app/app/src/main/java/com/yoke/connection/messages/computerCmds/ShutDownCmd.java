package com.yoke.connection.messages.computerCmds;

import com.yoke.connection.messages.ComputerCmd;

public class ShutDownCmd extends ComputerCmd {
	// Serialization ID
	private static final long serialVersionUID = 7663898149656611444L;

	public String getName() {
		return "Shutdown";
	}
}
