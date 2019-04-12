package com.yoke.activities.macro.tabs;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yoke.R;
import com.yoke.activities.macro.MacroActivity;
import com.yoke.connection.ComposedMessage;
import com.yoke.connection.CompoundMessage;
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
import com.yoke.database.types.Macro;
import com.yoke.database.types.Profile;
import com.yoke.utils.Callback;
import com.yoke.utils.DataCallback;

import java.util.Iterator;


public class MacroSequence extends Fragment {

    private static final String TAG = "MacroSequence";

    protected Connection connection =
            MultiClientConnection.getInstance(); // Gets the connection

    private int delayAmount = 0;
    private int repeatAmount = 1;

    private SeekBar seekbarDelay;
    private SeekBar seekbarRepeat;

    private TextView delayObserver;
    private TextView repeatObserver;

    // A receiver that listens for pc callbacks
    private MessageReceiver receiver;

    // A reference to the macro activity
    private MacroActivity actibity;

    private TextView editAction;



    public MacroSequence() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        actibity = (MacroActivity) getActivity();
        View view = inflater.inflate(R.layout.activity_macro_sequence,
                container, false);

        editAction = view.findViewById(R.id.editAction);
        seekbarDelay = view.findViewById(R.id.seekBarDelay);
        seekbarRepeat = view.findViewById(R.id.seekbarRepeat);

        delayObserver = view.findViewById(R.id.delayObserver);
        repeatObserver = view.findViewById(R.id.repeatObserver);
        editAction.setText("Undefined");

        actibity.loadMacro(() -> {
            // Initialize the values
            delayAmount = getCurrentDelay();
            delayObserver.setText(delayAmount + "s");
            seekbarDelay.setProgress(delayAmount);

            repeatAmount = getCurrentRepeatCount();
            repeatObserver.setText(repeatAmount + "x");
            seekbarRepeat.setProgress(repeatAmount);

            // Initialize the action
            updateCompoundAction(null);

            // Add all listeners for editing

            editAction.setOnClickListener(v -> {
                requestCommand(action -> {
                    if (action != null) {
                        updateCompoundAction(action);
                    } else {
                        Toast.makeText(getContext(),
                                getString(R.string.selectMacroFailed),
                                Toast.LENGTH_LONG).show();
                    }
                });
            });

            seekbarDelay.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    delayAmount = progress;
                    String text = progress + "s";
                    delayObserver.setText(text);
                    updateCompoundAction(null);
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });

