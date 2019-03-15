package com.yoke.executors;

import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Robot;

import com.yoke.connection.messages.MoveMouseCmd;

public class MoveMouseExecutor extends RobotExecutor<MoveMouseCmd>{

	@Override
	public void receive(MoveMouseCmd message) {
		int x = message.x;
		int y = message.y;
		
		// If the movement is supposed to be relative, add the current location
		if (!message.absolute) {
			Point p = MouseInfo.getPointerInfo().getLocation();
			System.out.println(p);
			x += p.getX();
			y += p.getY();
		}
		
		// Move the mouse to 0, 0 and then to the provided position
		robot.mouseMove(0, 0);
		robot.mouseMove(x, y);
		
		// doesn't work a 100% accurately, due to a bug connected to windows 10 screen scaling
	}

}