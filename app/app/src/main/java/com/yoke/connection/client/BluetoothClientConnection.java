package com.yoke.connection.client;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.util.Log;

import com.yoke.connection.Connection;
import com.yoke.connection.messages.ConnectionChange;
import com.yoke.connection.messages.connection.ConnectionFailed;
import com.yoke.connection.messages.connection.Disconnected;

import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

public class BluetoothClientConnection extends Connection {
    // Get android's bluetooth adapater
    private static BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

    // Keep a reference to the stored devices
    protected Set<BluetoothDevice> pairedDevices;

    // Keep a reference to the threads for proper disposal
    protected ProcessConnectionThread processThread;

    // Store the socket to send data with
    protected BluetoothSocket socket;

    // Store the context for the association with the app
    protected Context context;

    /**
     * @param context The context to keep an association with the specific app
     */
    public BluetoothClientConnection(Context context) {
        // Keep a reference to tge activity that started the connection, as services need it
        this.context = context;
    }

    public String[] getDeviceNames(){
        if (!mBluetoothAdapter.isEnabled()) {
            // TODO: properly handle this error
            return null;
        }

        pairedDevices = mBluetoothAdapter.getBondedDevices();

        String[] names = new String[pairedDevices.size()];
        int index = 0;

        // Go through all of the paired devices to get their names
        for (BluetoothDevice device: pairedDevices) {
            names[index++] = device.getName();
        }

        // Emit a request for the user to pick a device
        return names;
    }


    public void selectDevice(String deviceName) {
        // Look for the selected device
        for (BluetoothDevice device: pairedDevices) {
            if (deviceName.equals(device.getName())){
                if (device.getBondState() == BluetoothDevice.BOND_BONDED) {
                    processThread = new ProcessConnectionThread(device);
                    processThread.start();
                    this.state = Connection.CONNECTING;
                } else {
                    this.state = Connection.CONNECTIONFAILED;
                    this.emit(new ConnectionFailed("Selected device is not bonded"));
                }
                return;
            }
        }

        // If the device couldn't be found
        this.state = Connection.CONNECTIONFAILED;
        this.emit(new ConnectionFailed("Unknown device selected"));
    }

    @Override
    public void destroy() {
        processThread.interrupt();
        closeSocket();
    }

    /**
     * Closes the socket
     */
    protected void closeSocket() {
        try {
            socket.close();
        } catch (IOException e) {
            // TODO: properly handle errors
            e.printStackTrace();
        }
    }

    @Override
    protected void sendMessageStream(byte[] message) {
        if (this.state == CONNECTED) {
            try {
                socket.getOutputStream().write(message);
            } catch (IOException e) {
                // TODO: properly handle errors
                e.printStackTrace();
            }
        }
    }

    /**
     * A class to listen for incoming data from a bluetooth connection
     */
    class ProcessConnectionThread extends Connection.ProcessConnectionThread {
        // Keep a reference to the device to connect with
        private final BluetoothDevice device;
        public ProcessConnectionThread(BluetoothDevice device) {
            this.device = device;
            try {
                socket = device.createRfcommSocketToServiceRecord(
                        UUID.fromString("04c6093b-0000-1000-8000-00805f9b34fb"));
            } catch (IOException e) {
                // TOOD: properly handle error
                e.printStackTrace();
            }
        }

        public void run() {
            establishConnection();
        }

        /**
         * listens for connection attempts from devices
         */
        private void establishConnection() {
            // Cancel discovery because it otherwise slows down the connection.
            mBluetoothAdapter.cancelDiscovery();

            try {
                // Connect to the remote device through the socket. This call blocks
                // until it succeeds or throws an exception.
                socket.connect();
            } catch (IOException e) {
                e.printStackTrace();
                closeSocket();

                emit(new ConnectionFailed(e));
                state = Connection.CONNECTIONFAILED;
                emit(new Disconnected(this.ID));

                return;
            }

            state = Connection.CONNECTED;

            try{
                // Setup an input stream to receive data
                InputStream inputStream = socket.getInputStream();

                System.out.println("waiting for input");

                boolean awaitingMessages = true;
                while (awaitingMessages) {
                    int data = inputStream.read();

                    if (data == -1) {
                        // If the device disconnected, indicate this
                        state = Connection.DISCONNECTED;
                        awaitingMessages = false;
                        closeSocket();
                    }

                    this.readByte(data);
                }
            }catch(IOException e) {
                // TODO: notify about disconnected
                closeSocket();
                state = Connection.DISCONNECTED;
                emit(new Disconnected(this.ID));
            }
        }
    }
}
