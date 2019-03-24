package com.yoke.activities.splash;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.yoke.R;
import com.yoke.connection.Connection;
import com.yoke.connection.MessageReceiver;
import com.yoke.connection.client.MultiClientConnection;
import com.yoke.connection.client.types.BluetoothClientConnection;
import com.yoke.connection.messages.connection.Connected;
import com.yoke.database.DataBase;
import com.yoke.database.DataObject;


public class SplashActivity extends AppCompatActivity {
    private GifView gifView;
    Handler h = new Handler();
    protected Connection connection;
    boolean connected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        gifView = (GifView) findViewById(R.id.gif_view);

        this.databaseInit(true, ()->{
            runOnUiThread(new Runnable() {
                public void run() {
                }
            });
        });
    }

    Runnable r = new Runnable() {
        @Override
        public void run() {
            startActivity(new Intent(SplashActivity.this,
                    com.yoke.activities.tutorial.TutorialActivity.class));
            finish();
        }
    };

    //initialize database
    protected void databaseInit (boolean writeData, final DataObject.Callback initialized) {
        DataBase.initialize(this, new DataObject.Callback() {
            public void call() {
                if (true) {
                    connectionInit();
                }
            }
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
                //eventually check for first time
                //if connected message is received, close splash

//                startActivity(new Intent(SplashActivity.this,
//                        com.yoke.activities.tutorial.TutorialActivity.class));
                //h.postDelayed(r, 5000);

            }
        });

        // Initialize the bluetooth connection
        boolean bluetoothEnabled = bluetoothConnection.setup(false);//returns bool whether succeeded
        if (bluetoothEnabled) {
            //proceed if bluetooth is enabled
        } else {
            //create alert dialog if bluetooth not enabled
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Bluetooth is not enabled");
            builder.setMessage("This app will not function correctly without Bluetooth enabled.")
                    .setPositiveButton("open Bluetooth settings", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent intentOpenBluetoothSettings = new Intent();
                            intentOpenBluetoothSettings.setAction(android.provider.Settings.ACTION_BLUETOOTH_SETTINGS);
                            startActivity(intentOpenBluetoothSettings);
                        }
                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                        }
                    });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
    }
}
