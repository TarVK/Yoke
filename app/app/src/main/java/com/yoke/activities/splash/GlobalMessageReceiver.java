package com.yoke.activities.splash;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import com.yoke.activities.mouse.MouseActivity;
import com.yoke.activities.profile.ProfileActivity;
import com.yoke.connection.Message;
import com.yoke.connection.messages.ProgramFocused;
import com.yoke.connection.messages.app.OpenProfileCmd;
import com.yoke.connection.messages.app.OpenTrackpadCmd;
import com.yoke.connection.messages.connection.ConnectionFailed;
import com.yoke.connection.messages.connection.Disconnected;
import com.yoke.database.types.Profile;
import com.yoke.utils.DataCallback;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

public class GlobalMessageReceiver extends BroadcastReceiver {
    private static final String TAG = "MyBroadcastReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {
        Message message = (Message) intent.getSerializableExtra("message");

        Activity activity = getActivity();
        if (activity == null) {
            return;
        }

        // If the event was a connection failed event
        if (message instanceof ConnectionFailed) {
            final AlertDialog.Builder builder1 =
                    new AlertDialog.Builder(activity);
            builder1.setTitle("Connection Failed");
            builder1.setMessage("Your phone was not able to connect to your laptop/pc. \n" +
                    "Close the app and try again.")
                    .setPositiveButton("ok", (dialog, id) -> dialog.dismiss());
            AlertDialog alertDialog = builder1.create();
            alertDialog.show();
        }

        // If the event was an disconnect event
        else if (message instanceof Disconnected) {
            final AlertDialog.Builder builder2 =
                    new AlertDialog.Builder(activity);
            builder2.setTitle("Your phone is disconnected");
            builder2.setMessage("Reconnect your phone to your laptop/pc.")
                    .setPositiveButton("open Bluetooth settings",
                            (dialog, id) -> {
                                Intent intentOpenBluetoothSettings = new Intent();
                                intentOpenBluetoothSettings.setAction
                                        (android.provider.Settings.ACTION_BLUETOOTH_SETTINGS);
                                context.startActivity(intentOpenBluetoothSettings);
                            })
                    .setNegativeButton("cancel", (dialog, id) -> dialog.dismiss());
            AlertDialog alertDialog = builder2.create();
            alertDialog.show();
        }

        // Manage programs focusing
        else if (message instanceof ProgramFocused) {
            // Only potentially switch if we are currently in a profile
            if (activity != null && !(activity instanceof ProfileActivity)) {
                return;
            }

            // Get the program name
            String name = ((ProgramFocused) message).programName;

            // Check if a profile exists with this associated program
            Profile.getAssociated(name, (profile) -> {
                if (profile != null) {
                    // Make sure the profile isn't already opened
                    if (((ProfileActivity) activity).profile.getID() == profile.getID()) {
                        return;
                    }

                    // Switch to profile
                    Intent profileIntent = new Intent(context, ProfileActivity.class);
                    profileIntent.putExtra("profile id", profile.getID());
                    context.startActivity(profileIntent);

                    // Make sure to not store the history
                    if (activity != null) {
                        activity.finish();
                    }
                }
            });
        }

        // Also manage app messages\

        // Open trackpad command
        else if (message instanceof OpenTrackpadCmd) {
            context.startActivity(new Intent(context, MouseActivity.class));
        }
        // Switch profile command
        else if (message instanceof OpenProfileCmd) {
            OpenProfileCmd cmd = (OpenProfileCmd) message;

            // Check if the profile exists first
            Profile.getByID(cmd.profileID, (profile) -> {
                if (profile != null) {
                    // Switch to profile
                    Intent profileIntent = new Intent(context, ProfileActivity.class);
                    profileIntent.putExtra("profile id", cmd.profileID);
                    context.startActivity(profileIntent);

                    // Make sure to not store the history
                    if (activity != null) {
                        activity.finish();
                    }
                } else {
                    // If no such profile exists, try to send an error notification
                    if (activity != null) {
                        activity.runOnUiThread(() -> {
                            Toast.makeText(context, "The profile that this action should link to, no longer exists.",
                                    Toast.LENGTH_LONG).show();
                        });
                    }
                }
            });
        }
    }

    /**
     * Retrieves the running activity
     * @returns The running activity
     * Source: https://stackoverflow.com/a/28423385/8521718
     */
    public static Activity getActivity() {
        try {
            Class activityThreadClass = Class.forName("android.app.ActivityThread");
            Object activityThread = activityThreadClass.getMethod("currentActivityThread")
                    .invoke(null);
            Field activitiesField = activityThreadClass.getDeclaredField("mActivities");
            activitiesField.setAccessible(true);

            Map<Object, Object> activities = (Map<Object, Object>) activitiesField.get(activityThread);
            if (activities == null)
                return null;

            for (Object activityRecord : activities.values()) {
                Class activityRecordClass = activityRecord.getClass();
                Field pausedField = activityRecordClass.getDeclaredField("paused");
                pausedField.setAccessible(true);
                if (!pausedField.getBoolean(activityRecord)) {
                    Field activityField = activityRecordClass.getDeclaredField("activity");
                    activityField.setAccessible(true);
                    Activity activity = (Activity) activityField.get(activityRecord);
                    return activity;
                }
            }

        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException | NoSuchFieldException e) {
            e.printStackTrace();
        }
        return null;
    }
}