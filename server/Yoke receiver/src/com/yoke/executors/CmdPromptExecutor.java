package com.yoke.executors;

import java.io.IOException;

import com.yoke.connection.Message;
import com.yoke.connection.MessageReceiver;

/**
 * An abstract message receiver that provides methods to send terminal commands
 */
public abstract class CmdPromptExecutor<T extends Message> extends MessageReceiver<T>{
    /**
     * Executes a terminal command
     * @param command  The command to execute
     */
    protected void execCmd(String command) {
        // Simple code to execute cmd line arguments
        try {
            Runtime.getRuntime().exec(
                    new String[] { "bash", "-c", command});
        } catch (IOException e) {
            e.printStackTrace();
            // TODO: handle errors
        }
    }
    
    /**
     * Executes a terminal command
     * @param cmd  The command to execute, split into arguments
     */
    protected void execAdvancedCmd(String... cmd) {
        ProcessBuilder builder = new ProcessBuilder(cmd);
        builder.redirectError(ProcessBuilder.Redirect.INHERIT);
        try {
            builder.start();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
