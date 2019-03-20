package com.yoke.connection.messages.connection;

import com.yoke.connection.Message;

/**
 * A message representing that a device connection has failed
 */
public class ConnectionFailed extends Message {
	// Serialization ID
	private static final long serialVersionUID = 1061223223921709987L;

		// The exception that caused the failure (if applicable)
	public Exception exception;
	
	// A description of what went wrong
	public String description;
	
    /**
     * Creates a connection failed message
     * @param exception  The exception that caused the failure
     */
	public ConnectionFailed(Exception exception) {
		this(exception, "");
	}
	/**
	 * Creates a connection failed message
	 * @param description  A description of what cause the failure
	 */
	public ConnectionFailed(String description) {
		this(null, description);
	}
    /**
     * Creates a connection failed message
     * @param exception  The exception that caused the failure
     * @param description  A description of what cause the failure
     */
	public ConnectionFailed(Exception exception, String description) {
		this.exception = exception;
		this.description = description;
	}
}
