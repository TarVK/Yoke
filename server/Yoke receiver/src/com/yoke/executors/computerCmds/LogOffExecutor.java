package com.yoke.executors.computerCmds;

import com.yoke.connection.messages.computerCmds.LogOffCmd;
import com.yoke.connection.messages.computerCmds.SleepCmd;
import com.yoke.executors.CmdPromptExecutor;

/**
 * A message receiver that listens for log off commands, and perform them
 */
public class LogOffExecutor extends CmdPromptExecutor<LogOffCmd>{

    @Override
    public void receive(LogOffCmd message) {
        execCmd("shutdown.exe -l");
    }
}
