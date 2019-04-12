package com.yoke.preset.types;

import android.content.Context;

import com.example.yoke.R;
import com.yoke.connection.messages.computerCmds.NextTrackCmd;
import com.yoke.connection.messages.computerCmds.PlayPauseCmd;
import com.yoke.connection.messages.computerCmds.PreviousTrackCmd;
import com.yoke.connection.messages.computerCmds.VolumeDownCmd;
import com.yoke.connection.messages.computerCmds.VolumeUpCmd;
import com.yoke.database.types.Profile;
import com.yoke.preset.Preset;

/**
 * A preset for media controls, initialized in the splash activity
 */
public class MediaControlsPreset extends Preset {
    // The preset to link to
    protected Preset linkPreset;

    /**
     * Creates a media controls preset
     * @param context  The context to keep an association with the specific app
     */
    public MediaControlsPreset(Context context) {
        super(context);
    }

    @Override
    protected void setupProfile(Profile profile) {
        profile.setName("Media Controls");

        addMacro("Volume Down", R.drawable.macro_volum_up, new VolumeDownCmd());
        addMacro("Volume Up", R.drawable.macro_volume_down, new VolumeUpCmd());
        addMacro("Previous Track", R.drawable.macro_prev_track, new PreviousTrackCmd());
        addMacro("Next Track", R.drawable.macro_next_track, new NextTrackCmd());
        addMacro("Play/Pause", R.drawable.macro_play, new PlayPauseCmd());
    }
}
