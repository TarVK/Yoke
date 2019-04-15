package com.yoke.activities.macro.tabs;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.widget.EditText;
import android.widget.Toast;

import com.example.yoke.R;
import com.yoke.connection.Connection;
import com.yoke.connection.Message;
import com.yoke.connection.MessageReceiver;
import com.yoke.connection.client.MultiClientConnection;
import com.yoke.connection.messages.ClickMouseCmd;
import com.yoke.connection.messages.OpenProgramCmd;
import com.yoke.connection.messages.OpenURLCmd;
import com.yoke.connection.messages.PressKeysCmd;
import com.yoke.connection.messages.app.OpenProfileCmd;
import com.yoke.connection.messages.app.OpenTrackpadCmd;
import com.yoke.connection.messages.prompts.RequestFilePath;
import com.yoke.connection.messages.prompts.RequestKeyPress;
import com.yoke.connection.messages.prompts.ReturnFilePath;
import com.yoke.connection.messages.prompts.ReturnKeyPress;
import com.yoke.database.types.Profile;
import com.yoke.utils.DataCallback;

public class CommandRequester extends Activity {

    private static final String TAG = "CommandRequester";

    protected Connection connection =
            MultiClientConnection.getInstance(); // Gets the connection

    // A receiver that listens for pc callbacks
    private MessageReceiver receiver;

    private Context context;

    public CommandRequester(Context context) {
        this.context = context;
    }

    /**
     * Starts the prompts for the selection of an action
     * @param callback The callback to trigger once an action has been selected
     */
    protected void requestCommand(DataCallback<Message> callback) {
        // Dispose of old listeners
        removeListeners();

        // Create a new dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.selectMacroMain)
                .setItems(new String[]{
                        context.getString(R.string.selectMacroMainProfile),
                        context.getString(R.string.selectMacroMainMouse),
                        context.getString(R.string.selectMacroMainKeyboard),
                        context.getString(R.string.selectMacroMainProgram),
                        context.getString(R.string.selectMacroMainSite),
                }, (dialog, which) -> {
                    switch (which) {
                        // Profile was pressed
                        case 0:
                            requestProfileCommand(callback);
                            break;
                        // Mouse was pressed
                        case 1:
                            requestMouseCommand(callback);
                            break;
                        // Keyboard was pressed
                        case 2:
                            if (connection.getState() == Connection.CONNECTED) {
                                receiver = new MessageReceiver<ReturnKeyPress>() {
                                    public void receive(ReturnKeyPress keypress) {
                                        runOnUiThread(() -> {
                                            if (keypress.cancelled) {
                                                callback.retrieve(null);
                                            } else {
                                                callback.retrieve(new PressKeysCmd(keypress.keys));
                                            }
                                            removeListeners();
                                        });
                                    }
                                };
                                connection.addReceiver(receiver);
                                connection.send(new RequestKeyPress());
                                Toast.makeText(context,
                                        context.getString(R.string.selectMacroPrompted),
                                        Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(context,
                                        context.getString(R.string.selectMacroConnectionRequired),
                                        Toast.LENGTH_LONG).show();
                                callback.retrieve(null);
                            }
                            break;
                        // Program was pressed
                        case 3:
                            if (connection.getState() == Connection.CONNECTED) {
                                receiver = new MessageReceiver<ReturnFilePath>() {
                                    public void receive(ReturnFilePath filePath) {
                                        runOnUiThread(() -> {
                                            if (filePath.cancelled) {
                                                callback.retrieve(null);
                                            } else {
                                                callback.retrieve(new OpenProgramCmd(filePath.filePath));
                                            }
                                            removeListeners();
                                        });
                                    }
                                };
                                connection.addReceiver(receiver);
                                connection.send(new RequestFilePath());
                                Toast.makeText(context,
                                        context.getString(R.string.selectMacroPrompted),
                                        Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(context,
                                        context.getString(R.string.selectMacroConnectionRequired),
                                        Toast.LENGTH_LONG).show();
                                callback.retrieve(null);
                            }
                            break;
                        // Site was pressed
                        case 4:
                            requestSiteCommand(callback);
                            break;
                        // Idk what happened
                        default:
                            callback.retrieve(null);
                            break;
                    }
                });
        builder.show();
    }

    /**
     * Starts the prompts for the selection of a profile action
     * @param callback The callback to trigger once an action has been selected
     */
    protected void requestProfileCommand(DataCallback<Message> callback) {
        Profile.getAll(this, profiles -> {
            // Obtain the list of profile names
            String[] profileNames = new String[profiles.size()];
            for (int i = 0; i < profiles.size(); i++){
                profileNames[i] = profiles.get(i).getName();
            }

            runOnUiThread(() -> {
                // Create a dialog to choose one of these profiles
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle(R.string.selectMacroProfile)
                        .setItems(profileNames, (dialog, which) -> {
                            Profile p = profiles.get(which);
                            if (p != null) {
                                callback.retrieve(new OpenProfileCmd(p.getID()));
                            }
                        });
                builder.show();
            });
        });
    }

    /**
     * Starts the prompts for the selection of a mouse action
     * @param callback The callback to trigger once an action has been selected
     */
    protected void requestMouseCommand(DataCallback<Message> callback) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.selectMacroMouse)
                .setItems(new String[]{
                        context.getString(R.string.selectMacroMouseOpenTrackpad),
                        context.getString(R.string.selectMacroMouseLeftClick),
                        context.getString(R.string.selectMacroMouseRightClick),
                        context.getString(R.string.selectMacroMouseMiddleClick),
                        context.getString(R.string.selectMacroMouseScrollUp),
                        context.getString(R.string.selectMacroMouseScrollDown),
                }, (dialog, which) -> {
                    switch (which) {
                        // Open trackpad was pressed
                        case 0:
                            callback.retrieve(new OpenTrackpadCmd());
                            break;
                        // Left click was pressed
                        case 1:
                            callback.retrieve(new ClickMouseCmd(ClickMouseCmd.LEFTCLICK));
                            break;
                        // Right click was pressed
                        case 2:
                            callback.retrieve(new ClickMouseCmd(ClickMouseCmd.RIGHTCLICK));
                            break;
                        // Middle click was pressed
                        case 3:
                            callback.retrieve(new ClickMouseCmd(ClickMouseCmd.MIDDLECLICK));
                            break;
                        // Scroll up was pressed
                        case 4:
                            callback.retrieve(new ClickMouseCmd(ClickMouseCmd.SCROLLUP));
                            break;
                        // Scroll down was pressed
                        case 5:
                            callback.retrieve(new ClickMouseCmd(ClickMouseCmd.SCROLLDOWN));
                            break;
                        // Idk what happened
                        default:
                            callback.retrieve(null);
                            break;
                    }
                });
        builder.show();
    }

    /**
     * Starts the prompts for the selection of a open URL action
     * @param callback The callback to trigger once an action has been selected
     */
    protected void requestSiteCommand(DataCallback<Message> callback) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.selectMacroSite);

        final EditText input = new EditText(context);
        builder.setView(input);
        builder.setPositiveButton(R.string.selectMacroSiteAccept,
                (dialog, which) -> {
                    String url = input.getText().toString();
                    callback.retrieve(new OpenURLCmd(url));
                });
        builder.show();
    }

    @Override
    public void onPause() {
        super.onPause();
        removeListeners();
    }

    /**
     * Gets rid of any registered message receiver if present
     */
    protected void removeListeners() {
        if (receiver != null) {
            connection.removeReceiver(receiver);
            receiver = null;
        }
    }
}
