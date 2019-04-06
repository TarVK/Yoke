package com.yoke.connection.messages.softwareCmds;

import com.yoke.connection.messages.SoftwareCmd;

/**
 * A command representing the toggling of recording with OBS
 */
public class ToggleRecordingCmd extends SoftwareCmd {

    @Override
    public String toString() {
        return "Toggle OBS recording";
    }
}
