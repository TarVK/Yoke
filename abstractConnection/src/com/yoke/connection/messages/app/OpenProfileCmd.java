package com.yoke.connection.messages.app;

/**
 * A class representing opening of another profile in the app
 */
public class OpenProfileCmd extends AppCmd {
    // Serialization ID
    private static final long serialVersionUID = 4298204355886298903L;

    // The ID of the profile to be opened
    public long profileID;

    /**
     * Creates a new open profile command, to open a specific profile
     * @param profileID  The ID of the profile to open
     */
    public OpenProfileCmd(long profileID){
        this.profileID = profileID;
    }

    @Override
    public String toString() {
        return "Open profile " + this.profileID;
    }
}
