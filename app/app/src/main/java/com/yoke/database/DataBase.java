package com.yoke.database;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.yoke.database.types.Button;
import com.yoke.database.types.Macro;
import com.yoke.database.types.Profile;
import com.yoke.database.types.Settings;

// Provide metadata for the class to be turned into a proper database
@Database(entities = {
        Macro.MacroData.class,
        Settings.SettingsData.class,
        Profile.ProfileData.class,
        Button.ButtonData.class
}, version = 37)

/**
 * A class to manage all stored data in the application
 */
public abstract class DataBase extends RoomDatabase {
    // All daos as per room's standard
    public abstract Macro.MacroDataDao macroDataDao();
    public abstract Settings.SettingsDataDao settingsDataDao();
    public abstract Profile.ProfileDataDao profileDataDao();
    public abstract Button.ButtonDataDao buttonDataDao();


    // Singleton
    private static DataBase instance;

    /**
     * Initializes the database
     * @param context  The context to keep an association with the specific app
     * @param done  The callback to be called once initialization has finished
     */
    public static void initialize(Context context, com.yoke.utils.Callback done){
        if (instance == null) {
            instance = Room.databaseBuilder(context,
                    DataBase.class, "data").fallbackToDestructiveMigration().build();
            Settings.initialize(done);
        } else {
            done.call();
        }
    }

    /**
     * Retrieves the singleton instance of the database
     * @return  The database
     */
    public static DataBase getInstance(){
        if (instance == null) {
            throw new ExceptionInInitializerError("The class hasn't been initialized yet");
        }

        return instance;
    }

    /**
     * Makes sure all of the data is properly disposed
     */
    public static void destroyInstance(){
        if (instance != null) {
            instance.close();
            instance = null;
        }
    }
}