package com.yoke.executors.computerCmds;

import com.yoke.connection.messages.computerCmds.ShutDownCmd;
import com.yoke.executors.CmdPromptExecutor;

public class ShutDownExecutor extends CmdPromptExecutor<ShutDownCmd>{

	@Override
	public void receive(ShutDownCmd message) {
		execCmd("shutdown.exe -s");
	}
}