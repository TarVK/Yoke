package com.yoke.database.types;

import android.arch.core.util.Function;
import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Query;
import android.content.Context;

import com.yoke.database.DataBase;
import com.yoke.database.DataObject;
import com.yoke.utils.Callback;
import com.yoke.utils.DataCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * A class to manage the profile data in the database
 */
public class Profile extends DataObject<Profile.ProfileData> {
    // Keep track of the buttons on the profile
    List<Button> buttons;

    /**
     * Creates a profile with the given name
     * @param name  The name of the profile
     */
    public Profile(String name) {
        super(new Profile.ProfileData());
        this.setName(name);
        this.buttons = new ArrayList<Button>();
        this.setSize((byte)2, (byte)3);
    }

    /**
     * Creates a profile with the data that was obtained from the database
     * @param data  The data that was obtained from the database
     */
    protected Profile(ProfileData data) {
        super(data);
    }

    // Interaction methods (with super redundant java doc)

    /**
     * Retrieves the name of the profile
     * @return The name
     */
    public String getName() {
        return this.data.name;
    }

    /**
     * Retrieves the associated programs of the profile
     * @return The assoiciated programs
     */
    public String getAssociatedPrograms() {
        return this.data.associatedPrograms == null ? "" : this.data.associatedPrograms;
    }

    /**
     * Retrieves the list of buttons of the profile
     * @return The buttons
     */
    public List<Button> getButtons() {
        return this.buttons;
    }

    /**
     * Retrieves the width of the grid for the profile
     * @return The grid width
     */
    public byte getWidth() {
        return this.data.gridWidth;
    }

    /**
     * Retrieves the height of the grid for the profile
     * @return The grid height
     */
    public byte getHeight() {
        return this.data.gridHeight;
    }

    /**
     * Retrieves the index within the profile list for the profile
     * @return The index
     */
    public int getIndex() {
        return this.data.index;
    }

    /**
     * Sets the name for the profile
     * @param name The name
     */
    public void setName(String name) {
        this.data.name = name;
    }

    /**
     * Sets the associated programs of the profile
     * @param programs The associated programs (Comma separated)
     */
    public void setAssociatedPrograms(String programs) {
        this.data.associatedPrograms = programs;
    }

    /**
     * Sets the grid size of the profile
     * @param width  The grid width
     * @param height  The grid height
     */
    public void setSize(byte width, byte height) {
        this.data.gridWidth = width;
        this.data.gridHeight = height;
    }

    /**
     * Sets the index within the profile list for the profile
     * @param index  The index
     */
    public void setIndex(int index) {
        this.data.index = index;
    }

    /**
     * Retrieves whether or not the is space for another layout_button
     * @return  Whether or not there is still space to add a layout_button
     */
    public boolean hasSpace(){
        return this.buttons.size() < this.getWidth() * this.getHeight();
    }

    /**
     * Assigns the buttons belonging to this profile, to the profile
     * @param buttons  The buttons
     */
    protected void setButtons(List<Button> buttons) {
        this.buttons = buttons;
    }

    /**
     * Adds a layout_button to the profile (a layout_button can only be added to a single profile)
     * @param button  The layout_button
     * @throws IllegalStateException if there is no more space for a layout_button
     */
    public void addButton(Button button) throws IllegalStateException {
        // Make sure the index is within the grid
        if (!this.hasSpace()) {
            throw new IllegalStateException("Tried to add a layout_button to a full grid");
        }

        // Check if the spot is free
        byte index = button.getIndex();
        boolean notFree = false;
        do {
            notFree = false;
            for (Button b: this.buttons) {
                if (b.getIndex() == index) {
                    // If the index is not free, try the next index
                    notFree = true;
                    index++;
                    break;
                }
            }
            // Keep looking for an index until one is found
        } while (notFree);

        // Assign the index
        button.setIndex(index);

        // Store the layout_button
        this.buttons.add(button);
    }

    /**
     * Removes a layout_button from the profile
     * @param button  The layout_button
     */
    public void removeButton(Button button) {
        this.buttons.remove(button);
    }

    @Override
    public void save(Context context, Callback callback){
        Profile p = this;

        super.save(context, new Callback() {
            // Track how many of the buttons we have saved
            int completed = 0;

            public void call() {
                for (Button button: buttons) {
                    button.setProfile(p);
                    button.save(context, () -> {
                        // If we have saved all of the buttons, perform the callback
                        if (++completed == buttons.size()) {
                            callback.call();
                        }
                    });
                }

                // If there are no buttons to save, perform the callback immediately
                if (buttons.size() == 0) {
                    callback.call();
                }
            }
        });
    }

    @Override
    public void delete(Context context, Callback callback) {
        super.delete(context, new Callback() {
            // Track how many of the buttons we have deleted
            int completed = 0;

            public void call() {
                for (Button button: buttons) {
                    button.delete(context, () -> {
                        // If we have deleted all of the buttons, perform the callback
                        if (++completed == buttons.size()) {
                            callback.call();
                        }
                    });
                }

                // If there are no buttons to be deleted, perform the callback immediately
                if (buttons.size() == 0) {
                    callback.call();
                }
            }
        });
    }

    // Defines the data that is stored
    @Entity
    static public final class ProfileData extends DataObject.DataObjectData {
        @ColumnInfo(name = "name")
        public String name;

        @ColumnInfo(name = "associatedPrograms")
        public String associatedPrograms;

