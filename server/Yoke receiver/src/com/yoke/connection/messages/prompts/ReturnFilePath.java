package com.yoke.connection.messages.prompts;

import com.yoke.connection.Message;

public class ReturnFilePath extends Message {
	// Serialization ID
	private static final long serialVersionUID = 5166198158857373512L;
	
	// The path of the file
	public String filePath;
	
	public ReturnFilePath(String filePath) {
		this.filePath = filePath;
	}
}
