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
	
	public Main() {
		BluetoothServerConnection bluetooth = new BluetoothServerConnection();
		connection = new MultiServerConnection(bluetooth);
		
		setupTray();
		setupConnectionListeners();
		setupExecutors();
	}
	
	/*
	 * Sets up the listeners to check connection state changes
	 */
	protected void setupConnectionListeners() {
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
	
	/**
	 * Sets up the tray interface
	 */
	protected MenuItem connectedDevices;
	protected void setupTray() {		
		// Check the SystemTray is supported
        if (!SystemTray.isSupported()) {
            System.out.println("SystemTray is not supported");
            return;
        }
        
        // Get the system tray
        final SystemTray tray = SystemTray.getSystemTray();
        
        // Create the image
        Image image = new ImageIcon(this.getClass().getResource("/logo.png"), "").getImage();
        Dimension iconSize = tray.getTrayIconSize();
        image = image.getScaledInstance(iconSize.width, iconSize.height, Image.SCALE_SMOOTH);
        
        // Create the actual tray icon and menu
        final PopupMenu popup = new PopupMenu();
        final TrayIcon trayIcon = new TrayIcon(image);
        trayIcon.setPopupMenu(popup);
        trayIcon.setToolTip("Yoke");

        // Show the created trayIcon
        try {
            tray.add(trayIcon);
        } catch (AWTException e) {
            System.out.println("TrayIcon could not be added.");
        }
        
       
        // Create a pop-up menu components
        MenuItem about = new MenuItem("About");
        connectedDevices = new MenuItem("");
        CheckboxMenuItem autoStartup = new CheckboxMenuItem("Auto Startup");
        MenuItem exit = new MenuItem("Exit");
       
        // Add pop-up components
        popup.add(connectedDevices);
        popup.addSeparator();
        popup.add(autoStartup);
        popup.addSeparator();
        popup.add(about);
        popup.add(exit);
        
        // Add interaction handlers
        exit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
        
        // Update the connected devices
        refreshConnectedDevices();
	}
	
	/**
	 * Updates the number of connected devices in the menu
	 */
	protected void refreshConnectedDevices() {
		connectedDevices.setLabel("Connected Devices: " + deviceIDs.size());
	}
}
