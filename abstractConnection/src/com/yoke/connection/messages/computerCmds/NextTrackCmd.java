package com.yoke.connection.messages.computerCmds;

import com.yoke.connection.messages.ComputerCmd;

/**
 *  A command representing going to the next music track
 */
public class NextTrackCmd extends ComputerCmd {
    // Serialization ID
    private static final long serialVersionUID = 76853691411453686L;

    @Override
    public String getName() {
        return "Next Track";
    }
}
