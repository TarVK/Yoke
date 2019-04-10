package com.yoke.preset.types;

import android.content.Context;

import com.example.yoke.R;
import com.yoke.connection.messages.app.OpenTrackpadCmd;
import com.yoke.connection.messages.prompts.RequestFilePath;
import com.yoke.connection.messages.prompts.RequestKeyPress;
import com.yoke.database.types.Profile;
import com.yoke.preset.Preset;

/**
 * A preset for testing some presets, initialized in the splash activity
 */
public class TestPreset extends Preset {
    /**
     * Creates a media controls preset
     * @param context  The context to keep an association with the specific app
     */
    public TestPreset(Context context) {
        super(context);
    }

    @Override
    protected void setupProfile(Profile profile) {
        profile.setName("Test Profile");

        // TODO: remove this whole preset
        addMacro("Select File", R.drawable.macro_steam, new RequestFilePath());
        addMacro("Select shortcut", R.drawable.macro_spotify, new RequestKeyPress());
        addMacro("Open Trackpad", R.drawable.macro_twitch, new OpenTrackpadCmd());
    }
}
