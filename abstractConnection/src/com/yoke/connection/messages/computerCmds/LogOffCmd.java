package com.yoke.connection.messages.computerCmds;

import com.yoke.connection.messages.ComputerCmd;

/**
 * A command representing logging of from a computer
 */
public class LogOffCmd extends ComputerCmd {
    // Serialization ID
    private static final long serialVersionUID = -9010951738387914577L;

    @Override
    public String toString() {
        return "Log off";
    }
}
