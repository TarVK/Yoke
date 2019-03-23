package com.yoke.connection.messages.prompts;

import com.yoke.connection.Message;

/**
 * A message containing the response for a file path request
 */
public class ReturnFilePath extends Message {
    // Serialization ID
    private static final long serialVersionUID = 5166198158857373512L;
    
    // The path of the file
    public String filePath;
    
    // Whether or not the prompt was cancelled
    public boolean cancelled = false;
    
    /**
     * Creates a returnFilePath message
     * @param filePath  The file path to return
     */
    public ReturnFilePath(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Creates a returnFilePath message if it was cancelled
     */
    public ReturnFilePath() {
        this.cancelled = true;
    }
}
