package com.yoke.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
//import android.widget.Button;

import com.example.yoke.R;
import com.yoke.activities.splash.SplashActivity;
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

public class MainActivity extends AppCompatActivity {
    protected Connection connection;
    //private Button splashBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //splash button
//        splashBtn = (Button) findViewById(R.id.splBtn);
//        splashBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                openSplash();
//            }
//        });

        this.databaseTest(true, ()->{
            runOnUiThread(new Runnable() {
                public void run() {
                    bluetoothTest();
                }
            });
        });
    }

    protected void handleMessage(){

    }

    /**
     * Just some test method to test the bluetooth connection with
     */
    protected void bluetoothTest(){
        Context context = this;
        if (connection == null) {
            connection = MultiClientConnection.getInstance();

            if (connection == null) {
                final BluetoothClientConnection bluetoothConnection = new BluetoothClientConnection(context);
                MultiClientConnection.initialize(bluetoothConnection);
                connection = MultiClientConnection.getInstance();

                // Initialize the bluetooth connection
                bluetoothConnection.setup();
            }
        }

        // Add a popup prompt to select a device when the devices are known
        connection.addReceiver(new MessageReceiver<SleepCmd>() {
            public void receive(SleepCmd message) {
                Log.w("SLEEP", "SLEEP");
            }
        });

        // Send messages when clicking the button
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.w("Detect", "this");
                connection.send(new OpenURLCmd("youtube.com"));
                CompoundMessage cm = new CompoundMessage();
                cm.add(new PlayPauseCmd(), 0);
                cm.add(new NextTrackCmd(), 2000);
                connection.send(cm);
            }
        });
    }

    /**
     * Just a test for the database
     * @param writeData  Whether to write data to the device, or read data from it
     * @param initialized  A callback that gets called once the database has been initialized
     */
    protected void databaseTest(boolean writeData, final DataObject.Callback initialized) {
        // Initialize the database
        DataBase.initialize(this, new DataObject.Callback() {
            public void call() {
                if (writeData) {
                    // Change some data in the settings
                    Settings settings = Settings.getInstance();
                    settings.setLanguage("orange");

                    // Delete any old data
                    Profile.getAll(profiles -> {
                        for (Profile p: profiles) {
                            p.delete();
                        }
                    });
                    Macro.getAll(macros -> {
                        for (Macro m: macros) {
                            m.delete();
                        }
                    });
                    Button.getAll(buttons -> {
                        for (Button b: buttons) {
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
                } else {
                    // Check the settings data
                    Settings settings = Settings.getInstance();
                    Log.w("DATABASE TEST", "language: "+ settings.getLanguage());

                    // Get all of the profiles
                    Profile.getAll((profiles) -> {
                        Log.w("DATABASE TEST", "profile count: " + profiles.size());

                        for (Profile profile: profiles) {
                            Log.w("DATABASE TEST", "profile name: " + profile.getName());

                            List<Button> buttons = profile.getButtons();
                            Log.w("DATABASE TEST", "button count: " + buttons.size());
                            for (Button button: buttons) {
                                Macro macro = button.getMacro();

                                if (macro==null) {
                                    Log.w("DATABASE TEST", "button has no macro");
                                    continue;
                                }

                                Log.w("DATABASE TEST", "macro name: " + macro.getName());
                                Log.w("DATABASE TEST", "macro text: " + macro.getText());

                                try {
                                    Message m = macro.getAction();
                                    Log.w("DATABASE TEST", "action type: " + m.toString());
                                } catch (IllegalStateException e) {
                                    Log.w("DATABASE TEST", "macro has no action");
                                }
                            }
                        }
                    });

                    /**
                     * The code above uses a lambda expression for an asynchronous callback
                     *
                     * Profile.getAll((profiles) -> {
                     *
                     * }
                     *
                     * is equivalent to
                     *
                     * Profile.getAll(new DataObject.DataCallback<List<Profile>>(){
                     *     public void retrieve(List<Profile> profiles) {
                     *
                     *     }
                     * });
                     */

                }

                if (initialized != null) {
                    initialized.call();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //button for splash
//    public void openSplash() {
//        Intent intent = new Intent(this, SplashActivity.class);
//        startActivity(intent);
//    }
}
