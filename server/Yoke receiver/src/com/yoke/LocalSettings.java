package com.yoke;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.prefs.Preferences;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * A singleton class intended to store local data
 */
public class LocalSettings {
    // The singleton instance of the settings
    protected static LocalSettings INSTANCE;
    
    /**
     * Retrieves the singleton instance of the settings
     * @return The instance
     */
    public static LocalSettings getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new LocalSettings();
        }
        
        return INSTANCE;
    }
    
    // A reference to the file to store the settings in
    protected File settingsFile = new File(System.getenv("APPDATA") + "/Yoke/settings.txt");
    
    // Stores all of the settings
    protected HashMap<String, String> settings = new HashMap<String, String>();
    
    /**
     * Creates a local settings instance
     */
    public LocalSettings() {
        // Check if a previous settings file exists
        if (settingsFile.exists()) {
            
            // Read the data from the file
            StringBuilder contentBuilder = new StringBuilder();
            try (BufferedReader br = new BufferedReader(new FileReader(settingsFile))){
                
                // Go through all lines
                String line;
                while ((line = br.readLine()) != null){
                    addSetting(line);
                }
            } catch (IOException e){
                e.printStackTrace();
            }
        }
    }
    

    /**
     * Retrieves whether or not yoke should poll what program is selected
     * @return Whether it should poll
     */
    public boolean getPollFocusedProgram() {
        String pollEnabled = settings.get("pollFocused");
        
        return pollEnabled == null || pollEnabled.equals("true");
    }
    
    /**
     * Sets whether or not yoke should poll what program is selected
     * @param value  Whether it should poll
     */
    public void setPollFocusedProgram(boolean value) {
        settings.put("pollFocused", value + "");
    }
    
    /**
     * Retrieves the folder that was last opened to select a file
     */
    public String getFileDirectory() {
        String storedDirectory = settings.get("fileDirectory");
        
        // Default to the program files
        if (storedDirectory == null) {
            storedDirectory = System.getenv("ProgramFiles");
        }
        
        return storedDirectory;
    }
    
    /**
     * Sets the directory that was last opened by the user
     * @param directory  The directory
     */
    public void setFileDirectory(String directory) {
        settings.put("fileDirectory", directory);
    }
    
    /**
     * Reads a setting value and puts it into the settings
     * @param text  The text including the setting name and value
     */
    protected void addSetting(String text) {
        // Get the key and value from the string
        String[] parts = text.split(":");
        
        // Get the setting name
        String name = parts[0];
        
        // Get the value
        String value = String.join(":", Arrays.copyOfRange(parts, 1, parts.length));
        
        // Store the setting
        settings.put(name, value);
    }
    
    /**
     * Reads all of the stored settings into a string
     * @return All settings represented as a string
     */
    protected String readSettings() {
        String output = "";
        for (String setting: settings.keySet()) {
            String value = settings.get(setting);
            output += setting + ":" + value + "\n";
        }
        return output;
    }
    
    /**
     * Saves all changes that have been made
     */
    public void save() {
        // Convert the settings to a string
        String output = readSettings();
         
        try {
            // Make sure the file exists
            if (!settingsFile.exists()) {
                File parent = settingsFile.getParentFile();
                parent.mkdirs();
                settingsFile.createNewFile();                
            }
            
            // Write the data into the file
            BufferedWriter writer = new BufferedWriter(new FileWriter(settingsFile));
            writer.write(output);
            writer.close();            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
