package com.yoke.connection;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import com.yoke.connection.messages.computerCmds.ShutDownCmd;
import com.yoke.connection.messages.connection.Connected;
import com.yoke.connection.messages.connection.Disconnected;

/**
 * The abstract class representing a connection to the application
 */
public abstract class Connection {
    // Simple 'enum' for connection states
    public static int SETUP = 0;
    public static int CONNECTIONFAILED = 1;
    public static int CONNECTING = 2;
    public static int CONNECTED = 3;
    public static int DISCONNECTED = 4;
    
    // The current connection state
    protected int state = SETUP;
    
    // The list of receivers that are listening for messages
    protected HashMap<Class<? extends Message>, List<MessageReceiver<?>>> receivers 
        = new HashMap<Class<? extends Message>, List<MessageReceiver<?>>>();

    // The list of receivers that are listening for send messages (instead of received)
    protected HashMap<Class<? extends Message>, List<MessageReceiver<?>>> sendReceivers
            = new HashMap<Class<? extends Message>, List<MessageReceiver<?>>>();


    /**
     * The constructor method
     */
    public Connection() {}
    
    
    /**
     * Destroys the connection
     */
    public abstract void destroy();    
    
    /**
     * Sends a message over the channel, by turning it into a byte array
     * @param message  The message to send
     */
    public void send(Message message) {
        try {
            // Send the message to local receivers
            emitToReceivers(message, this.sendReceivers);

            // If it's a non compound message, serialize it        
            byte[] messageStream = Message.serialize(message);
            int size = messageStream.length;
            
            // Prefix the message with the byte size
            byte[] stream = new byte[size + 4];
            System.arraycopy(messageStream, 0, stream, 4, size);
            stream[0] = (byte)(size >> 24);
            stream[1] = (byte)(size >> 16);
            stream[2] = (byte)(size >> 8);
            stream[3] = (byte)(size);
        
            // Send the message
            this.sendMessageStream(stream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Sends a message stream over the channel
     * @param message  The byte code of the message to send
     */
    protected abstract void sendMessageStream(byte[] message);


    /**
     * Registers a receiver to listen for a specific message
     * @param receiver  The receiver to register
     */
    public void addReceiver(MessageReceiver<?> receiver) {
        this.addReceiver(receiver, false);
    }

    /**
     * Registers a receiver to listen for a specific message
     * @param receiver  The receiver to register
     * @param onSend  Whether it listens for sending of messages instead of receiving
     *                If set to true, the receiver will trigger if this connection sends a message
     */
    public void addReceiver(MessageReceiver<?> receiver, boolean onSend) {
        // Get the message type to listen for
        Class<? extends Message> type = getMessageClass(receiver);
        
        // Get the list of receivers
        HashMap<Class<? extends Message>, List<MessageReceiver<?>>> receiverMap =
                onSend ? this.sendReceivers : this.receivers;
        List<MessageReceiver<?>> receivers = receiverMap.get(type);
        
        // If the list of receivers doesn't exist, create it
        if (receivers == null) {
            receivers = new ArrayList<MessageReceiver<?>>();
            receiverMap.put(type, receivers);
        }
        
        // Add the receiver to the list
        receivers.add(receiver);
    }


    /**
     * Removes a receiver to listen for a specific message
     * @param receiver  The receiver to remove
     */
    public void removeReceiver(MessageReceiver<?> receiver) {
        this.removeReceiver(receiver, false);
    }

    /**
     * Removes a receiver to listen for a specific message
     * @param receiver  The receiver to remove
     * @param onSend  Whether it listens for sending of messages instead of receiving
     *                If set to true, the receiver will trigger if this connection sends a message
     */
    public void removeReceiver(MessageReceiver<?> receiver, boolean onSend) {
        // Get the message type to listen for
        Class<? extends Message> type = getMessageClass(receiver);
        
        // Get the list of receivers
        List<MessageReceiver<?>> receivers = onSend ? this.sendReceivers.get(type)
                : this.receivers.get(type);

        if (receivers != null) {
            // Remove the receiver from the list
            receivers.remove(receiver);
        }
    }
    
    /**
     * Extracts the message type that a receiver is listening for
     * @param receiver  The receiver to extract the data from
     * @return The message class that was retrieved
     */
    protected Class<? extends Message> getMessageClass(MessageReceiver<?> receiver) {        
        // Find the receive methods
        Class<?> c = receiver.getClass();
        Method[] methods = c.getMethods();
        for (Method method: methods) { 
            if (method.getName()=="receive") {
                
                // Go through its parameters
                Type[] parameters = method.getParameterTypes();
                
                // Make sure there is exactly one parameter and get it
                if (parameters.length != 1) {
                    continue;
                }
                Class mClass = (Class) parameters[0];
                
                // Make sure it's a valid message class
                if (!Message.class.isAssignableFrom(mClass)) {
                    continue;
                }
                    
                // return the class if it isn't the message class
                if (mClass != Message.class) {
                    return (Class<? extends Message>) mClass;
                }
            }
        }
        
        // If no non-message class could be found, return the message class
        return Message.class;
    }
    
    /**
     * Translate the byte stream to a message and 
     * forwards a certain message to all receivers for this message type
     * @param stream  The message to emit
     * @throws IllegalArgumentException if no message could be created from the byte array
     */
    protected void emit(byte[] stream) throws IllegalArgumentException {
        try {
            this.emit(Message.deserialize(stream));
        } catch(IOException | ClassNotFoundException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("The given byte array could not be converted to a message");
        }
    }
    
    /**
     * Forwards a certain message to all receivers for this message type
     * @param message  The message to emit
     */
    protected void emit(Message message) {
        // Check whether it is a regular message, or a compound message
        if (message instanceof CompoundMessage) {
            // If it is a compound message, sequence the emit properly
            CompoundMessage cm = (CompoundMessage) message;

            // Store the cumulative delay
            int cumulativeDelay = 0;

            // Create a timer
            Timer t = new java.util.Timer();

            // Go through all of the messages
            for (ComposedMessage.MessageDelay md: cm) {
                // Get the delay after which to send the message
                cumulativeDelay += md.delay;

                // Create an message sending thread
                t.schedule(
                    new java.util.TimerTask() {
                        public void run() {
                            emit(md.message);
                        }
                    },
                    cumulativeDelay
                );
            }
        } else {
            emitToReceivers(message, this.receivers);
        }
    }

    /**
     * Forwards a certain message to subset of passed receivers for this message type
     * Doesn't handle compound messages
     * @param message  The message to emit
     * @param receiverMap  The possible receivers to send it to
     */
    protected void emitToReceivers(Message message,
                                   HashMap<Class<? extends Message>,
                                           List<MessageReceiver<?>>> receiverMap) {
        // Go through all super classes of the message
        Class c = message.getClass();
        while (c != null) {

            // Get the receivers for this message type
            List<MessageReceiver<?>> receivers = receiverMap.get(c);

            // Make sure there are receivers for the message type
            if (receivers != null) {
                // Call each of the receivers
                for (MessageReceiver receiver: receivers) {
                    invokeReceiver(receiver, message);
                }
            }

            // Get the super class
            c = c.getSuperclass();
        }
    }
    
    /**
     * Invokes a message receiver and deals with its potential errors
     * @param receiver  The receiver to invoke
     * @param message  The message to invoke the receiver with
     */
    public void invokeReceiver(MessageReceiver receiver, Message message) {
        try {
            receiver.receive(message);
        }catch(Exception e) {
            System.out.println("Something went wrong while invoking a receiver");
            e.printStackTrace();
        }
    }
    
    /**
     * Returns the current connection state, matchable using the global enums
     * @return The current state of the connection
     */
    public int getState(){
        return this.state;
    }
    
    /**
     * Returns a unique ID to keep track of connected devices
     * @return A unique ID
     */
    public static int getNextID() {
        return uniqueID ++;
    }
    protected static int uniqueID = 0;
    
    /**
     * A class to process incoming data
     */
    public abstract class ProcessConnectionThread extends Thread {        
        // Store the ID for the connection/device
        protected int ID;
        
        // Keep track of the received size bytes
        protected byte[] size = new byte[4];
        protected byte sizeIndex = 0;
        
        // Keep track of the actual message bytes
        protected byte[] message;
        protected int messageIndex = 0;
        
        public ProcessConnectionThread() {
            this.ID = Connection.getNextID();
            state = CONNECTED;
            emit(new Connected(ID));
        }
        
        /**
         * The run method to be implemented by a specific connection
         */
        public abstract void run();
        
        /**
         * Reads one byte of data, and handles closing of the stream (on -1 received)
         * @param data  The byte of data, represented as a bit
         * @return Whether or not the device disconnected
         */
        protected boolean readByte(int data) {
            // If the data is -1, the connection has been closed
            if (data == -1) {

                emit(new Disconnected(ID));
                return true;
            } else {
                readByte((byte) data);
            }
            return false;
        }
        
        /**
         * Reads a single byte of data
         * @param data
         */
        protected void readByte(byte data) {
            // Check if the bytes should be added to the size
            if (message == null) {
                size[sizeIndex++] = data;
                
                // Check if we have received an int of data yet
                if (sizeIndex == 4) {
                    int s = (size[0]) << 24 | 
                            (size[1] & 0xFF) << 16 | 
                            (size[2] & 0xFF) << 8 | 
                            (size[3] & 0xFF);
                    
                    // Create the message array
                    message = new byte[s];
                    messageIndex = 0;
                    
                    // Reset the size input
                    size = new byte[4];
                    sizeIndex = 0;
                }
            } else {
                message[messageIndex++] = data;
                
                // Check if the whole message has been received
                if (messageIndex == message.length) {
                    // Emit the message
                    emit(message);
                    
                    // Continue receiving the next message
                    message = null;
                }
            }
        }
    }
}
