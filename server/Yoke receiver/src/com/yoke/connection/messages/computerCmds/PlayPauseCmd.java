package com.yoke.connection.messages.computerCmds;

import com.yoke.connection.messages.ComputerCmd;

/**
 * A command representing playing or pausing of audio (toggle)
 */
public class PlayPauseCmd extends ComputerCmd {
	// Serialization ID
	private static final long serialVersionUID = 8298204355886298203L;

	@Override
	public String getName() {
		return "Play/Pause Music";
	}
}
