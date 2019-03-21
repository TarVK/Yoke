package com.yoke.activities.splash;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.yoke.R;
import com.yoke.connection.CompoundMessage;
import com.yoke.connection.Connection;
import com.yoke.connection.Message;
import com.yoke.connection.MessageReceiver;
import com.yoke.connection.client.MultiClientConnection;
import com.yoke.connection.client.types.BluetoothClientConnection;
import com.yoke.connection.messages.OpenURLCmd;
import com.yoke.connection.messages.computerCmds.NextTrackCmd;
import com.yoke.connection.messages.computerCmds.PlayPauseCmd;
import com.yoke.connection.messages.computerCmds.ShutDownCmd;
import com.yoke.connection.messages.computerCmds.SleepCmd;
import com.yoke.database.DataBase;
import com.yoke.database.DataObject;
import com.yoke.database.types.Button;
import com.yoke.database.types.Macro;
import com.yoke.database.types.Profile;
import com.yoke.database.types.Settings;

import java.util.List;


public class SplashActivity extends AppCompatActivity {
    private GifView gifView;
    Handler h = new Handler();
    protected Connection connection;
//    public Database database;
//    public MultiClientConnection conn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        gifView = (GifView) findViewById(R.id.gif_view);
        //database = Database.getInstance();
        //database.initialize(this, ?);   //callback runOnUi
        //create normal connection
        //conn = MultiClientConnection.getInstance(); //no get instance
        //conn.initialize(?); //bluetooth requires setupcall, after all receivers
        //look in bluetooth test
        //add listener and receiver to connection

        if (connection == null) {
            connection = MultiClientConnection.getInstance();

            if (connection == null) {
                final BluetoothClientConnection bluetoothConnection =
                        new BluetoothClientConnection(this);
                MultiClientConnection.initialize(bluetoothConnection);
                connection = MultiClientConnection.getInstance();

                // Initialize the bluetooth connection
                bluetoothConnection.setup();
            }
        }

        connection.addReceiver(new MessageReceiver<SleepCmd>() {
            public void receive(SleepCmd message) {
                Log.w("SLEEP", "SLEEP");
            }
        });


        DataBase.initialize(this, new DataObject.Callback() {
            public void call() {
                if (true) {
                    // Change some data in the settings
                    Settings settings = Settings.getInstance();
                    settings.setLanguage("orange");

                    // Delete any old data
                    Profile.getAll(profiles -> {
                        for (Profile p : profiles) {
                            p.delete();
                        }
                    });
                    Macro.getAll(macros -> {
                        for (Macro m : macros) {
                            m.delete();
                        }
                    });
                    Button.getAll(buttons -> {
                        for (Button b : buttons) {
                            b.delete();
                        }
                    });

                    // Create some profile with a button
                    Profile p = new Profile("Test1");
                    Macro m = new Macro("ShutDown");
                    Button b = new Button(m);
                    if (p.hasSpace()) { // Make sure there is space to add a button
                        p.addButton(b);
                    }

                    // Assign it some data
                    m.setAction(new ShutDownCmd());
                    m.setText("Crap");

                    // Save all of the data
                    settings.save();
                    // The macro has to be saved before saving the profile
                    m.save(() -> {
                        // The button will get saved by saving the profile
                        p.save();
                    });
                }
            }
        });

        h.postDelayed(r, 5000);
    }

    Runnable r = new Runnable() {
        @Override
        public void run() {
            startActivity(new Intent(SplashActivity.this,
                    com.yoke.activities.tutorial.TutorialActivity.class));
            finish();
        }
    };
}
