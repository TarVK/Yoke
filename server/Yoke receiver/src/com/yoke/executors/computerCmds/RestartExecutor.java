package com.yoke.executors.computerCmds;

import com.yoke.connection.messages.computerCmds.RestartCmd;
import com.yoke.executors.CmdPromptExecutor;


/**
 * A message receiver that listens for restart commands, and perform them
 */
public class RestartExecutor extends CmdPromptExecutor<RestartCmd>{

    @Override
    public void receive(RestartCmd message) {
        execCmd("shutdown.exe -r");
    }
}