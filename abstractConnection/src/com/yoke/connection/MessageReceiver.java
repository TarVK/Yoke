package com.yoke.connection;

import java.io.IOException;
import java.net.URISyntaxException;

// Use an abstract class rather than interface in order to not
// allow for lambda expression of which the type parameters can't be
// retrieved at runtime

/**
 * An 'interface' (abstract class) used to listen for messages being received
 * @param <T> The message type that this receiver can receive
 */
public abstract class MessageReceiver <T extends Message> {
    public abstract void receive(T message) throws Exception;
}
