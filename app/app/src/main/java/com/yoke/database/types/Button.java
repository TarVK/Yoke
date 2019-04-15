package com.yoke.database.types;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Query;
import android.content.Context;

import com.yoke.database.DataBase;
import com.yoke.database.DataObject;
import com.yoke.utils.Callback;
import com.yoke.utils.DataCallback;

import java.util.List;

/**
 * A class that manages the data for connecting the profiles and macros together
 * Since this is a many to many relation
 */
public class Button extends DataObject<Button.ButtonData> {
    // A local reference to the assigned macro
    protected Macro macro;

    /**
     * Creates a layout_button for a profile
     * @param macro  The macro that this layout_button represents
     * @pre The macro needs to be saved at least once before the layout_button is saved
     */
    public Button(Macro macro) {
        super(new Button.ButtonData());
        this.setMacro(macro);
    }

    /**
     * Creates a layout_button with the data that was obtained from the database
     * @param data  The data that was obtained from the database
     */
    protected Button(Button.ButtonData data) {
        super(data);
    }

    // Interaction methods (with super redundant java doc)
    /**
     * Retrieves the ID of the macro
     * @return The ID of the macro
     */
    protected long getMacroID(){
        return this.data.macroID;
    }

    /**
     * Retrieves the macro of the layout_button
     * @return The macro that this layout_button represents
     */
    public Macro getMacro() {
        return this.macro;
    }

    /**
     * Retrieves the index in the grid of the layout_button
     * @return The index
     */
    public byte getIndex() {
        return this.data.gridIndex;
    }

    /**
     * Sets the macro of the layout_button
     * @param macro  The macro
     * @pre The macro needs to be saved at least once before the layout_button is saved
     */
    public void setMacro(Macro macro) {
        this.macro = macro;
    }

    /**
     * Sets the index of the layout_button in the grid
     * @param index  The index
     */
    public void setIndex(byte index) {
        this.data.gridIndex = index;
    }

    /**
     * Sets the profile of the layout_button
     * @param profile  The profile to store the layout_button in
     * @pre The profile needs to be saved at least once before assigned
     */
    protected void setProfile(Profile profile) {
        this.data.profileID = profile.getID();
    }


    /**
     * Saves the layout_button data into the database
     */
    public void save(Context context, Callback callback){
        // Assign the id from the macro
        this.data.macroID = this.macro.getID();

        // Save the data
        super.save(context, callback);
    }


    // Defines the data that is stored
    @Entity
    static public final class ButtonData extends DataObject.DataObjectData {
        @ColumnInfo(name = "gridIndex")
        public byte gridIndex;

        @ColumnInfo(name = "macroID")
        public long macroID;

        @ColumnInfo(name = "profileID")
        public long profileID;
    }


    // Creates a method to return instances
    /**
     * Creates a 'decorator' to assign buttons their macros
     * @param context  The context to keep an association with the specific app
     * @param dataCallback The callback to call after performing the action
     * @returns The decorator
     */
    protected static DataCallback<List<Button>> getMacroAssigner(Context context, DataCallback<List<Button>> dataCallback) {
        return new DataCallback<List<Button>>() {
            // Keep track of how many callbacks have been received
            int completed = 0;
            public void retrieve(List<Button> buttons) {
                for (Button button: buttons) {
                    // Get the macro of the layout_button, and assign it
                    Macro.getByID(context, button.getMacroID(), macro -> {
                        button.setMacro(macro);

                        // Check if this was the last assignment, and if so perform the callback
                        if (++completed == buttons.size()) {
                            dataCallback.retrieve(buttons);
                        }
                    });
                }

                // Automatically finish if there are not buttons
                if (buttons.size() == 0) {
                    dataCallback.retrieve(buttons);
                }
            }
        };
    }

    /**
     * Retrieves all of the buttons
     * @param context  The context to keep an association with the specific app
     * @param dataCallback  The callback to make once the data has been retrieved
     */
    public static void getAll(Context context, DataCallback<List<Button>> dataCallback){
        DataBase.getInstance(context, (db) -> {
            DataObject.getAll(
                    db.buttonDataDao(),
                    (buttonData)->new Button(buttonData),
                    getMacroAssigner(context, dataCallback));
        });
    }

    /**
     * Retrieves all of the buttons belonging to a specific profile
     * @param context  The context to keep an association with the specific app
     * @param profileID  The ID of the profile to get the buttons for
     * @param dataCallback  The callback to make once the data has been retrieved
     */
    public static void getAll(Context context, final long profileID, DataCallback<List<Button>> dataCallback){
        DataCallback<List<Button>> macroAssigner = getMacroAssigner(context, dataCallback);

        DataBase.getInstance(context, (db) -> {
            new Thread(new Runnable() {
                public void run() {
                    List<ButtonData> data = db.buttonDataDao().getAll(profileID);
                    macroAssigner.retrieve(mapAll(data, (buttonData) -> new Button(buttonData)));
                }
            }).start();
        });
    }


    /**
     * Retrieves a specific layout_button
     * @param context  The context to keep an association with the specific app
     * @param ID  The ID of the layout_button to retrieve
     * @param dataCallback  The callback to make once the data has been retrieved
     */
    public static void getByID(Context context, long ID, DataCallback<Button> dataCallback){
        // A callback for the getAll method to assign the macro
        DataCallback<Button> macroAssigner = new DataCallback<Button>() {
            public void retrieve(Button button) {
                // Get the macro of the layout_button, and assign it
                Macro.getByID(context, button.getMacroID(), macro -> {
                    button.setMacro(macro);

                    // Perform the callback
                    dataCallback.retrieve(button);
                });
            }
        };


        // Get the profile and use the macroAssigner method before performing the callback
        DataBase.getInstance(context, (db) -> {
            DataObject.getByID(
                    ID,
                    db.buttonDataDao(),
                    (buttonData) -> new Button(buttonData),
                    dataCallback);
        });
    }

    // Defines the associated queries
    @Dao
    public interface ButtonDataDao extends DataDao<Button.ButtonData> {
        @Query("SELECT * FROM buttonData")
        List<Button.ButtonData> getAll();


        @Query("SELECT * FROM buttonData WHERE profileID=:profileID")
        List<Button.ButtonData> getAll(long profileID);

        @Query("SELECT * FROM buttonData WHERE uid=:ID")
        Button.ButtonData getByID(long ID);
    }

    // Attaches the dao
    protected DataDao<Button.ButtonData> getDoa(DataBase db) {
        return db.buttonDataDao();
    }
}
