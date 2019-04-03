package com.yoke.preset.types;

import android.content.Context;

import com.example.yoke.R;
import com.yoke.connection.messages.OpenProgramCmd;
import com.yoke.connection.messages.OpenURLCmd;
import com.yoke.connection.messages.app.OpenTrackpadCmd;
import com.yoke.database.types.Profile;
import com.yoke.preset.Preset;

/**
 * A preset for launching programs
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

        // TODO: fix pornhub image
        addMacro("Steam", R.drawable.steam, new OpenProgramCmd("C:\\Program Files (x86)\\Steam\\Steam.exe"));
        addMacro("Spotify", R.drawable.spotify, new OpenURLCmd("https://www.spotify.com/"));
        addMacro("Twitch", R.drawable.twitch, new OpenURLCmd("https://www.twitch.tv/"));
        addMacro("Wikipedia", R.drawable.wikipedia, new OpenURLCmd("https://www.wikipedia.org/"));
        addMacro("Youtube", R.drawable.youtube, new OpenURLCmd("https://www.youtube.com/"));
        addMacro("Pornhub", R.drawable.spotify, new OpenURLCmd("https://www.pornhub.com/"));
    }
}
