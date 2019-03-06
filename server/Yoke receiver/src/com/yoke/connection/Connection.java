package com.yoke.connection;

import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import com.yoke.connection.CompoundMessage.MessageDelay;
import com.yoke.connection.messages.computerCmds.ShutDownCmd;

public abstract class Connection implements Runnable {
	// Simple 'enum' for connection states
	public static int SETUP = -2;
	public static int CONNECTIONFAILED = -1;
	public static int CONNECTING = 0;
	public static int CONNECTED = 1;
	
	// The current connection state
	protected int state = SETUP;
	
	// The list of receivers that are listening for messages
	protected HashMap<Class<? extends Message>, List<MessageReceiver<?>>> receivers 
		= new HashMap<Class<? extends Message>, List<MessageReceiver<?>>>(); 
	

	public Connection() {}
	
	/**
	 * Makes sure the setup method is only called once, within a thread
	 */
	public void run() {
		if (this.state != SETUP) {
			throw new IllegalStateException("This method may not be called after setup");
		}
		
		this.setup();
	}
	
	/**
	 * Sets up the connection
	 */
	protected abstract void setup();
	
	
	/**
	 * Sends a message over the channel, handles compound messages
	 * @param message  The message to send
	 */
	public void send(Message message) {
		if (message instanceof CompoundMessage) {
			CompoundMessage cm = (CompoundMessage) message;
			
			// Store the cumulative delay
			int cumulativeDelay = 0;
			
			// Create a timer
			Timer t = new java.util.Timer();
			
			// Go through all of the messages
			for (MessageDelay md: cm) {
				// Get the delay after which to send the message
				cumulativeDelay += md.delay;
				
				// Create an message sending thread
				t.schedule( 
			        new java.util.TimerTask() {
			            public void run() {
							send(md.message);
			            }
			        }, 
			        cumulativeDelay 
				);
			}
		} else {
			// If it's a non compound message, send it using sendSingleMessage
			this.sendSingleMessage(message);
		}
	}
	
	/**
	 * Sends a message over the channel, doesn't handle compound messages
	 * @param message  The message to send
	 */
	protected abstract void sendSingleMessage(Message message);
	
	/**
	 * Registers a receiver to listen for a specific message
	 * @param receiver  The receiver to register
	 */
	public void addReceiver(MessageReceiver<?> receiver) {
		// Get the message type to listen for
		Class<? extends Message> type = getMessageClass(receiver);
		
		// Get the list of receivers
		List<MessageReceiver<?>> receivers = this.receivers.get(type);
		
		// If the list of receivers doesn't exist, create it
		if (receivers == null) {
			receivers = new ArrayList<MessageReceiver<?>>();
			this.receivers.put(type, receivers);
		}
		
		// Add the receiver to the list
		receivers.add(receiver);
	}
	
	/**
	 * Removes a receiver to listen for a specific message
	 * @param receiver  The receiver to remove
	 */
	public void removeReceiver(MessageReceiver<?> receiver) {
		// Get the message type to listen for
		Class<? extends Message> type = getMessageClass(receiver);
		
		// Get the list of receivers
		List<MessageReceiver<?>> receivers = this.receivers.get(type);
		if (receivers != null) {
			// Remove the receiver from the list
			receivers.remove(receiver);
		}
	}
	
	/**
	 * Extracts the message type that a receiver is listening for
	 * @param reciever  The receiver to extract the data from
	 * @return The message class that was retrieved
	 */
	protected Class<? extends Message> getMessageClass(MessageReceiver<?> receiver) {
		// Find the receive methods
		Class<?> c = receiver.getClass();
		Method[] methods = c.getMethods();
		for (Method method: methods) { 
			if (method.getName()=="receive") {
				
				// Go through its parameters
				Parameter[] parameters = method.getParameters();
				for (Parameter parameter: parameters) {
					Class mClass = parameter.getType();
					
					// return the class
					return mClass;
				}
			}
		}
		
		// This line shouldn't be reached
		assert false;
		return null;
	}
	
	/**
	 * Forwards a certain message to all receivers for this message type
	 * @param message  The message to emit
	 */
	protected void emit(Message message) {
		// Go through all super classes of the message
		Class c = message.getClass();
		while (c != null) {
			
			// Get the receivers for this message type
			List<MessageReceiver<?>> receivers = this.receivers.get(c);
			
			// Make sure there are receivers for the message type
			if (receivers != null) {
				// Call each of the receivers
				for (MessageReceiver receiver: receivers) {
					try {				
						receiver.receive(message);
					}catch(Exception e) {
						System.out.println("Something went wrong while invoking a receiver");
						e.printStackTrace();
					}
				}
			}
			
			// Get the super class
			c = c.getSuperclass();
		}
	}
	
	/**
	 * Returns the current connection state, matchable using the global enums
	 * @return The current state of the connection
	 */
	protected int getState(){
		return this.state;
	}
}
