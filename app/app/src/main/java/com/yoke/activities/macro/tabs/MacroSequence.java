package com.yoke.activities.macro.tabs;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yoke.R;
import com.yoke.connection.Connection;
import com.yoke.connection.Message;
import com.yoke.connection.client.MultiClientConnection;
import com.yoke.connection.messages.prompts.RequestKeyPress;
import com.yoke.database.types.Macro;
import com.yoke.utils.Callback;


public class MacroSequence extends Fragment {

    private static final String TAG = "MacroSequence";

    protected Connection connection =
            MultiClientConnection.getInstance(); // Gets the connection

    private Long macroID;
    private Macro macro;

    private int delayAmount;
    private int repeatAmount;

    private SeekBar seekbarDelay;
    private SeekBar seekbarRepeat;

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

        View view = inflater.inflate(R.layout.activity_macro_sequence,
                container, false);

        editAction = view.findViewById(R.id.editAction);
        seekbarDelay = view.findViewById(R.id.seekBarDelay);
        seekbarRepeat = view.findViewById(R.id.seekbarRepeat);

        TextView delayObserver = view.findViewById(R.id.delayObserver);
        TextView repeatObserver = view.findViewById(R.id.repeatObserver);

        loadMacro(() -> {
            editAction.setOnClickListener(v -> {
                Macro.getByID(macroID, (macro) -> {
                    Log.d(TAG, "Request Action");
                    if (connection.getState() == Connection.CONNECTED) {
                        try {
                            //TODO send request
//                            connection.send();
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(getContext(),
                                    "Something went wrong while trying to perform this action",
                                    Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(getContext(), "Command could not be sent. " +
                                        "\n Please make sure you are connected",
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
     * Retrieves the macro and stores it
     * @param callback  Gets called once the macro has been loaded
     */
    protected void loadMacro(Callback callback) {
        macroID = getActivity().getIntent().getLongExtra("macro id", -1);
        Log.w(TAG, "retrieveData: " + macroID);

        // Check macroID
        if (macroID == -1) {
            Log.e(TAG, "MacroActivity has not been called properly: " + macroID);
            return;
        }

        Macro.getByID(macroID, macro -> {
            getActivity().runOnUiThread(() -> {
                MacroSequence.this.macro = macro;

                if (macro != null) {
                    Message action = macro.getAction();
                    editAction.setText(action.toString());

                    callback.call();
                } else {
                    Log.e(TAG, "Macro is not initialized: " + macroID);
                }
            });
        });
    }
}
