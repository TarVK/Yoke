package com.yoke.database.types;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Query;

import com.yoke.database.DataBase;
import com.yoke.database.DataObject;

import java.util.List;

public class Settings extends DataObject<Settings.SettingsData> {
    protected static Settings INSTANCE;
    protected Settings() {
        super(new Settings.SettingsData());
    }
    protected Settings(Settings.SettingsData data) {
        super(data);
    }

    /**
     * Sets up the singleton settings instance
     * @param done  The callback to be called once initialization has completed
     */
    public static void initialize(DataObject.Callback done) {
        Settings.get(new DataCallback<Settings>() {
            public void retrieve(Settings settings) {
                INSTANCE = settings;
                done.call();
            }
        });
    }

    /**
     * Retrieves the singleton settings object
     * @return Settings
     */
    public static Settings getInstance(){
        return INSTANCE;
    }

    // Interaction methods (with super redundant java doc)
    /**
     * Retrieves the user's language
     * @return  The language represented by a string
     */
    public String getLanguage() {
        return this.data.language;
    }

    /**
     * Retrieves an identifier for the color scheme to use
     * @return  The identifier
     */
    public byte getMainColorID () {
        return this.data.mainColorID;
    }

    /**
     * Retrieves an identifier for the connection type to use
     * @return  The identifier
     */
    public byte getConnectionType () {
        return this.data.connectionType;
    }

    /**
     * Retrieves whether or not the light theme is enabled
     * @return  Whether the light theme is enabled
     */
    public boolean getUsesLightTheme () {
        return this.data.useLightTheme;
    }

    /**
     * Retrieves whether or not automatic pc server startup is enabled
     * @return  Whether or not automatic startup is enabled
     */
    public boolean getUsesAutomaticStartup () {
        return this.data.useAutomaticStartup;
    }

    /**
     * Sets the language
     * @param language The language as represented by a string
     */
    public void setLanguage(String language) {
        this.data.language = language;
    }

    /**
     * Sets the identifier of the color scheme to use
     * @param colorID  The identifier of the color scheme
     */
    public void setMainColorID(byte colorID) {
        this.data.mainColorID = colorID;
    }

    /**
     * Sets the identifier for the connection type to use
     * @param connectionType  The identifier of the connection type
     */
    public void setConnectionType(byte connectionType) {
        this.data.connectionType = connectionType;
    }

    /**
     * Sets whether or not the light theme should be used
     * @param usesLightTheme  Whether or not the light theme should be used
     */
    public void setUsesLightTheme(boolean usesLightTheme) {
        this.data.useLightTheme = usesLightTheme;
    }

    /**
     * Sets whether or not the pc server should automaticall start up
     * @param usesAutomaticStartup  Whether or not the pc server shoulds tart automatically
     */
    public void setUsesAutomaticStartup(boolean usesAutomaticStartup) {
        this.data.useAutomaticStartup = usesAutomaticStartup;
    }

    // Defines the data that is stored
    @Entity
    static public final class SettingsData extends DataObject.DataObjectData {
        @ColumnInfo(name = "language")
        public String language;

        @ColumnInfo(name = "mainColorID")
        public byte mainColorID;

        @ColumnInfo(name = "connectionType")
        public byte connectionType;

        @ColumnInfo(name = "useLightTheme")
        public boolean useLightTheme;

        @ColumnInfo(name = "useAutomaticStartup")
        public boolean useAutomaticStartup;
    }


    // Creates a method to return instances
    protected static void get(DataObject.DataCallback<Settings> callback){
        new Thread(new Runnable() {
            public void run() {
                SettingsData data = DataBase.getInstance().settingsDataDao().get();
                if (data == null) {
                    callback.retrieve(new Settings());
                } else {
                    callback.retrieve(new Settings(data));
                }
            }
        }).start();
    }

    // Defines the associated queries
    @Dao
    public interface SettingsDataDao extends DataObject.DataDao<Settings.SettingsData> {
        @Query("SELECT * FROM settingsData Limit 1")
        Settings.SettingsData get();

        // Not used, but need to be present for compilation
        @Query("SELECT * FROM settingsData")
        List<Settings.SettingsData> getAll();

        @Query("SELECT * FROM settingsData WHERE uid=:ID")
        Settings.SettingsData getByID(long ID);
    }

    // Attaches the dao
    protected DataObject.DataDao<Settings.SettingsData> getDoa() {
        return db.settingsDataDao();
    }
}