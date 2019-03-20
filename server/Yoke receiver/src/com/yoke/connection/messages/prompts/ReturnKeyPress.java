package com.yoke.connection.messages.prompts;

import com.yoke.connection.Message;

/**
 * A message containing the response for a key press request
 */
public class ReturnKeyPress extends Message {
	// Serialization ID
	private static final long serialVersionUID = -1771989680681923794L;
	
	// The key codes of the keys that are pressed
	public int[] keys;
	
	/**
	 * Creates a returnkeyPress message
	 * @param keys  The keys that have been pressed (Id from KeyEvent.)
	 */
	public ReturnKeyPress(int... keys) {
		this.keys = keys;
	}
}
