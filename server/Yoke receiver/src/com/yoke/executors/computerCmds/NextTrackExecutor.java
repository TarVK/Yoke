package com.yoke.executors.computerCmds;

import com.yoke.connection.messages.computerCmds.NextTrackCmd;


/**
 * A message receiver that listens for next track commands, and perform them
 */
public class NextTrackExecutor extends VirtualKeyExecutor<NextTrackCmd>{

	@Override
	public void receive(NextTrackCmd message) {
		sendKey(0xB0);
	}	
}