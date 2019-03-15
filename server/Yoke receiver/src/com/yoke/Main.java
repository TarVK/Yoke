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
import com.yoke.executors.ClickMouseExecutor;
import com.yoke.executors.MoveMouseExecutor;
import com.yoke.executors.OpenProgramExecutor;
import com.yoke.executors.OpenURLExecutor;
import com.yoke.executors.PressKeysExecutor;
import com.yoke.executors.computerCmds.LogOffExecutor;
import com.yoke.executors.computerCmds.NextTrackExecutor;
import com.yoke.executors.computerCmds.PlayPauseExecutor;
import com.yoke.executors.computerCmds.PreviousTrackExecutor;
import com.yoke.executors.computerCmds.RestartExecutor;
import com.yoke.executors.computerCmds.ShutDownExecutor;
import com.yoke.executors.computerCmds.SleepExecutor;
import com.yoke.executors.computerCmds.VolumeDownExecutor;
import com.yoke.executors.computerCmds.VolumeUpExecutor;

public class Main {
	public static void main(String[] args) {
		BluetoothServerConnection bluetooth = new BluetoothServerConnection();
		MultiServerConnection connection = new MultiServerConnection(bluetooth);
		
		// Setup connection state listeners
		connection.addReceiver(new MessageReceiver<Connected>() {
			public void receive(Connected cmd) {				
				System.out.println("Device " + cmd.deviceID + " connected, state: " + connection.getState());
			}
		});
		connection.addReceiver(new MessageReceiver<Disconnected>() {
			public void receive(Disconnected cmd) {				
				System.out.println("Device " + cmd.deviceID + " disconnected, state: " + connection.getState());
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
		connection.addReceiver(new PlayPauseExecutor());
		connection.addReceiver(new PreviousTrackExecutor());
		connection.addReceiver(new NextTrackExecutor());
		connection.addReceiver(new VolumeUpExecutor());
		connection.addReceiver(new VolumeDownExecutor());
		
		connection.addReceiver(new ShutDownExecutor());
		connection.addReceiver(new SleepExecutor());
		connection.addReceiver(new RestartExecutor());
		connection.addReceiver(new LogOffExecutor());
		
		connection.addReceiver(new MoveMouseExecutor());
		connection.addReceiver(new ClickMouseExecutor());
		connection.addReceiver(new PressKeysExecutor());
		
		connection.addReceiver(new OpenProgramExecutor());
		connection.addReceiver(new OpenURLExecutor());
		
	}
}
