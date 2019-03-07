package com.yoke.connection.messages.connection;

import com.yoke.connection.Message;

public class Connected extends Message {
    public String device;

    public Connected() {
        this("");
    }
    public Connected(String device) {
        this.device = device;
    }
}
