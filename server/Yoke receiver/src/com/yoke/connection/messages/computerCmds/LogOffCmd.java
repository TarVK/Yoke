package com.yoke.connection.messages.computerCmds;

import com.yoke.connection.messages.ComputerCmd;

public class LogOffCmd extends ComputerCmd {
	// Serialization ID
	private static final long serialVersionUID = -9010951738387914577L;

	public String getName() {
		return "Logoff";
	}
}