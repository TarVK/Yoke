package com.yoke.connection.messages;

import com.yoke.connection.Message;


/**
 * A command representing opening a url
 */
public class OpenURLCmd extends Message {
    // Serialization ID
    private static final long serialVersionUID = 5167797938713468374L;
    
    // The URL to open
    public String URL; 

    /**
     * Creates an open URL command
     * @param URL  The URL to open
     */
    public OpenURLCmd(String URL) {
        this.URL = URL;
    }

    @Override
    public String toString() {
        return "Open URL " + URL;
    }
}
