package com.yoke.connection.messages;

import com.yoke.connection.messages.softwareCmds.ToggleRecordingCmd;
import com.yoke.connection.Message;


/**
 * A general abstract class that can be extended for software commands 
 */
public abstract class SoftwareCmd extends Message {
    // Serialization ID
    private static final long serialVersionUID = 7562666274942589417L;
    
    // All available computer commands 
    protected static SoftwareCmd[] commands = new SoftwareCmd[] {
        new ToggleRecordingCmd(),
    };
    
    /**
     * Creates a software command
     */
    public SoftwareCmd() {}
    
    /**
     * Retrieves all available software commands
     * @return All available commands
     */
    public static SoftwareCmd[] getCommands() {
        return commands;
    }
}
