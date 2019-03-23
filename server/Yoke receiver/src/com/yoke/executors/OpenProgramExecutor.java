package com.yoke.executors;

import java.io.File;

import com.yoke.Tray;
import com.yoke.connection.messages.OpenProgramCmd;

/**
 * A message receiver that listens for open program commands, and perform them
 */
public class OpenProgramExecutor extends CmdPromptExecutor<OpenProgramCmd>{

    @Override
    public void receive(OpenProgramCmd message) {
        // Check if the path is present on the user's computer
        File file = new File(message.path);
        if (!file.exists()) {
            Tray.getInstance().showMessage("The file \"" + message.path + "\"could not be found on this computer.");
            return;
        }
        
        // If it does, simply open it using the terminal
        execAdvancedCmd(message.path);
    }

}
