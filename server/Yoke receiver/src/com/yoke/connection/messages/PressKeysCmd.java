package com.yoke.connection.messages;

import com.yoke.connection.Message;

public class PressKeysCmd extends Message {
	// Serialization ID
	private static final long serialVersionUID = -2283998888388309525L;
	
	// The type of key presses that are available
	public static int KEYDOWN = 0;
	public static int KEYUP = 1;
	public static int KEYPRESS = 2;

	// The list of key codes to press
	public int[] keys;
	
	// The type of keypress
	public int type;
	
	public PressKeysCmd(int[] keys) {
		this(keys, KEYPRESS);
	}
	public PressKeysCmd(int[] keys, int type) {
		this.keys = keys;
		this.type = type;
	}
}