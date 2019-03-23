package com.yoke.executors.computerCmds;

import com.yoke.connection.messages.computerCmds.PlayPauseCmd;


/**
 * A message receiver that listens for play pause commands, and perform them
 */
public class PlayPauseExecutor extends VirtualKeyExecutor<PlayPauseCmd>{

	@Override
	public void receive(PlayPauseCmd message) {
		sendKey(0xB3);
	}	
}
