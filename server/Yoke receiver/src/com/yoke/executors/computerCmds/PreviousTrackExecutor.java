package com.yoke.executors.computerCmds;

import com.yoke.connection.messages.computerCmds.PreviousTrackCmd;


/**
 * A message receiver that listens for previous track commands, and perform them
 */
public class PreviousTrackExecutor extends VirtualKeyExecutor<PreviousTrackCmd>{

    @Override
    public void receive(PreviousTrackCmd message) {
        sendKey(0xB1);
    }    
}