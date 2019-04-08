package com.yoke.connection.messages.computerCmds;

import com.yoke.connection.messages.ComputerCmd;

/**
 * A command representing turning the audio volume up
 */
public class VolumeUpCmd extends ComputerCmd {
    // Serialization ID
    private static final long serialVersionUID = 3309191022047453317L;

    @Override
    public String toString() {
        return "Volume Up";
    }
}