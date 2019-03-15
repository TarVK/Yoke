package com.yoke.connection;

import java.io.Serializable;

public interface ComposedMessage extends Iterable<MessageDelay>{
	
}

/**
 * A class to store the combination of messages and their delays
 */
class MessageDelay implements Serializable {
	// Serialization ID
	private static final long serialVersionUID = 719043628021174700L;
	
	// The message and it's activation delay
	public Message message;
	public Integer delay;
	MessageDelay(Message message, Integer delay){
		this.message = message;
		this.delay = delay;
	}
}