package com.yoke.connection;

import java.io.Serializable;

/**
 * An interface representing any composed message
 */
public interface ComposedMessage extends Iterable<ComposedMessage.MessageDelay>{
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
}
