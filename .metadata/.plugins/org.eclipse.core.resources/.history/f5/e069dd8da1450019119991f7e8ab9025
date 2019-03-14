package com.yoke;

import javax.swing.JFrame;

import com.yoke.connection.MessageReceiver;
import com.yoke.connection.messages.computerCmds.ShutDownCmd;
import com.yoke.connection.messages.computerCmds.SleepCmd;
import com.yoke.connection.server.types.BluetoothServerConnection;

public class Main {
	public static void main(String[] args) {
		BluetoothServerConnection connection = new BluetoothServerConnection();
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
