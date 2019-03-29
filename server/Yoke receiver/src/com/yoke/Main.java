package com.yoke;

import java.awt.AWTException;
import java.awt.CheckboxMenuItem;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.Frame;
import java.awt.Image;
import java.awt.Menu;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import com.sun.jna.Native;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WTypes.LPWSTR;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.yoke.connection.Connection;
import com.yoke.connection.MessageReceiver;
import com.yoke.connection.messages.ProgramFocused;
import com.yoke.connection.messages.computerCmds.ShutDownCmd;
import com.yoke.connection.messages.computerCmds.SleepCmd;
import com.yoke.connection.messages.connection.Connected;
import com.yoke.connection.messages.connection.ConnectionFailed;
import com.yoke.connection.messages.connection.Disconnected;
import com.yoke.connection.messages.prompts.RequestFilePath;
import com.yoke.connection.messages.prompts.RequestKeyPress;
import com.yoke.connection.messages.prompts.ReturnFilePath;
import com.yoke.connection.messages.prompts.ReturnKeyPress;
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
import com.yoke.executors.computerCmds.VirtualKeyExecutor;
import com.yoke.executors.computerCmds.VolumeDownExecutor;
import com.yoke.executors.computerCmds.VolumeUpExecutor;

/**
 * The main class for the application
 */
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
    
    /*
     * The constructor method
     */
    public Main() {        
        // Setup the tray
        tray = Tray.getInstance();
        tray.updateConnectedDevices(deviceIDs.size());
        
        // Setup the connection
        BluetoothServerConnection bluetooth = new BluetoothServerConnection();
        connection = new MultiServerConnection(bluetooth);
        
        // Setup the remaining behavior
        setupConnectionListeners();
        setupExecutors();
        setupProgramPoll();        
        setupPrompts();
    }
    
    
    /**
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
                deviceIDs.remove((Object) cmd.deviceID);        
                
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
    
    /**
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
     * Sets up a function to poll for what program is selected
     */
    protected void setupProgramPoll() {
        // Get a reference to the settings
        final LocalSettings settings = LocalSettings.getInstance();
        
        // use the user32 dll
        U32 user32 = U32.INSTANCE;
        
        
        // Create a thread to perform the polling in
        Thread thread = new Thread() {
            // The name of the focused window
            protected String focusedName = "";
            
            @Override
            public void run() {
                try {
                    while(true) {
                        // Have a little timeout to reduce computation efforts
                        sleep(500);
                        
                        // Check if the poll should be performed
                        if (settings.getPollFocusedProgram()) {
                            // Get the window text name
                            HWND h = user32.GetForegroundWindow();
                            byte[] windowText = new byte[512];
                            U32.INSTANCE.GetWindowTextA(h, windowText, 512);
                            String name = Native.toString(windowText).trim();
                            
                            // Check if this name is different from the previous name
                            if (!focusedName.equals(name)) {
                                focusedName = name;
                                
                                // Send the program focus message
                                connection.send(new ProgramFocused(name));
                            }
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();
    }
    
    /**
     * An interface containing an instance of a class implementing the interface,
     * that allows for retrieval of a program name
     */
    protected interface U32 extends User32 {
        U32 INSTANCE = (U32) Native.load("user32.dll",U32.class);
        int GetWindowTextA(HWND  hWnd, byte[] lpString, int   nMaxCount);
    }
    
    /**
     * Set up the receivers for desktop input prompts 
     */
    protected void setupPrompts() {
        connection.addReceiver(new MessageReceiver<RequestFilePath>() {
            public void receive(RequestFilePath message) {
                // Get the icon for the chooser
                List<Image> icons = Tray.getInstance().getIcons();
                
                // Create the file chooser
                LocalSettings settings = LocalSettings.getInstance();
                FileDialog fileChooser = new FileDialog((Frame) null);
                fileChooser.setTitle("Please select a file");
                fileChooser.setDirectory(settings.getFileDirectory());
                fileChooser.setVisible(true);
                fileChooser.setIconImages(icons);
                
                // Retrieve the file that was chosen
                String fileName = fileChooser.getFile();
                if (fileName == null) {
                    // Return a cancelled file path message
                    connection.send(new ReturnFilePath());
                    return;
                }
                File file = new File(fileChooser.getDirectory() + "/" + fileName);
                
                // store the location of that file
                settings.setFileDirectory(file.getParent());
                settings.save();
                
                // Send the response message
                connection.send(new ReturnFilePath(file.getAbsolutePath()));
                System.out.println(file.getAbsolutePath());
            } 
        });
        
        connection.addReceiver(new MessageReceiver<RequestKeyPress>() {
            public void receive(RequestKeyPress message) {
                // Get the icon for the chooser
                List<Image> icons = Tray.getInstance().getIcons();
                
                // Create the chooser
                KeyPressChooser keyChooser = new KeyPressChooser();
                keyChooser.setIconImages(icons);
                keyChooser.setTitle("Please select a key combination");
                List<Integer> value = keyChooser.showDialog();
                
                // Check what value was returned, and send a return message
                if (value == null) {
                    connection.send(new ReturnKeyPress());
                } else {
                    int[] array = value.stream().mapToInt(i -> i).toArray();
                    connection.send(new ReturnKeyPress(array));
                }
            } 
        });
    }
}
