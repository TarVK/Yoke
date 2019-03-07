package com.yoke.connection.messages.connection;

import com.yoke.connection.Message;

public class ConnectionFailed extends Message {
    public String error;

    public ConnectionFailed(){
        this("Connection failed");
    }
    public ConnectionFailed(String error) {
        this.error = error;
    }
}
