package com.yoke.connection.messages.connection;

import com.yoke.connection.Message;

public class Disconnected extends Message {
    public String device;

    public Disconnected() {
        this("");
    }
    public Disconnected(String device) {
        this.device = device;
    }
}
