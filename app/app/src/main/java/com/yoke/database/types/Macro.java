package com.yoke.database.types;

import android.annotation.SuppressLint;
import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Query;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.AsyncTask;

import com.yoke.connection.Message;
import com.yoke.database.DataBase;
import com.yoke.database.DataObject;
import com.yoke.utils.ImageTools;

import java.util.List;

/**
 * A class to manage the macro data in the database
 */
public class Macro extends DataObject<Macro.MacroData> {
    // Define a resolution for the macro images
    public static final int resolution = 400;

    // Define the font size relative to the resolution, e.g. 0.25 is 1/4th of resolution in height
    public static final double fontScale = 0.1;

    /**
     * Creates a new macro with a certain name
     * @param name  The name of the macro
     */
    public Macro(String name) {
        super(new MacroData());
        this.setName(name);
    }

    /**
     * Creates a macro with the data that was obtained from the database
     * @param data  The data that was obtained from the database
     */
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
     * Retrieves the final image to represent all of the macro
     * @return The image representing all of the visual data
     */
    public Bitmap getCombinedImage() {
        return ImageTools.getImageFromString(this.data.combinedImage);
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
        return ImageTools.getImageFromString(this.data.foregroundImage); //TODO refactor to getForeground
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
    } //TODO remove

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
        this.data.foregroundImage = ImageTools.getStringFromImage(image);
    }

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

    @SuppressLint("StaticFieldLeak")
    /**
     * Combines the macro data into a single image
     * @param callback  The callback to be called to get the resulting image
     */
    public void createCombinedImage(DataCallback<Bitmap> callback) {
        (new AsyncTask<Void, Void, Bitmap>(){
            protected Bitmap doInBackground(Void... nothing){
                // Create the image
                Bitmap newImage = Bitmap.createBitmap(resolution, resolution,
                        Bitmap.Config.ARGB_8888);
                newImage.setHasAlpha(true);
                Canvas canvas = new Canvas(newImage);

                // Get some properties
                int width = canvas.getWidth();
                int height = canvas.getHeight();
                Bitmap backgroundImage = getBackgroundImage();
                Bitmap foregroundImage = getForegroundImage();
                int backgroundColor = getBackgroundColor();
                int foregroundColor = getForegroundColor();
                boolean useText = isTextEnabled();
                int textColor = getTextColor();
                String text = getText();

                // Draw the first layer
                if (backgroundImage != null) {
                    canvas.drawBitmap(backgroundImage,
                            new Rect(0, 0,
                                    backgroundImage.getWidth(),
                                    backgroundImage.getHeight()
                            ), new Rect(0, 0,
                                    resolution,
                                    resolution
                            ), null);
                } else {
                    Paint paint = new Paint();
                    paint.setColor(backgroundColor);
                    canvas.drawRect(0, 0, resolution, resolution, paint);
                }

                // Draw the second layer
                if (foregroundImage != null) {
                    canvas.drawBitmap(foregroundImage,
                            new Rect(0, 0,
                                    foregroundImage.getWidth(),
                                    foregroundImage.getHeight()
                            ), new Rect(0, 0,
                                    resolution,
                                    resolution
                            ), null);
                } else {
                    Paint paint = new Paint();
                    paint.setColor(foregroundColor);
                    canvas.drawRect(0, 0, resolution, resolution, paint);
                }

                // Draw text
                if (useText) {
                    // Determine the y level for the tezt
                    int y = resolution/2;
                    if (foregroundImage != null || backgroundImage != null) {
                        y = resolution/5;
                    }

                    // Create the font
                    Paint textPaint = new Paint();
                    textPaint.setColor(textColor);
                    textPaint.setTextAlign(Paint.Align.CENTER);
                    textPaint.setTypeface(Typeface.DEFAULT);
                    textPaint.setTextSize((int) (resolution * fontScale));

                    // Draw the text
                    canvas.drawText(text,resolution/2, y, textPaint);
                }

                // Return the created image
                return newImage;
            }
            protected void onPostExecute(Bitmap result) {
                callback.retrieve(result);
            }
        }).execute();
    }

    @Override
    public void save(Callback callback) {
        createCombinedImage((image) -> {
            data.combinedImage = ImageTools.getStringFromImage(image);
            super.save(callback);
        });
    }


    // Defines the data that is stored
    @Entity
    static public final class MacroData extends DataObject.DataObjectData {
        @ColumnInfo(name = "name")
        public String name;

        @ColumnInfo(name = "combinedImage")
        public String combinedImage;

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

    /**
     * Retrieves a specific macro
     * @param name  The name of the macro to retrieve
     * @param dataCallback  The callback to make once the data has been retrieved
     */
    public static void getByName(String name, DataCallback<Macro> dataCallback){
        final MacroDataDao dao = DataBase.getInstance().macroDataDao();
        new Thread(new Runnable() {
            public void run() {
                MacroData data = dao.getByName(name);
                dataCallback.retrieve(data != null ? new Macro(data) : null);
            }
        }).start();
    }

    // Defines the associated queries
    @Dao
    public interface MacroDataDao extends DataDao<MacroData> {
        @Query("SELECT * FROM macroData")
        List<MacroData> getAll();

        @Query("SELECT * FROM macroData WHERE uid=:ID")
        MacroData getByID(long ID);

        @Query("SELECT * FROM macroData WHERE name=:name")
        MacroData getByName(String name);
    }

    // Attaches the dao
    protected DataDao<MacroData> getDoa() {
        return db.macroDataDao();
    }
}
