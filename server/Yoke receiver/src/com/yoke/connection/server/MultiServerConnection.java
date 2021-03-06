package com.yoke.connection.server;

import com.yoke.Tray;
import com.yoke.connection.Connection;
import com.yoke.connection.Message;
import com.yoke.connection.MessageReceiver;
import com.yoke.connection.messages.connection.Connected;
import com.yoke.connection.messages.connection.ConnectionFailed;
import com.yoke.connection.messages.connection.Disconnected;

/**
 * A class that can contain multiple connection types, and handles connections through each of them
 */
public class MultiServerConnection extends Connection {
    // Store a list of concrete connection types
    protected Connection[] connections;
    
    /**
     * Creates a multieServerConnection instance
     * @param connections  The concrete connection types that should be used
     */
    public MultiServerConnection(Connection... connections){
        this.connections = connections;
        
        for (Connection c: connections) {
            // Register a receiver to forward messages for each connection
            c.addReceiver(new MessageReceiver<Message>() {
                public void receive(Message message) {
                    emit(message);
                }
            });
            
            // Add state management methods
            c.addReceiver(new MessageReceiver<Connected>() {
                public void receive(Connected message) {
                    recomputeState();
                }
            });
            c.addReceiver(new MessageReceiver<Disconnected>() {
                public void receive(Disconnected message) {
                    recomputeState();
                }
            });
            c.addReceiver(new MessageReceiver<ConnectionFailed>() {
                public void receive(ConnectionFailed message) {
                    recomputeState();
                }
            });
        }
    }
    
    /**
     * Computes its current state based on its child states
     */
    protected void recomputeState() {
        // Encodes how many connections have a certain state, where the state is the index
        int[] states = new int[4];
        
        // Increase the count for every state
        for (Connection c: connections) {
            states[c.getState()]++;
        }
        
        // Decide what the current state should be
        if (states[SETUP] > 0) {
            this.state = SETUP;
        } else if (states[CONNECTED] > 0) {
            this.state = CONNECTED;
        } else if (states[CONNECTIONFAILED] > 0) {
            this.state = CONNECTIONFAILED;
        } else {
            this.state = CONNECTING;
        }
    }
    
    @Override
    public void invokeReceiver(MessageReceiver receiver, Message message) {
        try {
            receiver.receive(message);
        }catch(Exception e) {
            Tray.getInstance().showMessage("Your computer doesn't seem to support this action, our apologies.");
            e.printStackTrace();
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
            c.send(message);
        }
    }
    
    @Override
    protected void sendMessageStream(byte[] message) {
        // Isn't reached
    }

}
