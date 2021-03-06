package com.yoke.connection.messages;

import com.yoke.connection.Message;
import com.yoke.utils.Keys;


/**
 * A command representing a key press
 */
public class PressKeysCmd extends Message {
    // Serialization ID
    private static final long serialVersionUID = -2283998888388309525L;
    
    // The type of key presses that are available
    public static int KEYDOWN = 0;
    public static int KEYUP = 1;
    public static int KEYPRESS = 2;

    // The list of key codes to press
    public int[] keys;
    
    // The type of keypress
    public int type;
    
    /**
     * Creates a press key command
     * @param keys  The key codes that should be pressed (KeyEvent.A, etc)
     */
    public PressKeysCmd(int[] keys) {
        this(keys, KEYPRESS);
    }
    
    /**
     * Creates a press key command
     * @param keys  The key codes that should be pressed (KeyEvent.A, etc)
     * @param type  The type of keypress to do (PressKeysCmd.KEYDOWN, etc)
     */
    public PressKeysCmd(int[] keys, int type) {
        this.keys = keys;
        this.type = type;
    }

    @Override
    public String toString() {
        String out = "Key ";

        // Add all keys
        for (int key: keys) {
            out += Keys.getKeyText(key) + " + ";
        }

        // Remouve the last plus
        out = out.substring(0, out.length() - 3);

        // Add the type
        if (type == KEYDOWN) {
            out += " down";
        } else if (type == KEYUP) {
            out += " up";
        } else if (type == KEYPRESS) {
            out += " press";
        }

        // Return the result
        return out;
    }
}
