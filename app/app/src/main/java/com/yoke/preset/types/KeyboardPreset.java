package com.yoke.preset.types;

import android.content.Context;

import com.example.yoke.R;
import com.yoke.connection.messages.PressKeysCmd;
import com.yoke.database.types.Profile;
import com.yoke.preset.Preset;
import com.yoke.utils.Keys;

/**
 * A preset for testing some presets, initialized in the splash activity
 */
public class KeyboardPreset extends Preset {
    /**
     * Creates a media controls preset
     * @param context  The context to keep an association with the specific app
     */
    public KeyboardPreset(Context context) {
        super(context);
    }

    @Override
    protected void setupProfile(Profile profile) {
        profile.setName("Keyboard Profile");

        // TODO: remove this whole preset
        addMacro("Alt+left", R.drawable.macro_alt_left, new PressKeysCmd(new int[]{Keys.VK_ALT, Keys.VK_LEFT}));
        addMacro("Alt+right", R.drawable.macro_alt_right, new PressKeysCmd(new int[]{Keys.VK_ALT, Keys.VK_RIGHT}));
        addMacro("Ctrl+T", R.drawable.macro_ctrl_t, new PressKeysCmd(new int[]{Keys.VK_CONTROL, Keys.VK_T}));
        addMacro("Ctrl+Shift+T", R.drawable.macro_ctrl_shift_t, new PressKeysCmd(new int[]{Keys.VK_CONTROL, Keys.VK_SHIFT, Keys.VK_T}));
        addMacro("Ctrl+W", R.drawable.macro_ctrl_w, new PressKeysCmd(new int[]{Keys.VK_CONTROL, Keys.VK_W}));
    }
}
