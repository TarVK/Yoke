package com.yoke.preset.types;

import android.content.Context;

import com.example.yoke.R;
import com.yoke.connection.messages.OpenProgramCmd;
import com.yoke.connection.messages.OpenURLCmd;
import com.yoke.database.types.Profile;
import com.yoke.preset.Preset;

public class GamerPreset extends Preset {
    /**
     * Constructs the preset
     *
     * @param context The context to keep an association with the specific app
     */
    public GamerPreset(Context context) {
        super(context);
    }

    @Override
    protected void setupProfile(Profile profile) {
        profile.setName("Gamer");

        addMacro("Steam", R.drawable.macro_steam, new OpenProgramCmd("C:\\Program Files (x86)\\Steam\\Steam.exe"));
        addMacro("Discord", R.drawable.macro_discord, new OpenProgramCmd("C:\\Program Files (x86)\\Discord\\discord.exe"));
        addMacro("Origin", R.drawable.macro_origin, new OpenProgramCmd("C:\\Program Files (x86)\\Origin\\Origin.exe"));
        addMacro("Uplay", R.drawable.macro_uplay, new OpenProgramCmd("C:\\Program Files (x86)\\Uplay\\Uplay.exe"));
        addMacro("BattleNet", R.drawable.macro_battlenet,new OpenProgramCmd("C:\\Program Files (x86)\\BattleNey\\Battlenet.exe"));
        addMacro("GOG", R.drawable.macro_gog, new OpenURLCmd("https://www.gog.com/"));
    }
}
