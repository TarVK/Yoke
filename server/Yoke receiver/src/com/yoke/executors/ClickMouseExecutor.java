package com.yoke.executors;

import java.awt.event.InputEvent;

import com.yoke.connection.messages.ClickMouseCmd;

public class ClickMouseExecutor extends RobotExecutor<ClickMouseCmd>{

	@Override
	public void receive(ClickMouseCmd message) {
		// Check the button type
		if (message.button == ClickMouseCmd.SCROLLUP) {
			robot.mouseWheel(1);
		} else if (message.button == ClickMouseCmd.SCROLLDOWN) {
			robot.mouseWheel(-1);
		} else {
			int button = message.button + 1;
			int mask = InputEvent.getMaskForButton(button);
			
			// Press the button if either a press or button down request
			if (message.type == ClickMouseCmd.BUTTONPRESS || 
					message.type == ClickMouseCmd.BUTTONDOWN) {
				robot.mousePress(mask);				
			}
			
			// Release the button if either a press or button up request
			if (message.type == ClickMouseCmd.BUTTONPRESS || 
					message.type == ClickMouseCmd.BUTTONUP) {
				robot.mouseRelease(mask);				
			}
		}
	}

}
