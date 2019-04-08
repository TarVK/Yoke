package com.yoke.connection.messages.app;


import com.yoke.connection.Message;

/**
 * A command representing opening of the trackpad (on the client)
 */
public class OpenTrackpadCmd extends AppCmd {
    // Serialization ID
    private static final long serialVersionUID = 4298204355886298203L;

    @Override
    public String toString() {
        return "Open trackpad";
    }
}
