package com.yoke.connection.messages.connection;

import com.yoke.connection.Message;

public class ConnectionFailed extends Message {
	// Serialization ID
	private static final long serialVersionUID = 1061223223921709987L;

		// The exception that caused the failure (if applicable)
	public Exception exception;
	
	// A description of what went wrong
	public String description;
	

	public ConnectionFailed(Exception exception) {
		this(exception, "");
	}
	public ConnectionFailed(String description) {
		this(null, description);
	}
	public ConnectionFailed(Exception exception, String description) {
		this.exception = exception;
		this.description = description;
	}
}
