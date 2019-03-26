package com.yoke.activities.splash;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.ContextThemeWrapper;
import android.util.Log;
import android.widget.Toast;

import com.example.yoke.R;
import com.yoke.connection.Message;
import com.yoke.connection.messages.connection.ConnectionFailed;
import com.yoke.connection.messages.connection.Disconnected;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

public class ConnectionEventReceiver extends BroadcastReceiver {
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