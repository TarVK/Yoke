package com.yoke.preset.types;

import android.content.Context;

import com.example.yoke.R;
import com.yoke.connection.messages.OpenURLCmd;
import com.yoke.database.types.Profile;
import com.yoke.preset.Preset;

public class TuePreset extends Preset {
    /**
     * Constructs the preset
     *
     * @param context The context to keep an association with the specific app
     */
    public TuePreset(Context context) {
        super(context);
    }

    @Override
    protected void setupProfile(Profile profile) {
        profile.setName("TU/e");

        addMacro("Canvas", R.drawable.macro_canvas, new OpenURLCmd("https://canvas.tue.nl/"));
        addMacro("MyTue" , R.drawable.macro_mytue, new OpenURLCmd("https://mytue.tue.nl/dashboard/home"));
    }
}
