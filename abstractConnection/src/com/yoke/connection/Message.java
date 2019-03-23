package com.yoke.connection;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * The class used for communication between the server and client
 * This class can be extended to any concrete message types that store data
 */
public class Message implements Serializable {
    // Serialization ID
    private static final long serialVersionUID = 3823107424651461814L;

    /**
     * Turns a message into a byte array to either be stored or sent
     * @param message  The message to serialize
     * @return The byte array that the message was turned into
     * @throws IOException
     */
    public static byte[] serialize(Message message) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(message);
        byte[] messageStream = baos.toByteArray();
        return messageStream;
    }

    /**
     * Turns a byte stream into a message
     * @param stream  The data to deserialize
     * @return The message that the byte array was turned into
     * @throws IOException
     * @throws ClassNotFoundException  If the message class couldn't be found
     * @throws IllegalArgumentException  If the stream data was not data for a message
     */
    public static Message deserialize(byte[] stream)
            throws IOException, ClassNotFoundException, IllegalArgumentException {
        ByteArrayInputStream bis = new ByteArrayInputStream(stream);
        ObjectInput ois = new ObjectInputStream(bis);
        Object message = ois.readObject();
        if (message instanceof Message) {
            return (Message) message;
        } else {
            throw new IllegalArgumentException("This stream was not a valid message");
        }
    }
}
