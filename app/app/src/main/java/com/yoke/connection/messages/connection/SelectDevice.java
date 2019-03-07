package com.yoke.connection.messages.connection;

import com.yoke.connection.Message;

public class SelectDevice extends Message {
    public String[] names;
    public SelectDevice(String[] names) {
        this.names = names;
    }
}
