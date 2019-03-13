package com.yoke.connection.messages;

import com.yoke.connection.Message;

public class OpenURLCmd extends Message {
	// Serialization ID
	private static final long serialVersionUID = 5167797938713468374L;
	
	// The URL to open
	public String URL; 

	public OpenURLCmd(String URL) {
		this.URL = URL;
	}
}
