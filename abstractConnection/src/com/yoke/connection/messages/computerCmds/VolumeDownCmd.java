package com.yoke.connection.messages.computerCmds;

import com.yoke.connection.messages.ComputerCmd;

/**
 * A command representing turning the audio volume down
 */
public class VolumeDownCmd extends ComputerCmd {
    // Serialization ID
    private static final long serialVersionUID = -3014987900311099524L;

    @Override
    public String getName() {
        return "Volume Down";
    }
}
