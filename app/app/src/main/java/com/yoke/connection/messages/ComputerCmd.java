package com.yoke.connection.messages;

import com.yoke.connection.Message;
import com.yoke.connection.messages.computerCmds.LogOffCmd;
import com.yoke.connection.messages.computerCmds.NextTrackCmd;
import com.yoke.connection.messages.computerCmds.PlayPauseCmd;
import com.yoke.connection.messages.computerCmds.PreviousTrackCmd;
import com.yoke.connection.messages.computerCmds.RestartCmd;
import com.yoke.connection.messages.computerCmds.ShutDownCmd;
import com.yoke.connection.messages.computerCmds.SleepCmd;
import com.yoke.connection.messages.computerCmds.VolumeDownCmd;
import com.yoke.connection.messages.computerCmds.VolumeUpCmd;

public abstract class ComputerCmd extends Message {
	// Serialization ID
	private static final long serialVersionUID = -4832852006823409330L;
	
	// All available computer commands 
	protected static ComputerCmd[] commands = new ComputerCmd[] {
		new LogOffCmd(),
		new SleepCmd(),
		new RestartCmd(),
		new ShutDownCmd(),
		new PlayPauseCmd(),
		new NextTrackCmd(),
		new PreviousTrackCmd(),
		new VolumeUpCmd(),
		new VolumeDownCmd(),
	};
	
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
