package com.yoke.connection.client;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.util.Log;

import com.yoke.connection.Connection;
import com.yoke.connection.messages.connection.ConnectionFailed;
import com.yoke.connection.messages.connection.SelectDevice;

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

    // Store the context for teh association with the app
    protected Context context;

    public BluetoothClientConnection(Context context) {
        // Keep a reference to tge activity that started the connection, as services need it
        this.context = context;
    }

    public void requestDevice(){
        if (!mBluetoothAdapter.isEnabled()) {
            // TODO: properly handle this error
            return;
        }

        pairedDevices = mBluetoothAdapter.getBondedDevices();

        String[] names = new String[pairedDevices.size()];
        int index = 0;

        // Go through all of the paired devices to get their names
        for (BluetoothDevice device: pairedDevices) {
            names[index++] = device.getName();
        }

        // Emit a request for the user to pick a device
        this.emit(new SelectDevice(names));
        Log.w("DETECT", "TEST");
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
                    // TODO: test error handling
                    this.emit(new ConnectionFailed("Selected device is not bonded"));
                }
            }
        }
    }

    public void destroy() {
        processThread.interrupt();
        closeSocket();
    }
    protected void closeSocket() {
        try {
            socket.close();
        } catch (IOException e) {
            // TODO: properly handle errors
            e.printStackTrace();
        }
    }

    protected void sendSingleMessage(byte[] message) {
        try {
            socket.getOutputStream().write(message);
        } catch (IOException e) {
            // TODO: properly handle errors
            e.printStackTrace();
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
                // TODO: properly handle the error
                return;
            }

            state = Connection.CONNECTED;

            try{
                // Setup an input stream to receive data
                InputStream inputStream = socket.getInputStream();

                System.out.println("waiting for input");

                while (true) {
                    int data = inputStream.read();
                    this.readByte(data);
                }
            }catch(IOException e) {
                // TODO: notify about disconnected
                closeSocket();
            }
        }
    }
}
