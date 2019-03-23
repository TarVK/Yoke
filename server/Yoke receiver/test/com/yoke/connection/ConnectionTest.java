package com.yoke.connection;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.junit.ClassRule;
import org.junit.jupiter.api.Test;
import org.junit.rules.Timeout;

import com.yoke.connection.Connection;
import com.yoke.connection.messages.ComputerCmd;
import com.yoke.connection.messages.computerCmds.ShutDownCmd;
import com.yoke.connection.messages.computerCmds.SleepCmd;

class ConnectionTest {
    // A variable to check callbacks
    boolean called = false;
    boolean notCalled = false;
    int delay1;
    int delay2;
    
    @Test
    void addReceiver() {
        ConnectionC connection = new ConnectionC();
        connection.addReceiver(new MessageReceiver<ShutDownCmd>() {
            @Override
            public void receive(ShutDownCmd message) {
                
            }
        });
    }

    @Test
    void removeReceiver() {
        ConnectionC connection = new ConnectionC();
        connection.addReceiver(new MessageReceiver<ShutDownCmd>() {
            @Override
            public void receive(ShutDownCmd message) {
                
            }
        });
        connection.addReceiver(new MessageReceiver<ShutDownCmd>() {
            @Override
            public void receive(ShutDownCmd message) {
                
            }
        });
    }
    
    @Test 
    void emitMessage() {
        ConnectionC connection = new ConnectionC();
        called = false;
        connection.addReceiver(new MessageReceiver<ShutDownCmd>() {
            @Override
            public void receive(ShutDownCmd message) {
                called = true;
            }
        });
        connection.emit(new ShutDownCmd());
        
        assertTrue(called);
    }

    @Test 
    void emitMessageSupertype() {
        ConnectionC connection = new ConnectionC();
        called = false;
        connection.addReceiver(new MessageReceiver<ComputerCmd>() {
            @Override
            public void receive(ComputerCmd message) {
                called = true;
            }
        });
        connection.emit(new ShutDownCmd());
        
        assertTrue(called);
    }

    @Test 
    void emitMessageOthertype() {
        ConnectionC connection = new ConnectionC();
        called = false;
        notCalled = true;
        connection.addReceiver(new MessageReceiver<ShutDownCmd>() {
            @Override
            public void receive(ShutDownCmd message) {
                called = true;
            }
        });
        connection.addReceiver(new MessageReceiver<SleepCmd>() {
            @Override
            public void receive(SleepCmd message) {
                notCalled = false;
            }
        });
        connection.emit(new ShutDownCmd());
        
        assertTrue(called, "receiver wasn't called");
        assertTrue(notCalled, "incorrect receiver was called");
    }
    

    @Test
    void emitCompoundMessage() {
        try {
            // Setup a future to await
            CompletableFuture<Void> resolve = new CompletableFuture<Void>();
            
            // Create a timeout for when the test fails
            new java.util.Timer().schedule( 
                new java.util.TimerTask() {
                    public void run() {
                        resolve.complete(null);
                    }
                }, 
                2000 
            );
            
            // Create a compound message
            CompoundMessage cm = new CompoundMessage();
            cm.add(new ShutDownCmd(), 1000);
            cm.add(new SleepCmd(), 500);
            
            // Get the current time in ms
            final long start = System.currentTimeMillis();
            
            // Create a connection that registers send message calls
            ConnectionC connection = new ConnectionC();
            connection.addReceiver(new MessageReceiver<ShutDownCmd>() {
                public void receive(ShutDownCmd message) {
                    delay1 = (int) (System.currentTimeMillis() - start);
                }
            });
            connection.addReceiver(new MessageReceiver<SleepCmd>() {
                public void receive(SleepCmd message) {
                    delay2 = (int) (System.currentTimeMillis() - start);
                    resolve.complete(null);
                }
            });
            
            // Send the messages
            connection.emit(cm);            
            
            // Wait for the future to complete
            resolve.get();
            int margin = 2;
            assertTrue(1000 <= delay1 && delay1 <= 1000 + margin);
            assertTrue(1500 <= delay2 && delay2 <= 1500 + margin);
        } catch (InterruptedException | ExecutionException  e) {
            e.printStackTrace();
        }
    }
    
    @Test
    void sendMessage() {
        called = false;
        ConnectionC connection = new ConnectionC() {
            protected void sendMessageStream(byte[] stream) { 
                called = true;
                
                // Get the message from the stream
                try {
                    stream = Arrays.copyOfRange(stream, 4, stream.length);
                    Object m = Message.deserialize(stream);
                    
                    assertTrue(m instanceof Message);
                } catch (IOException | ClassNotFoundException e) {
                    assertTrue(false);
                }
            }
        };        

        connection.send(new ShutDownCmd());
        assertTrue(called);
    }
}

/**
 * A concrete version of the Connection class
 */
class ConnectionC extends Connection{
    public void emit(Message message) {
        super.emit(message);
    }
    
    protected void sendMessageStream(byte[] message) { }
    public void destroy() { }
}