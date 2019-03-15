package com.yoke.connection.messages;

import com.yoke.connection.Message;

public class MoveMouseCmd extends Message {
	// Serialization ID
	private static final long serialVersionUID = 4796051523764478340L;

	// The amount of pixels to move on the x axis
	public int x;
	
	// The amount of pixels to move on the y axis
	public int y;
	
	// Whether to treat x and y as coordinates rather than offets
	public boolean absolute;
	
	public MoveMouseCmd(int x, int y) {
		this(x, y, false);
	}
	public MoveMouseCmd(int x, int y, boolean absolute) {
		this.x = x;
		this.y = y;
		this.absolute = absolute;
	}
}