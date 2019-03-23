package com.yoke.executors.computerCmds;

import com.yoke.connection.messages.computerCmds.VolumeDownCmd;

/**
 * A message receiver that listens for volume down commands, and perform them
 */
public class VolumeDownExecutor extends VirtualKeyExecutor<VolumeDownCmd>{

    @Override
    public void receive(VolumeDownCmd message) {
        sendKey(0xAE);
    }    
}