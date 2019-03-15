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
import com.yoke.connection.messages.connection.ConnectionFailed;

public class BluetoothServerConnection extends Connection {
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
	protected void sendMessageStream(byte[] message) {
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

	        	state = Connection.CONNECTING;

	            // Await a connection
	            while(true) {
	                StreamConnection connection = notifier.acceptAndOpen();

	    	        state = CONNECTED;

	                // Get an output stream to send message through
	                OutputStream os = connection.openOutputStream();
	                // Get the input stream to retrieve messages from
	                InputStream is = connection.openInputStream();

	                // Create a new thread if a connection is made
	                ProcessConnectionThread processThread = new ProcessConnectionThread(is, os);
	                processThread.start();
	                processThreads.add(processThread);

	                // Store the output stream to send messages
	                outputStreams.add(os);
		        }
	        } catch (Exception e) {
	        	state = CONNECTIONFAILED;
	        	emit(new ConnectionFailed(e));
	            e.printStackTrace();
	        }
	    }
	}

	/**
	 * A class to listen for incoming data from a bluetooth connection
	 */
	class ProcessConnectionThread extends Connection.ProcessConnectionThread {
		// Keep track of the input and output streams of the connection
	    protected InputStream is;
	    protected OutputStream os;

	    public ProcessConnectionThread(InputStream is, OutputStream os) {
	        this.is = is;
	        this.os = os;
	    }

	    @Override
	    public void run() {
	        try {
	            // Listen for incoming data
	            while (true) {
	                int data = is.read();
	                // Check if the connection got closed

	                if (data == -1) {
	                	// If the device disconnected, destroy its stream
	                	outputStreams.remove(os);
	                	os.close();

	                	if (outputStreams.size() == 0) {
	                		state = Connection.CONNECTING;
	                	}
	                }

	                // Process the bit as usual
	                this.readByte(data);
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
	}
}
