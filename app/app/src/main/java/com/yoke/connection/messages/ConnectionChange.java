package com.yoke.connection.messages;

import com.yoke.connection.Message;

public class ConnectionChange extends Message {
	// Serialization ID
	private static final long serialVersionUID = -7713281877315187083L;
	
	// The different available connection type
	public static int CONNECTIONFAILED = -1;
	public static int CONNECTED = 0;
	public static int DISCONNECTED = 1;
	
	// The local ID of the device the change applies to
	public int deviceID;
	
	// The type of change that occurred
	public int type;
	
	// Potential error
	public Exception exception;
	
	public ConnectionChange(int ID, int type) {
		this(ID, type, null);
	}
	public ConnectionChange(int ID, int type, Exception exception) {
		this.deviceID = ID;
		this.type = type;
		this.exception = exception;
	}
}
