package com.yoke;

import javax.swing.JFrame;

import com.yoke.connection.Connection;
import com.yoke.connection.MessageReceiver;
import com.yoke.connection.messages.computerCmds.ShutDownCmd;
import com.yoke.connection.messages.computerCmds.SleepCmd;
import com.yoke.connection.messages.connection.Connected;
import com.yoke.connection.messages.connection.ConnectionFailed;
import com.yoke.connection.messages.connection.Disconnected;
import com.yoke.connection.server.MultiServerConnection;
import com.yoke.connection.server.types.BluetoothServerConnection;

public class Main {
	public static void main(String[] args) {
		BluetoothServerConnection bluetooth = new BluetoothServerConnection();
		MultiServerConnection connection = new MultiServerConnection(bluetooth);
		
		// Setup connection state listeners
		connection.addReceiver(new MessageReceiver<Connected>() {
			public void receive(Connected cmd) {				
				System.out.println("Device " + cmd.deviceID + " connected");
			}
		});
		connection.addReceiver(new MessageReceiver<Disconnected>() {
			public void receive(Disconnected cmd) {				
				System.out.println("Device " + cmd.deviceID + " disconnected");
			}
		});
		connection.addReceiver(new MessageReceiver<ConnectionFailed>() {
			public void receive(ConnectionFailed cmd) {		
				System.out.println("Something went wrong while connecting device; " 
						+ cmd.description);
				if (cmd.exception != null) {
					cmd.exception.printStackTrace();
				}
			}
		});
		
		// Setup cmd listeners
		connection.addReceiver(new MessageReceiver<ShutDownCmd>() {
			public void receive(ShutDownCmd cmd) {				
				System.out.println("received shutdown");
			}
		});
		connection.addReceiver(new MessageReceiver<SleepCmd>() {
			public void receive(SleepCmd cmd) {				
				System.out.println("received sleep");
				connection.send(cmd);
			}
		});
	}
}
