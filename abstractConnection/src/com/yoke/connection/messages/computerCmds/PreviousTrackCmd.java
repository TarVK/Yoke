package com.yoke.connection.messages.computerCmds;

import com.yoke.connection.messages.ComputerCmd;

/**
 * A command representing going to the previous music track
 */
public class PreviousTrackCmd extends ComputerCmd {
    // Serialization ID
    private static final long serialVersionUID = -665911888787301177L;

    @Override
    public String toString() {
        return "Previous Track";
    }
}