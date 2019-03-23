package com.yoke.executors.computerCmds;

import com.yoke.connection.messages.computerCmds.ShutDownCmd;
import com.yoke.executors.CmdPromptExecutor;

/**
 * A message receiver that listens for shutdown commands, and perform them
 */
public class ShutDownExecutor extends CmdPromptExecutor<ShutDownCmd>{

    @Override
    public void receive(ShutDownCmd message) {
        execCmd("shutdown.exe -s");
    }
}