package com.yoke.connection.messages.connection;

import com.yoke.connection.Message;

public class Connected extends Message {
	// Serialization ID
	private static final long serialVersionUID = -8995546060792314705L;

	// The local ID of the device the change applies to
	public int deviceID;
	
	public Connected(int ID) {
		deviceID = ID;
	}
}