        @ColumnInfo(name = "gridWidth")
        public byte gridWidth;

        @ColumnInfo(name = "gridHeight")
        public byte gridHeight;

        @ColumnInfo(name = "index")
        public int index;
    }


    // Creates a method to return instances
    /**
     * Retrieves all of the profiles
     * @param context  The context to keep an association with the specific app
     * @param dataCallback  The callback to make once the data has been retrieved
     */
    public static void getAll(Context context, DataCallback<List<Profile>> dataCallback){
        // A callback for the getAll method to assign the buttons
        DataCallback<List<Profile>> getButtons = new DataCallback<List<Profile>>() {
            // Keep track of how many callbacks have been received
            int completed = 0;
            public void retrieve(List<Profile> data) {
                for (Profile profile: data) {
                    // Get all the buttons for the profile, and assign them
                    Button.getAll(context, profile.getID(), buttons -> {
                        profile.setButtons(buttons);

                        // Check if this was the last assignment, and if so perform the callback
                        if (++completed == data.size()) {
                            dataCallback.retrieve(data);
                        }
                    });
                }

                // Automatically finish if there is no data
                if (data.size() == 0) {
                    dataCallback.retrieve(data);
                }
            }
        };

        // Get the profiles and use the getButtons method before performing the callback
        DataBase.getInstance(context, (db) -> {
            DataObject.getAll(
                    db.profileDataDao(),
                    (profileData) -> new Profile(profileData),
                    getButtons);
        });
    }


    /**
     * Creates a decorator that will assign the buttons to a given profile
     * @param context  The context to keep an association with the specific app
     * @param dataCallback  The callback to decorate
     * @return The decorator
     */
    protected static DataCallback<Profile> getButtonAssigner(Context context, DataCallback<Profile> dataCallback) {
        return new DataCallback<Profile>() {
            public void retrieve(Profile profile) {
                // Make sure the profile is defined before continuing
                if (profile == null) {
                    dataCallback.retrieve(null);
                    return;
                }

                // Get all the buttons for the profile, and assign them
                Button.getAll(context, profile.getID(), buttons -> {
                    profile.setButtons(buttons);

                    // Perform the callback
                    dataCallback.retrieve(profile);
                });
            }
        };
    }

    /**
     * Retrieves an associated profile for a program, if existent
     * @param context  The context to keep an association with the specific app
     * @param program  The program title to get the associated profile for
     * @param dataCallback  The callback to make once the data has been retrieved
     */
    public static void getAssociated(Context context, String program, DataCallback<Profile> dataCallback){
        // A callback for the getAll method to assign the buttons
        DataCallback<Profile> getButtons = getButtonAssigner(context, dataCallback);

        // Get the profile and use the getButtons method before performing the callback
        DataBase.getInstance(context, (db) -> {
            new Thread(new Runnable() {
                public void run() {
                    // Get all the data
                    List<ProfileData> data = db.profileDataDao().getAll();

                    // Filter out profile data that matches the program
                    for (ProfileData pfData: data) {
                        // Make sure the data contains associated programs
                        if (pfData.associatedPrograms == null) {
                            continue;
                        }

                        // Get all of the patterns
                        String[] patterns = pfData.associatedPrograms.split(",");

                        // Check each of the patterns
                        for (String pattern: patterns) {
                            // Check if the pattern (partially) matches (case insensitive)
                            if (program.replaceAll("(?i)" + pattern.trim(), "").length()
                                    != program.length()) {
                                // Return the profile
                                getButtons.retrieve(new Profile(pfData));

                                // Don't continue looking
                                return;
                            }
                        }
                    }

                    // If no profile could be found, return null
                    getButtons.retrieve(null);
                }
            }).start();
        });
    }


    /**
     * Gets all of the instances for some data object type from the database
     * @param dao  The dao to use to get the data
     * @param getObject  The function to translate the data obtained from the database into a data object
     * @param dataCallback  The callback to return all the data to
     * @param <T>  The data object type to retrieve
     * @param <D>  The data object data that the data object uses
     */
    protected static <T extends DataObject<D>, D extends DataObject.DataObjectData> void getAll(
            final DataDao<D> dao,
            final Function<D, T> getObject,
            final DataCallback<List<T>> dataCallback) {
    }

    /**
     * Retrieves a specific profile
     * @param context  The context to keep an association with the specific app
     * @param ID  The ID of the profile to retrieve
     * @param dataCallback  The callback to make once the data has been retrieved
     */
    public static void getByID(Context context, long ID, DataCallback<Profile> dataCallback){
        // A callback for the getAll method to assign the buttons
        DataCallback<Profile> getButtons = getButtonAssigner(context, dataCallback);

        // Get the profile and use the getButtons method before performing the callback
        DataBase.getInstance(context, (db) -> {
            DataObject.getByID(
                    ID,
                    db.profileDataDao(),
                    (profileData)->new Profile(profileData),
                    getButtons);
        });
    }

    // Defines the associated queries
    @Dao
    public interface ProfileDataDao extends DataDao<Profile.ProfileData> {
        @Query("SELECT * FROM profileData")
        List<Profile.ProfileData> getAll();

        @Query("SELECT * FROM profileData WHERE uid=:ID")
        Profile.ProfileData getByID(long ID);
    }

    // Attaches the dao
    protected DataDao<Profile.ProfileData> getDoa(DataBase db) {
        return db.profileDataDao();
    }
}
