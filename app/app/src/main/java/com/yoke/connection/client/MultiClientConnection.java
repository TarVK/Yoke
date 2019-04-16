package com.yoke.connection.client;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;

import com.yoke.activities.splash.SplashActivity;
import com.yoke.connection.Connection;
import com.yoke.connection.Message;
import com.yoke.connection.MessageReceiver;
import com.yoke.connection.messages.app.AppCmd;
import com.yoke.connection.messages.connection.Connected;
import com.yoke.connection.messages.connection.ConnectionFailed;
import com.yoke.connection.messages.connection.Disconnected;

public class MultiClientConnection extends Connection {
    // Singleton
    protected static MultiClientConnection INSTANCE;

    /**
     * Initializes the MultiClientConnection
     * @param type The concrete connection type to use
     */
    public static void initialize(Connection type) {
        INSTANCE = new MultiClientConnection(type);
    }

    /**
     * Retrieves an instance of this class, initialize must have been called first
     * @return an instance of the MultiClientConnection class
     */
    public static MultiClientConnection getInstance(Context context) {
        if (INSTANCE == null) {
            Intent intent = new Intent(context, SplashActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            context.startActivity(intent);
        }
        return INSTANCE;
    }

    /**
     * Properly destroys the connection instance
     */
    public static void destroyInstance() {
        if (INSTANCE != null) {
            INSTANCE.destroy();
            INSTANCE = null;
        }
    }

    // The concrete connection to use
    protected Connection connection;

    protected MultiClientConnection(Connection connection) {
        this.setType(connection);
    }

    /**
     * Changes the concrete connection type that is used
     * @param connection  The connection that should be used
     */
    public void setType(Connection connection) {


        // Check if the previous connection has to be disposed
        if (this.connection != null) {
            this.connection.destroy();
        }

        // Initialize the new connection
        this.connection = connection;

        // Update the connection state
        this.state = connection.getState();
        connection.addReceiver(new MessageReceiver<Connected>() {
            public void receive(Connected message) {
                updateState();
            }
        });
        connection.addReceiver(new MessageReceiver<Disconnected>() {
            public void receive(Disconnected message) {
                updateState();
            }
        });
        connection.addReceiver(new MessageReceiver<ConnectionFailed>() {
            public void receive(ConnectionFailed message) {
                updateState();
            }
        });

        // Forward all of the connection's events
        connection.addReceiver(new MessageReceiver<Message>() {
            public void receive(Message message) {
                emit(message);
            }
        });
    }

    protected void updateState() {
        this.state = connection.getState();
    }


    @Override
    public void destroy() {
        this.connection.destroy();
    }

    @Override
    public void send(Message message, boolean local) {
        if (message instanceof AppCmd || local) {
            // Emit message locally
            super.send(message, true);
        } else {
            // Send using connection
            this.connection.send(message);
        }
    }

    @Override
    protected void sendMessageStream(byte[] message) {
        // Isn't reached
    }
}
