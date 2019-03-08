package com.yoke.database.types;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Query;

import com.yoke.database.DataBase;
import com.yoke.database.DataObject;

import java.util.List;

public class Button extends DataObject<Button.ButtonData> {
    // A local reference to the assigned macro
    protected Macro macro;

    /**
     * Creates a button for a profile
     * @param macro  The macro that this button represents
     * @pre The macro needs to be saved at least once before the button is saved
     */
    public Button(Macro macro) {
        super(new Button.ButtonData());
        this.setMacro(macro);
    }
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
     * Retrieves the macro of the button
     * @return The macro that this button represents
     */
    public Macro getMacro() {
        return this.macro;
    }

    /**
     * Retrieves the index in the grid of the button
     * @return The index
     */
    public byte getIndex() {
        return this.data.gridIndex;
    }

    /**
     * Sets the macro of the button
     * @param macro  The macro
     * @pre The macro needs to be saved at least once before the button is saved
     */
    public void setMacro(Macro macro) {
        this.macro = macro;
    }

    /**
     * Sets the index of the button in the grid
     * @param index  The index
     */
    public void setIndex(byte index) {
        this.data.gridIndex = index;
    }

    /**
     * Sets the profile of the button
     * @param profile  The profile to store the button in
     * @pre The profile needs to be saved at least once before assigned
     */
    protected void setProfile(Profile profile) {
        this.data.profileID = profile.getID();
    }


    /**
     * Saves the button data into the database
     */
    public void save(Callback callback){
        // Assign the id from the macro
        this.data.macroID = this.macro.getID();

        // Save the data
        super.save(callback);
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
     * @param dataCallback The callback to call after performing the action
     * @returns The decorator
     */
    protected static DataCallback<List<Button>> getMacroAssigner(DataCallback<List<Button>> dataCallback) {
        return new DataCallback<List<Button>>() {
            // Keep track of how many callbacks have been received
            int completed = 0;
            public void retrieve(List<Button> buttons) {
                for (Button button: buttons) {
                    // Get the macro of the button, and assign it
                    Macro.getByID(button.getMacroID(), macro -> {
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
     * @param dataCallback  The callback to make once the data has been retrieved
     */
    public static void getAll(DataCallback<List<Button>> dataCallback){
        DataObject.getAll(
                DataBase.getInstance().buttonDataDao(),
                (buttonData)->new Button(buttonData),
                getMacroAssigner(dataCallback));
    }

    /**
     * Retrieves all of the buttons belonging to a specific profile
     * @param profileID  The ID of the profile to get the buttons for
     * @param dataCallback  The callback to make once the data has been retrieved
     */
    public static void getAll(final long profileID, DataCallback<List<Button>> dataCallback){
        DataCallback<List<Button>> macroAssigner = getMacroAssigner(dataCallback);

        new Thread(new Runnable() {
            public void run() {
                List<ButtonData> data = DataBase.getInstance().buttonDataDao().getAll(profileID);
                macroAssigner.retrieve(mapAll(data, (buttonData)->new Button(buttonData)));
            }
        }).start();
    }


    /**
     * Retrieves a specific button
     * @param ID  The ID of the button to retrieve
     * @param dataCallback  The callback to make once the data has been retrieved
     */
    public static void getByID(long ID, DataCallback<Button> dataCallback){
        // A callback for the getAll method to assign the macro
        DataCallback<Button> macroAssigner = new DataCallback<Button>() {
            public void retrieve(Button button) {
                // Get the macro of the button, and assign it
                Macro.getByID(button.getMacroID(), macro -> {
                    button.setMacro(macro);

                    // Perform the callback
                    dataCallback.retrieve(button);
                });
            }
        };


        // Get the profile and use the macroAssigner method before performing the callback
        DataObject.getByID(
                ID,
                DataBase.getInstance().buttonDataDao(),
                (buttonData)->new Button(buttonData),
                dataCallback);
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
    protected DataDao<Button.ButtonData> getDoa() {
        return db.buttonDataDao();
    }
}
