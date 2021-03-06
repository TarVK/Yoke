package com.yoke.connection.messages.connection;

import com.yoke.connection.Message;

/**
 * A message representing that a device has been connected
 */
public class Connected extends Message {
    // Serialization ID
    private static final long serialVersionUID = -8995546060792314705L;

    // The local ID of the device the change applies to
    public int deviceID;
    
    /**
     * Creates a connected message
     * @param ID  The ID of the device that has connected
     */
    public Connected(int ID) {
        deviceID = ID;
    }
}
