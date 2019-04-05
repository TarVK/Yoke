package com.yoke.preset.types;

import android.content.Context;

import com.example.yoke.R;
import com.yoke.connection.messages.OpenProgramCmd;
import com.yoke.connection.messages.OpenURLCmd;
import com.yoke.connection.messages.app.OpenProfileCmd;
import com.yoke.connection.messages.app.OpenTrackpadCmd;
import com.yoke.connection.messages.computerCmds.NextTrackCmd;
import com.yoke.connection.messages.computerCmds.PlayPauseCmd;
import com.yoke.connection.messages.computerCmds.PreviousTrackCmd;
import com.yoke.connection.messages.computerCmds.VolumeDownCmd;
import com.yoke.connection.messages.computerCmds.VolumeUpCmd;
import com.yoke.database.types.Profile;
import com.yoke.preset.Preset;

/**
 * A preset for media controls
 */
public class MediaControlsPreset extends Preset {
    // The preset to link to
    protected Preset linkPreset;

    /**
     * Creates a media controls preset
     * @param context  The context to keep an association with the specific app
     * @param linkPreset  The preset that the last layout_button should link to
     */
    public MediaControlsPreset(Context context, Preset linkPreset) {
        super(context);
        this.linkPreset = linkPreset;
    }

    @Override
    protected void setupProfile(Profile profile) {
        profile.setName("Media Controls");

        // TODO: add proper icons, think of a last action
        linkPreset.getID((id) -> {
            addMacro("Volume Up", R.drawable.steam, new VolumeUpCmd());
            addMacro("Volume Down", R.drawable.spotify, new VolumeDownCmd());
            addMacro("Previous Track", R.drawable.twitch, new PreviousTrackCmd());
            addMacro("Next Track", R.drawable.wikipedia, new NextTrackCmd());
            addMacro("Play/Pause", R.drawable.youtube, new PlayPauseCmd());
            addMacro("Select Launch Profile", R.drawable.spotify, new OpenProfileCmd(id));
        });
    }
}
