package com.yoke.executors.computerCmds;

import com.yoke.connection.messages.computerCmds.VolumeUpCmd;


/**
 * A message receiver that listens for volume up commands, and perform them
 */
public class VolumeUpExecutor extends VirtualKeyExecutor<VolumeUpCmd>{

    @Override
    public void receive(VolumeUpCmd message) {
        sendKey(0xAF);
    }    
}