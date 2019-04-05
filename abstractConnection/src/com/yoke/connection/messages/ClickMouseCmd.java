package com.yoke.connection.messages;

import com.yoke.connection.Message;

/**
 * A command representing mouse clicks
 */
public class ClickMouseCmd extends Message {
    // Serialization ID
    private static final long serialVersionUID = -2463696863023873571L;
    
    // All the available mouse buttons
    public static int LEFTCLICK = 0;
    public static int RIGHTCLICK = 1;
    public static int MIDDLECLICK = 2;
    public static int SCROLLUP = 3;
    public static int SCROLLDOWN = 4;

    // The type of key presses that are available
    public static byte BUTTONDOWN = 0;
    public static byte BUTTONUP = 1;
    public static byte BUTTONPRESS = 2;
    
    // The mouse press to perform
    public int button;

    // The type of layout_button press
    public byte type;
    
    /**
     * Creates a mouse click command
     * @param button  The mouse layout_button that should be clicked (ClickMouseCmd.LEFTCLICK, etc)
     */
    public ClickMouseCmd(int button) {
        this(button, BUTTONPRESS);
    }
    
    /**
     * Creates a mouse click command
     * @param button  The mouse layout_button that should be clicked (ClickMouseCmd.LEFTCLICK, etc)
     * @param type  The type of layout_button press (ClickMouseCmd.BUTTONDOWN, etc)
     */
    public ClickMouseCmd(int button, byte type) {
        this.button = button;
        this.type = type;
    }
}
