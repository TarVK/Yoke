package com.yoke.connection.messages.computerCmds;

import com.yoke.connection.messages.ComputerCmd;

/**
 * A command representing shutting down the computer
 */
public class ShutDownCmd extends ComputerCmd {
    // Serialization ID
    private static final long serialVersionUID = 7663898149656611444L;

    @Override
    public String toString() {
        return "Shutdown";
    }
}
