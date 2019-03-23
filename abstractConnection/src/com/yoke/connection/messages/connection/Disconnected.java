package com.yoke.connection.messages.connection;

import com.yoke.connection.Message;

/**
 * A message representing that a device has disconnected
 */
public class Disconnected extends Message {
    // Serialization ID
    private static final long serialVersionUID = -8607571329275515692L;

    // The local ID of the device the change applies to
    public int deviceID;
    
    /**
     * Creates a disconnected message
     * @param ID  The ID of the device that has been disconnected
     */
    public Disconnected(int ID) {
        deviceID = ID;
    }
}
