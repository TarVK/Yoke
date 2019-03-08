package com.yoke.database.types;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Query;
import android.graphics.Bitmap;

import com.yoke.connection.Message;
import com.yoke.database.DataBase;
import com.yoke.database.DataObject;
import com.yoke.utils.ImageTools;

import java.util.List;

public class Macro extends DataObject<Macro.MacroData> {
    public Macro(String name) {
        super(new MacroData());
        this.setName(name);
    }
    protected Macro(MacroData data) {
        super(data);
    }

    // Interaction methods (with super redundant java doc)
    /**
     * Retrieves the name of this macro to show in the available macro list
     * @return The name
     */
    public String getName() {
        return this.data.name;
    }

    /**
     * Retrieves the background image
     * @return The image as a bitmap
     */
    public Bitmap getBackgroundImage() {
        return ImageTools.getImageFromString(this.data.backgroundImage);
    }

    /**
     * Retrieves the foreground image
     * @return The image as a bitmap
     */
    public Bitmap getForegroundImage() {
        return ImageTools.getImageFromString(this.data.foregroundImage);
    }

    /**
     * Retrieves the background color
     * @return The color represented by an int
     */
    public int getBackgroundColor() {
        return this.data.backgroundColor;
    }

    /**
     * Retrieves the foreground color
     * @return The color represented by an int
     */
    public int getForegroundColor() {
        return this.data.foregroundColor;
    }

    /**
     * Retrieves the display text of the macro
     * @return The text
     */
    public String getText() {
        return this.data.text;
    }

    /**
     * Retrieves the display text's color
     * @return The color represented by an int
     */
    public int getTextColor() {
        return this.data.textColor;
    }

    /**
     * Retrieves whether or not the display text should show up
     * @return Whether it should show
     */
    public boolean isTextEnabled() {
        return this.data.useText;
    }

    /**
     * Retrieves the macro action that can be sent to the server
     * @return The action
     * @throws IllegalStateException  If the stored action is no longer valid
     */
    public Message getAction () throws IllegalStateException {
        try {
            return Message.deserialize(this.data.actionData);
        } catch (Exception e) {
            throw new IllegalStateException("This data does not contain a valid action");
        }
    }

    /**
     * Sets the name of the macro to show up in the available macro list
     * @param name The name
     */
    public void setName(String name) {
        this.data.name = name;
    }

    /**
     * Sets the background image
     * @param image The image
     */
    public void setBackgroundImage(Bitmap image) {
        this.data.backgroundImage = ImageTools.getStringFromImage(image);
    }

    /**
     * Sets the foreground image
     * @param image The image
     */
    public void setForegroundImage(Bitmap image) {
        this.data.foregroundImage = ImageTools.getStringFromImage(image);}

    /**
     * Sets the background color
     * @param color The color represented by an int
     */
    public void setBackgroundColor(int color) {
        this.data.backgroundColor = color;
    }

    /**
     * Sets the foreground color
     * @param color The color represented by an int
     */
    public void setForegroundColor(int color) {
        this.data.foregroundColor = color;
    }

    /**
     * Sets the display text of the button
     * @param text The text
     */
    public void setText(String text) {
        this.data.text = text;
    }

    /**
     * Sets the display text's color
     * @param color The color represented by an int
     */
    public void setTextColor(int color) {
        this.data.textColor = color;
    }

    /**
     * Sets whether or not the display text should show
     * @param enabled Whether it should show
     */
    public void setTextEnabled(boolean enabled) {
        this.data.useText = enabled;
    }

    /**
     * Sets the action to be sent to the server
     * @param action The action
     * @throws IllegalArgumentException  If the given data doesn't represent a valid action
     */
    public void setAction(Message action) {
        try {
            this.data.actionData = Message.serialize(action);
        } catch (Exception e) {
            throw new IllegalArgumentException("This data does not represent valid action");
        }
    }


    // Defines the data that is stored
    @Entity
    static public final class MacroData extends DataObject.DataObjectData {
        @ColumnInfo(name = "name")
        public String name;

        @ColumnInfo(name = "backgroundImage")
        public String backgroundImage;

        @ColumnInfo(name = "foregroundImage")
        public String foregroundImage;

        @ColumnInfo(name = "backgroundColor")
        public int backgroundColor;

        @ColumnInfo(name = "foregroundColor")
        public int foregroundColor;

        @ColumnInfo(name = "text")
        public String text;

        @ColumnInfo(name = "textColor")
        public int textColor;

        @ColumnInfo(name = "useText")
        public boolean useText;

        @ColumnInfo(name = "actionData", typeAffinity = ColumnInfo.BLOB)
        public byte[] actionData;
    }


    // Creates a method to return instances
    /**
     * Retrieves all of the macros
     * @param dataCallback  The callback to make once the data has been retrieved
     */
    public static void getAll(DataCallback<List<Macro>> dataCallback){
        DataObject.getAll(
                DataBase.getInstance().macroDataDao(),
                (macroData)->new Macro(macroData),
                dataCallback);
    }


    /**
     * Retrieves a specific macro
     * @param ID  The ID of the macro to retrieve
     * @param dataCallback  The callback to make once the data has been retrieved
     */
    public static void getByID(long ID, DataCallback<Macro> dataCallback){
        DataObject.getByID(
                ID,
                DataBase.getInstance().macroDataDao(),
                (macroData)->new Macro(macroData),
                dataCallback);
    }

    // Defines the associated queries
    @Dao
    public interface MacroDataDao extends DataDao<MacroData> {
        @Query("SELECT * FROM macroData")
        List<MacroData> getAll();

        @Query("SELECT * FROM macroData WHERE uid=:ID")
        MacroData getByID(long ID);
    }

    // Attaches the dao
    protected DataDao<MacroData> getDoa() {
        return db.macroDataDao();
    }
}
