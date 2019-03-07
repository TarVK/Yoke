package com.yoke.connection.server.types;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.UUID;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import javax.microedition.io.StreamConnectionNotifier;

import com.yoke.connection.Connection;
import com.yoke.connection.Message;

public class BluetoothServerConnection extends Connection{
	// The thread used to set up new connections
	protected EstablishConnectionThread connectionThread;
	
	// The threads used to listen for incoming messages
	protected List<ProcessConnectionThread> processThreads = new ArrayList<ProcessConnectionThread>();
	
	// The output streams used to send messages
	protected List<OutputStream> outputStreams = new ArrayList<OutputStream>();
	
	public BluetoothServerConnection() {
		super();
		
		connectionThread = new EstablishConnectionThread();
		connectionThread.start();
	}
	
	/**
	 * Disposes all of the data associated with this connection
	 */
	public void destroy() {
		connectionThread.interrupt();
		for (ProcessConnectionThread pct: processThreads) {
			pct.interrupt();
		}
		for (OutputStream os: outputStreams) {
			try {				
				os.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Sends a message to the clients using bluetooth
	 * @param message the Byte data of the message to send
	 */
	protected void sendSingleMessage(byte[] message) {
		for (OutputStream os: outputStreams) {
			try {
				os.write(message);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}	
	
	/**
	 * A class that sets up a server to listen for newly made bluetooth connections
	 */
	class EstablishConnectionThread extends Thread {
	
		public void run() {
			establishConnection();
	    }

	    /** 
	     * listens for connection attempts from devices 
	     */
	    private void establishConnection() {
	        try {
	        	// setup the server to listen for connection
	        	LocalDevice local = LocalDevice.getLocalDevice();
	            local.setDiscoverable(DiscoveryAgent.GIAC);

	            UUID uuid = new UUID(80087355); // "04c6093b-0000-1000-8000-00805f9b34fb"
	            String url = "btspp://localhost:" + uuid.toString() + ";name=RemoteBluetooth";
	            StreamConnectionNotifier notifier = (StreamConnectionNotifier)Connector.open(url);
	            
	            // Await a connection
	            while(true) {
		            System.out.println("Bluetooth: Listening for devices");
	                StreamConnection connection = notifier.acceptAndOpen();
	                
	                // Create a new thread if a connection is made
	                ProcessConnectionThread processThread = new ProcessConnectionThread(connection);
	                processThread.start();
	                processThreads.add(processThread);

	                // Get an output stream to send message through
	                OutputStream os = connection.openOutputStream();
	                outputStreams.add(os);
		        }
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
	}

	/**
	 * A class to listen for incomming data from a bluetooth connection
	 */
	class ProcessConnectionThread extends Connection.ProcessConnectionThread {
	    private StreamConnection mConnection;	
	    public ProcessConnectionThread(StreamConnection connection) {
	        mConnection = connection;
            System.out.println("Bluetooth: Device connected");
	    }
	
	    @Override
	    public void run() {
	        try {
	            // Setup an input stream to receive data
	            InputStream inputStream = mConnection.openInputStream();
	
	            // Listen for incoming data
	            while (true) {
	                int data = inputStream.read();
	                this.readByte(data);
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
	}
}