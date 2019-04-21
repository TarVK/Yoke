package com.yoke.activities.splash;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.yoke.R;
import com.yoke.activities.home.HomeActivity;
import com.yoke.activities.tutorial.TutorialActivity;
import com.yoke.connection.Connection;
import com.yoke.connection.Message;
import com.yoke.connection.MessageReceiver;
import com.yoke.connection.client.MultiClientConnection;
import com.yoke.connection.client.types.BluetoothClientConnection;
import com.yoke.connection.messages.ProgramFocused;
import com.yoke.connection.messages.app.AppCmd;
import com.yoke.connection.messages.connection.Connected;
import com.yoke.connection.messages.connection.ConnectionFailed;
import com.yoke.connection.messages.connection.Disconnected;
import com.yoke.database.DataBase;
import com.yoke.database.types.Profile;
import com.yoke.database.types.Settings;
import com.yoke.preset.Preset;
import com.yoke.preset.types.ComputerCommandPreset;
import com.yoke.preset.types.GamerPreset;
import com.yoke.preset.types.KeyboardPreset;
import com.yoke.preset.types.MediaControlsPreset;
import com.yoke.preset.types.SocialMediaPreset;
import com.yoke.preset.types.StreamerPreset;
import com.yoke.preset.types.TestPreset;
import com.yoke.preset.types.TuePreset;
import com.yoke.utils.Callback;


public class SplashActivity extends AppCompatActivity {
    private GifView gifView;
    protected Connection connection;
    boolean connected = false;

    // Keep track of whether we are still waiting for either timer or setup
    boolean timerFinished = false;
    boolean setupFinished = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        gifView = findViewById(R.id.gif_view);

        // Add a 'fake loading' delay to show off the splash
        new Handler().postDelayed(() -> {
            timerFinished = true;
            continueApp();
        }, 2400);
    }

    @Override
    protected void onResume() {
        super.onResume();

        this.databaseInit(true, this::connectionInit);
    }

    //initialize database
    protected void databaseInit (boolean writeData, final Callback initialized) {
        DataBase.getInstance(this, (db) -> {
            Log.w("SPLASH" , "Database initialized");
            createPresets(() -> {
                runOnUiThread(() -> {
                    Log.w("SPLASH" , "Presets initialized");
                    initialized.call();
                });
            });
        });
    }

    /**
     * Initializes bluetooth connection
     */
    protected void connectionInit () {
        //eventually connection from preferences
        //now just starts bluetooth connection
        final BluetoothClientConnection bluetoothConnection =
                new BluetoothClientConnection(SplashActivity.this);
        MultiClientConnection.initialize(bluetoothConnection);
        connection = MultiClientConnection.getInstance(this);

        //receiver in case of successful connection
        connection.addReceiver(new MessageReceiver<Connected>() {
            public void receive(Connected message) {
                //if connected message is received, close splash
                setupFinished = true;
                continueApp();
                Log.w("SPLASH" , "Connection initialized");
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

        /**
         * Checks whether bluetooth is enabled
         */
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


        // Receivers for navigation within the app
        // TODO: move all listener setup to 'GlobalMessageReceiver'
        connection.addReceiver(new MessageReceiver<AppCmd>() {
            public void receive(AppCmd message) {
                forwardGlobalMessage(message);
            }
        }, true);

        // Receiver for when desktop focus changed
        connection.addReceiver(new MessageReceiver<ProgramFocused>() {
            public void receive(ProgramFocused message) {
                forwardGlobalMessage(message);
            }
        });
    }

    /**
     * Exits the splash screen if loading is done
     */
    protected void continueApp() {
        // Check if both 'timers' finished
        if (!setupFinished || !timerFinished) {
            return;
        }

        // If they did, continue to the next activity
        Intent intent;
        if (Settings.getInstance().isFirstLaunch()) {
            intent = new Intent(this, TutorialActivity.class);
        } else {
            intent = new Intent(this, HomeActivity.class);
        }

        // Otherwise go home
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
    protected void createPresets(Callback callback) {
        Profile.getAll(this, (profiles) -> {
            Log.w("SPLASH", "Profiles returned " + profiles.size());
            // Check if any profiles have to be created
            if (profiles.size() != 0) {
                callback.call();
                return;
            }

            // Create new presets
            Preset.onFinish(callback,
//                    new LaunchProgramPreset(this),
                    new StreamerPreset(this),
                    new SocialMediaPreset(this),
                    new GamerPreset(this),
                    new MediaControlsPreset(this),
                    new TuePreset(this),
                    new ComputerCommandPreset(this),
                    new TestPreset(this),
                    new KeyboardPreset(this)


            );
        });
    }
}
