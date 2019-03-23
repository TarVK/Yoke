package com.yoke.connection;

import java.util.Iterator;

/**
 * A class to store a message that should be repeated a number of times
 */
public class RepeatMessage implements ComposedMessage{
    // Serialization ID
    private static final long serialVersionUID = 8396994954447114066L;

    // Stores the frequency
    public double frequency;
    
    // Stores the count
    public int repeatAmount;
    
    // Store the message type
    public Message message;
    
    /**
     * Creates a repeat method
     * @param message  The message to repeat
     */
    public RepeatMessage(Message message) {
        this(message, 1, 10);
    }
    /**
     * Creates a repeat method
     * @param message  The message to repeat
     * @param repeatAmount  The number of times to repeat the message
     * @param frequency  How often to execute the macro per second
     */
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
