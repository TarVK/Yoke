package com.yoke.executors;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Robot;
import java.awt.Toolkit;

import com.yoke.connection.Message;
import com.yoke.connection.MessageReceiver;

public abstract class RobotExecutor<T extends Message> extends MessageReceiver<T> {
	protected Robot robot;
	public RobotExecutor() {
		try {
			robot = new Robot();
		} catch (AWTException e) {
			e.printStackTrace();
		}
	}
}