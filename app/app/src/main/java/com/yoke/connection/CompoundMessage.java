package com.yoke.connection;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CompoundMessage extends Message implements Iterable<CompoundMessage.MessageDelay>{
	protected List<MessageDelay> messages = new ArrayList<MessageDelay>();
	
	/**
	 * Adds a message to the composite message
	 * @param message  The message to send
	 * @param delay  The delay after which the message should be sent in milliseconds
	 */
	public void add(Message message, int delay) {
		this.add(message, delay, messages.size());
	}
	/**
	 * Adds a message to the composite message
	 * @param message  The message to send
	 * @param delay  The delay after which the message should be sent in milliseconds
	 * @param index  The index at which to add the message
	 */
	public void add(Message message, int delay, int index) {
		messages.add(index, new MessageDelay(message, delay));
	}
	
	/**
	 * Removes a message from the composite message
	 * @param index  The index to remove
	 */
	public void remove(int index) {
		messages.remove(index);
	}
	
	
	/**
	 * Creates an iterator to go through all pairs of messages and delays
	 */
	public Iterator<MessageDelay> iterator() {
		return messages.iterator();
	}

	/**
	 * A class to store the combination of messages and their delays
	 */
	class MessageDelay{
		public Message message;
		public Integer delay;
		MessageDelay(Message message, Integer delay){
			this.message = message;
			this.delay = delay;
		}
	}
}
