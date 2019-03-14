package com.yoke.connection;

import java.util.Iterator;

public class RepeatMessage implements ComposedMessage{
	// Serialization ID
	private static final long serialVersionUID = 8396994954447114066L;

	// Stores the frequency
	public double frequency;
	
	// Stores the count
	public int repeatAmount;
	
	// Store the message type
	public Message message;
	
	
	public RepeatMessage(Message message) {
		this(message, 1, 10);
	}
	public RepeatMessage(Message message, int repeatAmount, double frequency) {
		this.message = message;
		this.repeatAmount = repeatAmount;
		this.frequency = frequency;
	}
	

	/**
	 * Creates an iterator to go through all pairs of messages and delays
	 */
	public Iterator<MessageDelay> iterator() {
		return new Iterator<MessageDelay>() {
			int index = 0;
			public boolean hasNext() {
				return index < repeatAmount;
			}

			public MessageDelay next() {
				if (index++ == 0) {
					return new MessageDelay(message, 0);
				} else {
					return new MessageDelay(message, (int) (1000.0/frequency));
				}
			}
		};
	}
}