            seekbarRepeat.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    repeatAmount = progress;
                    String text = progress + "x";
                    repeatObserver.setText(text);
                    updateCompoundAction(null);
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });
        });

        // Inflate the layout for this fragment
        return view;
    }

    /**
     * Updates the compound action of the macro
     * @param action The action to put in the compound message,
     *               or the current action if null is given
     * @returns The compound action
     */
    protected CompoundMessage updateCompoundAction(Message action) {
        if (actibity.macro != null) {
            if (action == null){
                try {
                    action = actibity.macro.getAction();
                } catch (Exception e) {

                }
            }

            // Retrieve the actual action from the compound message
            if (action instanceof CompoundMessage) {
                Iterator<ComposedMessage.MessageDelay> it = ((CompoundMessage) action).iterator();
                if (it.hasNext()) {
                    action = it.next().message;
                } else {
                    action = null;
                }
            }

            // Create a compound action, if another action is defined
            if (action != null) {
                editAction.setText(action.toString());
                CompoundMessage message = new CompoundMessage();
                for (int i = 0; i < repeatAmount; i++) {
                    message.add(action, delayAmount * 1000);
                }
                actibity.macro.setAction(message);
                return message;
            }
        }
        return null;
    }

    /**
     * Retrieves the message delay of the currently saved action
     * @return The delay
     */
    protected int getCurrentDelay() {
        if (actibity.macro != null) {
            try {
                Message action = actibity.macro.getAction();
                Log.w("INSTANCEOF", (action instanceof CompoundMessage)+"");
                if (action instanceof CompoundMessage) {
                    Iterator<ComposedMessage.MessageDelay> it = ((CompoundMessage) action).iterator();
                    if (it.hasNext()) {
                        return it.next().delay;
                    }
                }
            } catch (Exception e) {

            }
        }
        return 0;
    }


    /**
     * Retrieves the repeat count of the currently saved action
     * @return The repeat count
     */
    protected int getCurrentRepeatCount() {
        if (actibity.macro != null) {
            try {
                Message action = actibity.macro.getAction();
                if (action instanceof CompoundMessage) {
                    int i = 0;
                    for (ComposedMessage.MessageDelay md: ((CompoundMessage) action)) {
                        i++;
                    }
                    return i;
                }
            } catch (Exception e) {

            }
        }
        return 1;
    }

    /**
     * Starts the prompts for the selection of an action
     * @param callback The callback to trigger once an action has been selected
     */
    protected void requestCommand(DataCallback<Message> callback) {
        // Dispose any old listeners
        removeListeners();

        // Create a new dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.selectMacroMain)
                .setItems(new String[]{
                        getString(R.string.selectMacroMainProfile),
                        getString(R.string.selectMacroMainMouse),
                        getString(R.string.selectMacroMainKeyboard),
                        getString(R.string.selectMacroMainProgram),
                        getString(R.string.selectMacroMainSite),
                }, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Profile was pressed
                        if (which == 0) {
                            requestProfileCommand(callback);

                        // Mouse was pressed
                        } else if (which == 1) {
                            requestMouseCommand(callback);

                        // Keyboard was pressed
                        } else if (which == 2) {
                            if (connection.getState() == Connection.CONNECTED) {
                                receiver = new MessageReceiver<ReturnKeyPress>() {
                                    public void receive(ReturnKeyPress keypress) {
                                        getActivity().runOnUiThread(() -> {
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
                                Toast.makeText(getContext(),
                                        getString(R.string.selectMacroPrompted),
                                        Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(getContext(),
                                        getString(R.string.selectMacroConnectionRequired),
                                        Toast.LENGTH_LONG).show();
                            }


                        // Program was pressed
                        } else if (which == 3) {
                            if (connection.getState() == Connection.CONNECTED) {
                                receiver = new MessageReceiver<ReturnFilePath>() {
                                    public void receive(ReturnFilePath filePath) {
                                        getActivity().runOnUiThread(() -> {
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
                                Toast.makeText(getContext(),
                                        getString(R.string.selectMacroPrompted),
                                        Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(getContext(),
                                        getString(R.string.selectMacroConnectionRequired),
                                        Toast.LENGTH_LONG).show();
                            }

                        // Site was pressed
                        } else if (which == 4) {
                            requestSiteCommand(callback);

                        // Idk what happened
                        } else {
                            callback.retrieve(null);
                        }
                    }
                });
        builder.show();
    }

    /**
     * Starts the prompts for the selection of a profile action
     * @param callback The callback to trigger once an action has been selected
     */
    protected void requestProfileCommand(DataCallback<Message> callback) {
        Profile.getAll(profiles -> {
            // Obtain the list of profile names
            String[] profileNames = new String[profiles.size()];
            for (int i = 0; i < profiles.size(); i++){
                profileNames[i] = profiles.get(i).getName();
            }

            getActivity().runOnUiThread(() -> {
                // Create a dialog to choose one of these profiles
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(R.string.selectMacroProfile)
                        .setItems(profileNames, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Profile p = profiles.get(which);
                                if (p != null) {
                                    callback.retrieve(new OpenProfileCmd(p.getID()));
                                }
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
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.selectMacroMouse)
                .setItems(new String[]{
                        getString(R.string.selectMacroMouseOpenTrackpad),
                        getString(R.string.selectMacroMouseLeftClick),
                        getString(R.string.selectMacroMouseRightClick),
                        getString(R.string.selectMacroMouseMiddleClick),
                        getString(R.string.selectMacroMouseScrollUp),
                        getString(R.string.selectMacroMouseScrollDown),
                }, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Open trackpad was pressed
                        if (which == 0) {
                            callback.retrieve(new OpenTrackpadCmd());

                        // Left click was pressed
                        } else if (which == 1) {
                            callback.retrieve(new ClickMouseCmd(ClickMouseCmd.LEFTCLICK));

                        // Right click was pressed
                        } else if (which == 2) {
                            callback.retrieve(new ClickMouseCmd(ClickMouseCmd.RIGHTCLICK));

                        // Middle click was pressed
                        } else if (which == 3) {
                            callback.retrieve(new ClickMouseCmd(ClickMouseCmd.MIDDLECLICK));

                        // Scroll up was pressed
                        } else if (which == 4) {
                            callback.retrieve(new ClickMouseCmd(ClickMouseCmd.SCROLLUP));

                        // Scroll down was pressed
                        } else if (which == 5) {
                            callback.retrieve(new ClickMouseCmd(ClickMouseCmd.SCROLLDOWN));

                        // Idk what happened
                        } else {
                            callback.retrieve(null);
                        }
                    }
                });
        builder.show();
    }

    /**
     * Starts the prompts for the selection of a open URL action
     * @param callback The callback to trigger once an action has been selected
     */
    protected void requestSiteCommand(DataCallback<Message> callback) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.selectMacroSite);

        final EditText input = new EditText(getActivity());
        builder.setView(input);
        builder.setPositiveButton(R.string.selectMacroSiteAccept,
                new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String url = input.getText().toString();
                callback.retrieve(new OpenURLCmd(url));
            }
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
