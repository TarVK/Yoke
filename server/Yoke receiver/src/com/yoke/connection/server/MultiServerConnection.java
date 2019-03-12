package com.yoke.connection.server;

import com.yoke.connection.Connection;
import com.yoke.connection.Message;
import com.yoke.connection.MessageReceiver;

public class MultiServerConnection extends Connection {
	// Store a list of concrete connection types
	protected Connection[] connections;
	
	MultiServerConnection(Connection[] connections){
		this.connections = connections;
		
		for (Connection c: connections) {
			// Register a receiver to forward messages for each connection
			c.addReceiver(new MessageReceiver<Message>() {
				public void receive(Message message) {
					emit(message);
				}
			});
		}
	}
	
	@Override
	public void destroy() {
		for (Connection c: connections) {
			c.destroy();
		}
	}

	@Override
	public void send(Message message) {
		for (Connection c: connections) {
			// Try to send the message with the connection
			try {				
				c.send(message);
			} catch (Exception e) {
				
			}
		}
	}
	
	@Override
	protected void sendSingleMessage(byte[] message) {
		// Isn't reached
	}

}
