package com.yoke.executors;

import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Robot;

import com.sun.jna.Native;
import com.sun.jna.platform.win32.BaseTSD.ULONG_PTR;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef.DWORD;
import com.sun.jna.platform.win32.WinDef.LONG;
import com.sun.jna.platform.win32.WinUser.INPUT;
import com.sun.jna.win32.StdCallLibrary;
import com.yoke.connection.messages.MoveMouseCmd;

/**
 * A message receiver that listens for mouse move commands, and perform them
 */
public class MoveMouseExecutor extends RobotExecutor<MoveMouseCmd>{

    @Override
    public void receive(MoveMouseCmd message) {
        int x = message.x;
        int y = message.y;
        
        // If the movement is supposed to be relative, add the current location
        if (!message.absolute) {
            Point p = MouseInfo.getPointerInfo().getLocation();
            
            x += (int) p.getX();
            y += (int) p.getY();
        }
        
        // Move the mouse to 0, 0 and then to the provided position
        MouseUtils.move(x, y);
    }
}

/**
 * Class to replace robot.mouseMove which seems to have issues on windows 10
 * src: https://stackoverflow.com/questions/8408435/how-to-move-or-do-anything-with-the-mouse/8460961
 */
class MouseUtils {
    // Create a jna interface and instance
    public interface User32 extends StdCallLibrary {
        public static final long MOUSEEVENTF_MOVE = 0x0001L; 
        public static final long MOUSEEVENTF_VIRTUALDESK = 0x4000L; 
        public static final long MOUSEEVENTF_ABSOLUTE = 0x8000L;
        
        User32 INSTANCE = (User32) Native.loadLibrary("user32", User32.class);
        DWORD SendInput(DWORD dWord, INPUT[] input, int cbSize);
          
        public static final int SM_CXSCREEN = 0x0;
        public static final int SM_CYSCREEN = 0x1;
        int GetSystemMetrics(int index);
    }

    /**
     * Moves the mouse to the specified location
     * @param x  The x coordinate of the location
     * @param y  The y coordinate of the location
     */
    public static void move(int x, int y) {
        INPUT input = new INPUT();
        input.type = new DWORD(INPUT.INPUT_MOUSE);
        input.input.setType("mi");
        input.input.mi.dx = new LONG(x * 65536 / User32.INSTANCE.GetSystemMetrics(User32.SM_CXSCREEN));
        input.input.mi.dy = new LONG(y * 65536 / User32.INSTANCE.GetSystemMetrics(User32.SM_CYSCREEN));
        input.input.mi.mouseData = new DWORD(0);
        input.input.mi.dwFlags = new DWORD(User32.MOUSEEVENTF_MOVE
            | User32.MOUSEEVENTF_VIRTUALDESK | User32.MOUSEEVENTF_ABSOLUTE);
        input.input.mi.time = new DWORD(0);

        INPUT[] inArray = {input};

        int cbSize = input.size(); // mouse input struct size
        DWORD nInputs = new DWORD(1); // number of inputs
        DWORD result = User32.INSTANCE.SendInput(nInputs , inArray, cbSize);
   }
}