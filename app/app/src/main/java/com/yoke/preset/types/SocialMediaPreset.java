package com.yoke.preset.types;

import android.content.Context;

import com.example.yoke.R;
import com.yoke.connection.messages.OpenURLCmd;
import com.yoke.database.types.Profile;
import com.yoke.preset.Preset;

public class SocialMediaPreset extends Preset {
    /**
     * Constructs the preset
     *
     * @param context The context to keep an association with the specific app
     */
    public SocialMediaPreset(Context context) {
        super(context);
    }

    @Override
    protected void setupProfile(Profile profile) {
        profile.setName("Social Media");

        addMacro("Facebook", R.drawable.macro_facebook, new OpenURLCmd("https://www.facebook.com/"));
        addMacro("Reddit", R.drawable.macro_reddit, new OpenURLCmd("https://www.reddit.com/"));
        addMacro("Gmail", R.drawable.macro_gmail,new OpenURLCmd("https://www.gmail.com/"));
        addMacro("Twitter", R.drawable.macro_twitter, new OpenURLCmd("https://www.twitter.com/"));
        addMacro("Whatsapp", R.drawable.macro_whatsapp, new OpenURLCmd("https://web.whatsapp.com/"));
        addMacro("Outlook", R.drawable.macro_outlook, new OpenURLCmd("https://outlook.live.com/"));
    }
}
