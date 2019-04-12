package com.yoke.preset.types;

import android.content.Context;

import com.example.yoke.R;
import com.yoke.connection.messages.OpenURLCmd;
import com.yoke.connection.messages.computerCmds.VolumeDownCmd;
import com.yoke.connection.messages.computerCmds.VolumeUpCmd;
import com.yoke.database.types.Profile;
import com.yoke.preset.Preset;

public class StreamerPreset extends Preset {
    /**
     * Constructs the preset
     *
     * @param context The context to keep an association with the specific app
     */
    public StreamerPreset(Context context) {
        super(context);
    }

    @Override
    protected void setupProfile(Profile profile) {
        profile.setName("Streamer");

        addMacro("OBS", R.drawable.macro_obs, new OpenURLCmd("https://obsproject.com/"));
        addMacro("Twitch", R.drawable.macro_twitch, new OpenURLCmd("https://www.twitch.tv/"));
        addMacro("Youtube", R.drawable.macro_youtube, new OpenURLCmd("https://www.youtube.com/"));
        addMacro("Volume Down", R.drawable.macro_volum_up, new VolumeDownCmd());
        addMacro("Volume Up", R.drawable.macro_volume_down, new VolumeUpCmd());
    }
}
