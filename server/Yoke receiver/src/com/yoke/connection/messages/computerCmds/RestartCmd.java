package com.yoke.connection.messages.computerCmds;

import com.yoke.connection.messages.ComputerCmd;

public class RestartCmd extends ComputerCmd {
	// Serialization ID
	private static final long serialVersionUID = -8379999244226975157L;

	public String getName() {
		return "Restart";
	}
}
