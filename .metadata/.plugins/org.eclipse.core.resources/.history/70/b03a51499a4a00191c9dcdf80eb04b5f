package com.yoke;

import java.awt.AWTException;
import java.awt.CheckboxMenuItem;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Menu;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

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
	/**
	 * Starts the program, doesn't take any arguments
	 * @param args
	 */
	public static void main(String[] args) {
		new Main();
	}
	
	// The connection to any potential remotes
	protected MultiServerConnection connection;
	
	// A list of the currently connected device IDs
	protected List<Integer> deviceIDs = new ArrayList<Integer>();
	
	// A reference to the tray
	protected Tray tray;
	
	public Main() {
		BluetoothServerConnection bluetooth = new BluetoothServerConnection();
		connection = new MultiServerConnection(bluetooth);
		
		setupConnectionListeners();
		setupExecutors();
		
		// Setup the tray
		tray = Tray.getInstance();
		tray.updateConnectedDevices(deviceIDs.size());
	}
	
	/*
	 * Sets up the listeners to check connection state changes
	 */
	protected void setupConnectionListeners() {
		connection.addReceiver(new MessageReceiver<Connected>() {
			public void receive(Connected cmd) {
				// Add the device and get its index
				deviceIDs.add(cmd.deviceID);
				int index = deviceIDs.indexOf(cmd.deviceID) + 1;
				
				// Notify that a device has connected
				tray.showMessage("Panel " + index + " has connected");

				// Update how many devices are connected
				tray.updateConnectedDevices(deviceIDs.size());
			}
		});
		connection.addReceiver(new MessageReceiver<Disconnected>() {
			public void receive(Disconnected cmd) {
				// Get the index of the device, and remove it
				int index = deviceIDs.indexOf(cmd.deviceID) + 1;
				deviceIDs.remove(cmd.deviceID);		
				
				// Notify that a device has connected
				tray.showMessage("Panel " + index + " has disconnected");
				
				// Go through all devices after, and indicate that index update
				for (int i = index + 1; i <= deviceIDs.size(); i++) {
					tray.showMessage("Panel " + i + " has now become panel " + (i - 1));
				}
				
				// Update how many devices are connected
				tray.updateConnectedDevices(deviceIDs.size());
			}
		});
		connection.addReceiver(new MessageReceiver<ConnectionFailed>() {
			public void receive(ConnectionFailed cmd) {		
				// Show that there was an error
				tray.showMessage("Something went wrong while connecting panel" + (cmd.description != "" ? ":" : ""));
				
				// Show the error message
				tray.showMessage(cmd.description);
				
				// Show the exact error in the console, TODO: make this accessable to the user
				if (cmd.exception != null) {
					cmd.exception.printStackTrace();
				}
			}
		});
	}
	
	/*
	 * Sets up all of the command executors
	 */
	protected void setupExecutors() {
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
