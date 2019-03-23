package com.yoke.executors;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import com.yoke.connection.MessageReceiver;
import com.yoke.connection.messages.OpenURLCmd;

/**
 * A message receiver that listens for open url commands, and perform them
 */
public class OpenURLExecutor extends MessageReceiver<OpenURLCmd>{
    // A desktop object to connect to the user's desktop
    Desktop desktop = java.awt.Desktop.getDesktop();
    
    @Override
    public void receive(OpenURLCmd message) throws IOException, URISyntaxException {
        String URL = message.URL;
        
        // Make sure the URL has a valid prefix
        if (!URL.matches("(www\\.|https?\\://).*")) {
            URL = "www." + URL;
        }
        
        desktop.browse(new URI(URL));
    }

}
