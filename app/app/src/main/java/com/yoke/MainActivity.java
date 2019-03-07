package com.yoke;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.example.yoke.R;
import com.yoke.connection.Message;
import com.yoke.connection.MessageReceiver;
import com.yoke.connection.client.BluetoothClientConnection;
import com.yoke.connection.messages.computerCmds.ShutDownCmd;
import com.yoke.connection.messages.computerCmds.SleepCmd;
import com.yoke.connection.messages.connection.SelectDevice;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.bluetoothTest();
    }

    /**
     * Just some test method to test the bluetooth connection with
     */
    protected void bluetoothTest(){
        Context context = this;
        final BluetoothClientConnection connection = new BluetoothClientConnection(context);

        // Add a popup prompt to select a device when the devices are known
        connection.addReceiver(new MessageReceiver<SelectDevice>() {
            public void receive(SelectDevice message) {
                Log.w("DETECT ALSO", "TEST");
                final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Please choose your device");
                builder.setItems(message.names, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String device = message.names[which];
                        connection.selectDevice(device);
                    }
                });
                builder.show();
            }
        });

        connection.addReceiver(new MessageReceiver<SleepCmd>() {
            public void receive(SleepCmd message) {
                Log.w("SLEEP", "SLEEP");
            }
        });

        connection.requestDevice();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                connection.send(new ShutDownCmd());
                connection.send(new SleepCmd());
                connection.send(new ShutDownCmd());
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
}
