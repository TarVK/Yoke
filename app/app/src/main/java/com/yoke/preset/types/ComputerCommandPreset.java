package com.yoke.preset.types;

import android.content.Context;

import com.example.yoke.R;
import com.yoke.connection.messages.app.OpenTrackpadCmd;
import com.yoke.connection.messages.computerCmds.LogOffCmd;
import com.yoke.connection.messages.computerCmds.RestartCmd;
import com.yoke.connection.messages.computerCmds.ShutDownCmd;
import com.yoke.connection.messages.computerCmds.SleepCmd;
import com.yoke.database.types.Profile;
import com.yoke.preset.Preset;

public class ComputerCommandPreset extends Preset {
    /**
     * Constructs the preset
     *
     * @param context The context to keep an association with the specific app
     */
    public ComputerCommandPreset(Context context) {
        super(context);
    }

    @Override
    protected void setupProfile(Profile profile) {
        profile.setName("Computer command");

        addMacro("Shutdown", R.drawable.macro_shutdown, new ShutDownCmd());
        addMacro("Log off", R.drawable.macro_logoff,new LogOffCmd());
        addMacro("Sleep", R.drawable.macro_sleep, new SleepCmd());
        addMacro("Restart", R.drawable.macro_restart, new RestartCmd());
        addMacro("Mouse", R.drawable.macro_mouse, new OpenTrackpadCmd());
                

    }
}
