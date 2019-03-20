package com.yoke;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class LocalSettings {
	// The singleton instance of the settings
	protected LocalSettings INSTANCE;
	
	/**
	 * Retrieves the singleton instance of the settings
	 * @return The instance
	 */
	public LocalSettings getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new LocalSettings();
		}
		
		return INSTANCE;
	}
	
	// The file in which the settings are saved
	protected File settingsFile = new File(System.getenv("APPDATA")+"/Yoke/settings.txt");
	
	public LocalSettings() {
//
//		  BufferedReader br = new BufferedReader(new FileReader(settingsFilee)); 
//		  
//		  String data = "";
//		  String st; 
//		  while ((st = br.readLine()) != null) 
//			  data += st;
//		  } 
	}
	
	
	public void save() {
		
	}
}
