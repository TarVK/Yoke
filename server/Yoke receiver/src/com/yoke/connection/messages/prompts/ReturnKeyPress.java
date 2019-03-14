package com.yoke.connection.messages.prompts;

import com.yoke.connection.Message;

public class ReturnKeyPress extends Message {
	// Serialization ID
	private static final long serialVersionUID = -1771989680681923794L;
	
	// The key codes of the keys that are pressed
	public int[] keys;
	
	public ReturnKeyPress(int... keys) {
		this.keys = keys;
	}
}
