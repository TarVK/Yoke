package com.yoke.executors.computerCmds;

import com.yoke.connection.messages.computerCmds.SleepCmd;
import com.yoke.executors.CmdPromptExecutor;

/**
 * A message receiver that listens for sleep commands, and perform them
 */
public class SleepExecutor extends CmdPromptExecutor<SleepCmd>{

	@Override
	public void receive(SleepCmd message) {
		execCmd("shutdown.exe -h");
	}
}
