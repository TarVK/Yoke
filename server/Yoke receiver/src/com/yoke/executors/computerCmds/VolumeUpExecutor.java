package com.yoke.executors.computerCmds;

import com.yoke.connection.messages.computerCmds.VolumeUpCmd;

public class VolumeUpExecutor extends VirtualKeyExecutor<VolumeUpCmd>{

	@Override
	public void receive(VolumeUpCmd message) {
		sendKey(0xAF);
	}	
}