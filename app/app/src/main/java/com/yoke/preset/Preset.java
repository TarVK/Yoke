package com.yoke.preset;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.util.MutableInt;

import com.yoke.connection.Message;
import com.yoke.database.types.Button;
import com.yoke.database.types.Macro;
import com.yoke.database.types.Profile;
import com.yoke.utils.Callback;
import com.yoke.utils.DataCallback;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * A class that can be extended in order to easily create presets
 */
public abstract class Preset {
    // Store the context in order to retrieve resources
    protected Context context;

    // The actual profile that is being constructed
    protected Profile profile;

    // A list of listeners that try to retrieve the profile's ID
    protected List<DataCallback<Long>> creationCallbacks = new ArrayList<>();

    // Whether the preset has been created and initially stored
    protected boolean presetCreated = false;

    // The number of macros that finished being created
    protected int createdMacroCount = 0;

    // The number of macros in the profile
    protected byte buttonCount = 0;

    // The list of macros that should be added to the profile
    protected Macro[] macros;

    // A list of listeners that check whether the preset has been finished
    protected List<DataCallback<Long>> finishCallbacks = new ArrayList<>();

    // Whether the preset has been fully finished
    protected boolean presetFinished = false;


    /**
     * Constructs the preset
     * @param context  The context to keep an association with the specific app
     */
    protected Preset(Context context) {
        this.context = context;

        // Create a profile
        profile = new Profile("P" + Math.random());

        // Save the profile (such that it has an ID)
        profile.save(() -> {
            presetCreated = true;

            // Trigger all callbacks
            for (DataCallback<Long> callback: creationCallbacks) {
                callback.retrieve(profile.getID());
            }

            // Continue creating the preset
            setupProfile(profile);
        });
    }

    /**
     * Retrieves the ID asynchronously, as it might currently be constructed
     * @param createPresetCallback  The callback to trigger once the ID is obtained
     */
    public void getID(DataCallback<Long> createPresetCallback) {
        // If the profile has been saved already, just return its ID
        if (presetCreated) {
            createPresetCallback.retrieve(profile.getID());
            return;
        }

        // Otherwise add the callback to the lsiteners
        creationCallbacks.add(createPresetCallback);
    }

    /**
     * Retrieves the IDs of a set of presets
     * @param getIdsCallback  The callback to trigger once the ids have been retrieved
     * @param presets  The presets to get the IDs for
     */
    protected void getPresetIDs(DataCallback<Long[]> getIdsCallback, Preset... presets) {
        // Create an output array
        Long[] ids = new Long[presets.length];

        // The number of ids we received
        final AtomicInteger callbackCount = new AtomicInteger(0);

        // Go through all presets
        for (int i = 0; i < presets.length; i++) {
            final int index = i;
            Preset preset = presets[i];

            // Retrieve the ID of the preset
            preset.getID((id) -> {
                 ids[index] = id;

                 if (callbackCount.addAndGet(1) == ids.length) {
                     getIdsCallback.retrieve(ids);
                 }
            });
        }
    }

    /**
     * Fills the profile with the correct data
     * @param profile  The profile to fill
     */
    abstract protected void setupProfile(Profile profile);


    /**
     * Creates a macro using the specified data, unless a macro with the given name already exists
     * but doesn't add it to the profile
     * Should only be called from the setupProfile method
     * @param name  The name of the macro
     * @param imageResourceID  The id of the image resource for the layout_button
     * @param action  The action to perform once the layout_button is pressed
     */
    protected void createMacro(String name, int imageResourceID,
                            Message action) {
        this.addMacro(name, imageResourceID, action, false);
    }

    /**
     * Creates a macro using the specified data, unless a macro with the given name already exists
     * and adds it to the profile
     * Should only be called from the setupProfile method
     * @param name  The name of the macro
     * @param imageResourceID  The id of the image resource for the layout_button
     * @param action  The action to perform once the layout_button is pressed
     */
    protected void addMacro(String name, int imageResourceID,
                            Message action) {
        this.addMacro(name, imageResourceID, action, true);
    }

    /**
     * Creates a macro using the specified data, unless a macro with the given name already exists
     * and adds it to the profile
     * Should only be called from the setupProfile method
     * @param name  The name of the macro
     * @param imageResourceID  The id of the image resource for the layout_button
     * @param action  The action to perform once the layout_button is pressed
     * @param save  Whether the macro should be added to the profile or not
     */
    protected void addMacro(String name, int imageResourceID,
                               Message action, boolean save) {
        // Indicate that we need a callback from this macro
        final int index = buttonCount;

        if (save) {
            macros = new Macro[++buttonCount];
        }

        // Check if the macro already exists
        Macro.getByName(name, (macro) -> {
            if (macro != null) {
                if (save) {
                    addMacroCallback(macro, index);
                }
                return;
            }

            // If the macro doesn't exist yet, create it
            final Macro newMacro = new Macro(name);
            newMacro.setAction(action);
            newMacro.setBackgroundImage(
                    BitmapFactory.decodeResource(context.getResources(), imageResourceID));

            // Save the newly created macro
            newMacro.save(() -> {
                if (save) {
                    addMacroCallback(newMacro, index);
                }
            });
        });
    }

    /**
     * The method that should be called once a macro has been successfully created
     * @param macro  The macro that has been created
     * @param index  The index of the macro to add
     */
    protected void addMacroCallback(Macro macro, int index) {
        // Store the macro
        macros[index] = macro;

        // Check if all macros have been created
        if (++createdMacroCount == buttonCount) {
            completeSetup();
        }
    }

    /**
     * Adds all the created macros to the profile, saves it, and calls listeners
     */
    protected void completeSetup() {
        // Add all buttons
        for (Macro macro: macros) {
            profile.addButton(new Button(macro));
        }

        // Save the profile
        profile.save(() -> {
            presetFinished = true;

            // Trigger all listeners
            for (DataCallback<Long> finish: finishCallbacks) {
                finish.retrieve(profile.getID());
            }
        });
    }

    /**
     * Adds a callback to be called with the profile's ID once the preset is finished
     * @param onFinish  The callback
     */
    public void onFinish(DataCallback<Long> onFinish) {
        // If the preset has been finished already, just return its ID
        if (presetFinished) {
            onFinish.retrieve(profile.getID());
            return;
        }

        // Otherwise add the callback to the listeners
        finishCallbacks.add(onFinish);
    }

    /**
     * Wait for all presets to be created before continuing
     * @param callback  The callback to call once all presets have finished
     * @param presets  The presets to wait for
     */
    public static void onFinish(Callback callback, Preset... presets) {
        // The number of presets that finished
        final AtomicInteger callbackCount = new AtomicInteger(0);

        // Go through all presets
        for (Preset preset: presets) {
            preset.onFinish((id) -> {
                if (callbackCount.addAndGet(1) == presets.length) {
                    callback.call();
                }
            });
        }
    }
}
