package com.yoke.executors;

import com.yoke.connection.messages.OpenProgramCmd;

public class OpenProgramExecutor extends CmdPromptExecutor<OpenProgramCmd>{

	@Override
	public void receive(OpenProgramCmd message) {
		execAdvancedCmd(message.path);
	}

}
