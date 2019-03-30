package com.yoke.activities.splash;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import com.example.yoke.R;
import com.yoke.activities.home.HomeActivity;
import com.yoke.connection.Connection;
import com.yoke.connection.Message;
import com.yoke.connection.MessageReceiver;
import com.yoke.connection.client.MultiClientConnection;
import com.yoke.connection.client.types.BluetoothClientConnection;
import com.yoke.connection.messages.ClickMouseCmd;
import com.yoke.connection.messages.MoveMouseCmd;
import com.yoke.connection.messages.OpenProgramCmd;
import com.yoke.connection.messages.OpenURLCmd;
import com.yoke.connection.messages.PressKeysCmd;
import com.yoke.connection.messages.app.AppCmd;
import com.yoke.connection.messages.app.OpenProfileCmd;
import com.yoke.connection.messages.app.OpenTrackpadCmd;
import com.yoke.connection.messages.computerCmds.NextTrackCmd;
import com.yoke.connection.messages.computerCmds.PlayPauseCmd;
import com.yoke.connection.messages.computerCmds.VolumeDownCmd;
import com.yoke.connection.messages.computerCmds.VolumeUpCmd;
import com.yoke.connection.messages.connection.Connected;
import com.yoke.connection.messages.connection.ConnectionFailed;
import com.yoke.connection.messages.connection.Disconnected;
import com.yoke.database.DataBase;
import com.yoke.database.DataObject;
import com.yoke.database.types.Button;
import com.yoke.database.types.Macro;
import com.yoke.database.types.Profile;
import com.yoke.utils.Keys;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SplashActivity extends AppCompatActivity {
    private GifView gifView;
    Handler h = new Handler();
    protected Connection connection;
    boolean connected = false;

    // Keep track of whether we are still waiting for either timer or setup
    boolean timerFinished = false;
    boolean setupFinished = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        gifView = (GifView) findViewById(R.id.gif_view);

        this.databaseInit(true, () -> {
            connectionInit();
        });

        // Add a 'fake loading' delay to show off the splash
        new Handler().postDelayed(() -> {
            timerFinished = true;
            continueApp();
        }, 2400);
    }

    //initialize database
    protected void databaseInit (boolean writeData, final DataObject.Callback initialized) {
        DataBase.initialize(this, () -> {
            createPresets(() -> {
                runOnUiThread(() -> {
                    initialized.call();
                });
            });
        });
    }

    protected void connectionInit () {
        //eventually connection from preferences
        //now just starts bluetooth connection
        final BluetoothClientConnection bluetoothConnection =
                new BluetoothClientConnection(SplashActivity.this);
        MultiClientConnection.initialize(bluetoothConnection);
        connection = MultiClientConnection.getInstance();

        //add receiver to connection
        connection.addReceiver(new MessageReceiver<Connected>() {
            public void receive(Connected message) {
            //if connected message is received, close splash
            setupFinished = true;
            continueApp();
            }
        });

        //receiver in case of failed connection
        Context context = this;
        connection.addReceiver(new MessageReceiver<ConnectionFailed>() {
            public void receive(ConnectionFailed message) {
                forwardGlobalMessage(message);
            }
        });

        //receiver in case of disconnection
        connection.addReceiver(new MessageReceiver<Disconnected>() {
            public void receive(Disconnected message) {
                forwardGlobalMessage(message);
            }
        });


        // Initialize the bluetooth connection
        boolean bluetoothEnabled = bluetoothConnection.setup(false);//returns bool whether succeeded
        if (bluetoothEnabled) {
            //proceed if bluetooth is enabled
        } else {
            //create alert dialog if bluetooth is not enabled
            final AlertDialog.Builder builder3 = new AlertDialog.Builder(this);
            builder3.setTitle("Bluetooth is not enabled");
            builder3.setMessage("This app will not function correctly without Bluetooth enabled.")
                    .setPositiveButton("open Bluetooth settings",
                            (dialog, id) -> {
                                Intent intentOpenBluetoothSettings = new Intent();
                                intentOpenBluetoothSettings.setAction
                                        (android.provider.Settings.ACTION_BLUETOOTH_SETTINGS);
                                startActivity(intentOpenBluetoothSettings);
                            })
                    .setNegativeButton("cancel", (dialog, id) -> dialog.dismiss());
            AlertDialog alertDialog = builder3.create();
            alertDialog.show();
        }


        //receivers for navigation within the app
        // TODO: move all listener setup to 'GlobalMessageReceiver'
        connection.addReceiver(new MessageReceiver<AppCmd>() {
            public void receive(AppCmd message) {
                forwardGlobalMessage(message);
            }
        }, true);
    }

    /**
     * Exits the splash screen if loading is done
     */
    protected void continueApp() {
        // Check if both 'timers' finished
        if (!setupFinished || !timerFinished) {
            return;
        }

        // If they did, continue to the home activity
        Intent intent = new Intent(SplashActivity.this,
                HomeActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        finish();
    }

    /**
     * Forwards a message to the globalMessageReceiver
     * @param message  The message to forward
     */
    protected void forwardGlobalMessage(Message message) {
        Intent intent = new Intent(this, GlobalMessageReceiver.class);
        intent.setAction("connectionStateChanged");
        intent.putExtra("message", message);
        intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
        sendBroadcast(intent);
    }

    /**
     * Checks whether presets have to be created, and if so, does so
     * @param callback  The callback that should be triggered once the preset creating finished
     */
    protected void createPresets(DataObject.Callback callback) {
        Profile.getAll(new DataObject.DataCallback<List<Profile>>(){
            public void retrieve(List<Profile> profiles) {
                // Check if any profiles have to be created
                if (profiles.size() != 0) {
                    callback.call();
                    return;
                }

                // Make sure the required macros are available
                createMacro("1", R.drawable.spotify, new PlayPauseCmd(), macroCallback);
                createMacro("2", R.drawable.steam,
                        new OpenProgramCmd("C:\\Program Files (x86)\\Steam\\Steam.exe"),
                        macroCallback);
                createMacro("3", R.drawable.twitch,
                        new OpenURLCmd("https://www.twitch.tv/"), macroCallback);
                createMacro("4", R.drawable.wikipedia,
                        new OpenURLCmd("https://www.wikipedia.org/"), macroCallback);
                createMacro("5", R.drawable.youtube, // ooops
                        new OpenURLCmd("https://www.youtube.com"), macroCallback);
                createMacro("6", R.drawable.spotify,
                        new OpenTrackpadCmd(), macroCallback);

                createMacro("7", R.drawable.spotify, new VolumeUpCmd(), macroCallback);
                createMacro("8", R.drawable.steam, new VolumeDownCmd(), macroCallback);
                createMacro("9", R.drawable.twitch, new PressKeysCmd(new int[]{
                    Keys.VK_CONTROL, Keys.VK_A
                }), macroCallback);
                createMacro("10", R.drawable.wikipedia,
                        new MoveMouseCmd(100, 100), macroCallback);
                createMacro("11", R.drawable.youtube,
                        new ClickMouseCmd(ClickMouseCmd.LEFTCLICK), macroCallback);
                createMacro("12", R.drawable.spotify,
                        new OpenProfileCmd(0), macroCallback);


            }

            // The number of profiles that remain to be saved
            protected int remainingProfiles = 2;

            // A callback that checks whether all profiles have been saved
            protected DataObject.Callback profileSaveCallback = () -> {
                // Check if both profiles finished saving
                if (--remainingProfiles == 0) {
                    callback.call();
                }
            };

            // Track the macros that have been retrieved
            protected HashMap<String, Macro> macros = new HashMap<>();

            // A callback that continues setting up the profile,
            // once all macro callbacks have been received
            protected DataObject.DataCallback<Macro> macroCallback = (macro) -> {
                macros.put(macro.getName(), macro);

                // Check if all of the macros have been created/retrieved
                if (macros.size() == 12) {
                    // Create the profiles
                    Profile profile1 = new Profile("test1");
                    Profile profile2 = new Profile("test2");

                    // Save profile 2 such that it gets an ID
                    profile1.save(() -> {
                        // Fill the profiles with their buttons TODO; think of proper names
                        profile1.addButton(new Button(macros.get("1")));
                        profile1.addButton(new Button(macros.get("2")));
                        profile1.addButton(new Button(macros.get("3")));
                        profile1.addButton(new Button(macros.get("4")));
                        profile1.addButton(new Button(macros.get("5")));
                        profile1.addButton(new Button(macros.get("6")));

                        profile2.addButton(new Button(macros.get("7")));
                        profile2.addButton(new Button(macros.get("8")));
                        profile2.addButton(new Button(macros.get("9")));
                        profile2.addButton(new Button(macros.get("10")));
                        profile2.addButton(new Button(macros.get("11")));

                        // Assign the correct profileID to the openProfile macro
                        Macro openProfileMacro = macros.get("12");
                        openProfileMacro.setAction(new OpenProfileCmd(profile1.getID()));
                        profile2.addButton(new Button(openProfileMacro));

                        // Save the openProfileMacro
                        openProfileMacro.save(() -> {
                            // Save the profiles
                            profile1.save(profileSaveCallback);
                            profile2.save(profileSaveCallback);
                        });
                    });

                }
            };
        });

    }

    /**
     * Creates a macro using the specified data, unless a macro with the given name already exists
     * @param name  The name of the macro
     * @param imageResourceID  The id of the image resource for the button
     * @param action  The action to perform once the button is pressed
     * @param callback  The callback that should be triggered once the macro has been created
     */
    protected void createMacro(String name, int imageResourceID,
                               Message action, DataObject.DataCallback<Macro> callback) {
        // Check if the macro already exists
        Macro.getByName(name, (macro) -> {
            if (macro != null) {
                callback.retrieve(macro);
                return;
            }

            // If the macro doesn't exist yet, create it
            final Macro newMacro = new Macro(name);
            newMacro.setAction(action);
            newMacro.setBackgroundImage(BitmapFactory.decodeResource(this.getResources(), imageResourceID));

            // Save the newly created macro
            newMacro.save(() -> callback.retrieve(newMacro));
        });
    }
}
