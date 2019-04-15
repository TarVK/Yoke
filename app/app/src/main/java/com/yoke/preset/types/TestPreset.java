package com.yoke.preset.types;

import android.content.Context;

import com.example.yoke.R;
import com.yoke.connection.messages.OpenURLCmd;
import com.yoke.connection.messages.PressKeysCmd;
import com.yoke.connection.messages.prompts.RequestFilePath;
import com.yoke.database.types.Profile;
import com.yoke.preset.Preset;
import com.yoke.utils.Keys;

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
        addMacro("Select File", R.drawable.macro_filepath, new RequestFilePath());
        addMacro("Spotify", R.drawable.macro_spotify, new OpenURLCmd("https://www.spotify.com/"));
        addMacro("Wikipedia", R.drawable.macro_wikipedia, new OpenURLCmd("https://www.wikipedia.org/"));
        addMacro("Youtube", R.drawable.macro_youtube, new OpenURLCmd("https://www.youtube.com/"));
        addMacro("Ctrl+C", R.drawable.macro_ctrl_c, new PressKeysCmd(new int[]{Keys.VK_CONTROL, Keys.VK_C}));
        addMacro("Ctrl+V", R.drawable.macro_ctrl_v, new PressKeysCmd(new int[]{Keys.VK_CONTROL, Keys.VK_V}));
    }
}
