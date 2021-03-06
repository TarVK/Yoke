package com.yoke.executors;

import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.List;

import com.yoke.connection.messages.PressKeysCmd;


/**
 * A message receiver that listens for key press commands, and perform them
 */
public class PressKeysExecutor extends RobotExecutor<PressKeysCmd>{
    // All the keyboard modifiers
    static List<Integer> modifierKeys = Arrays.asList(new Integer[] {
            KeyEvent.VK_CONTROL, 
            KeyEvent.VK_SHIFT, 
            KeyEvent.VK_ALT, 
            KeyEvent.VK_WINDOWS 
        });
    
    
    @Override
    public void receive(PressKeysCmd message) {
        // Check if the key should be pressed
        if (message.type == PressKeysCmd.KEYPRESS || message.type == PressKeysCmd.KEYDOWN) {
            // Activate modifier keys
            for (int key: message.keys) {
                if (modifierKeys.contains(key)) {
                    robot.keyPress(key);
                }
            }
            // Activate normal keys
            for (int key: message.keys) {
                if (!modifierKeys.contains(key)) {
                    robot.keyPress(key);
                }
            }
        }
        
        // Check if the key should be depressed
        if (message.type == PressKeysCmd.KEYPRESS || message.type == PressKeysCmd.KEYUP) {
            // Deactivate normal keys
            for (int i = message.keys.length - 1; i >= 0; i--) {
                int key = message.keys[i];
                if (!modifierKeys.contains(key)) {
                    robot.keyRelease(key);
                }
            }
            // Deactivate modifier keys
            for (int i = message.keys.length - 1; i >= 0; i--) {
                int key = message.keys[i];
                if (modifierKeys.contains(key)) {
                    robot.keyRelease(key);
                }
            }
        }
    }

}
