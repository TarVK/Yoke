package com.yoke.executors;

import com.yoke.connection.messages.OpenProgramCmd;

/**
 * A message receiver that listens for open program commands, and perform them
 */
public class OpenProgramExecutor extends CmdPromptExecutor<OpenProgramCmd>{

	@Override
	public void receive(OpenProgramCmd message) {
		execAdvancedCmd(message.path);
	}

}
