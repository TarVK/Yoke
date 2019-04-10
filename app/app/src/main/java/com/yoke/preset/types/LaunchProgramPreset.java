package com.yoke.preset.types;

import android.content.Context;

import com.example.yoke.R;
import com.yoke.connection.messages.OpenProgramCmd;
import com.yoke.connection.messages.OpenURLCmd;
import com.yoke.database.types.Profile;
import com.yoke.preset.Preset;

/**
 * A preset for launching programs, initialized in the splash activity
 */
public class LaunchProgramPreset extends Preset {
    /**
     * Creates a launch program preset
     * @param context  The context to keep an association with the specific app
     */
    public LaunchProgramPreset(Context context) {
        super(context);
    }

    @Override
    protected void setupProfile(Profile profile) {
        profile.setName("Launch");

        addMacro("Steam", R.drawable.macro_steam, new OpenProgramCmd("C:\\Program Files (x86)\\Steam\\Steam.exe"));
        addMacro("Spotify", R.drawable.macro_spotify, new OpenURLCmd("https://www.macro_spotify.com/"));
        addMacro("Twitch", R.drawable.macro_twitch, new OpenURLCmd("https://www.macro_twitch.tv/"));
        addMacro("Wikipedia", R.drawable.macro_wikipedia, new OpenURLCmd("https://www.macro_wikipedia.org/"));
        addMacro("Youtube", R.drawable.macro_youtube, new OpenURLCmd("https://www.macro_youtube.com/"));
        addMacro("Pornhub", R.drawable.macro_pornhub, new OpenURLCmd("https://www.pornhub.com/"));

        // Create any additional default launcher macro's that aren't in any profile:
//        createMacro("Something", R.drawable.macro_pornhub, new OpenURLCmd("https://www.something.com/"));
    }
}
