package com.yoke.connection;

// Use an abstract class rather than interface in order to not
// allow for lambda expression of which the type parameters can't be
// retrieved at runtime
public abstract class MessageReceiver <T extends Message> {
	abstract void receive(T message);
}
