package com.yoke.executors.computerCmds;

import com.sun.jna.Native;
import com.sun.jna.platform.win32.User32;
import com.yoke.connection.Message;
import com.yoke.connection.MessageReceiver;

public abstract class VirtualKeyExecutor<T extends Message> extends MessageReceiver<T>{
	public interface VKeys extends User32 {
		VKeys INSTANCE = (VKeys) Native.load("user32.dll", VKeys.class);
		
		// The method to send virtual keys
		public void keybd_event(byte bVK, byte bScan, int dwFlags, int dwExtraInfo);
	}
	
	protected void sendKey(int i) {
		VKeys.INSTANCE.keybd_event((byte) i, (byte) 0, 0, 0);
	}
}
