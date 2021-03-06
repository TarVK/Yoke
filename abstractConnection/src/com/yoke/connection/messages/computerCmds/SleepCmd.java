package com.yoke.connection.messages.computerCmds;

import com.yoke.connection.messages.ComputerCmd;

/**
 * A command representing hibernating the computer
 */
public class SleepCmd extends ComputerCmd {
    // Serialization ID
    private static final long serialVersionUID = -6259637821869646630L;

    @Override
    public String toString() {
        return "Sleep";
    }
}
