package com.yoke.executors.computerCmds;

import com.yoke.connection.messages.computerCmds.NextTrackCmd;

public class NextTrackExecutor extends VirtualKeyExecutor<NextTrackCmd>{

	@Override
	public void receive(NextTrackCmd message) {
		sendKey(0xB0);
	}	
}