package com.yoke.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class ImageTools{
    /**
     * Turns an image to a base64 string
     * @param image  The image to turn into a string
     * @return  The retrieved string
     */
    public static String getStringFromImage(Bitmap image){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] bytes = stream.toByteArray();
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }

    /**
     * Turns a base64 string into an image
     * @param string  The string to turn into an image
     * @return  The retrieved image
     */
    public static Bitmap getImageFromString(String string){
        byte[] bytes = Base64.decode(string, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    /**
     * Turns a square image into a circular image
     * @param image  The image to make circular
     * @param irl  The listener to return the created image to
     */
    public static void makeImageCircular(Bitmap image, ImageResultListener irl){
        makeImageCircular(image, 0, 0, irl);
    }

    /**
     * Turns a square image into a circular image
     * @param image  The image to make circular
     * @param borderWidth  The width of the border to add to the image
     * @param borderIntColor  The color of the border to add
     * @param irl  The listener to return the created image to
     */
    public static void makeImageCircular(Bitmap image, final int borderWidth, final int borderIntColor, final ImageResultListener irl){
        (new AsyncTask<Bitmap, Void, Bitmap>(){
            protected Bitmap doInBackground(Bitmap... image){
                // Create the new image as a copy of the original
                Bitmap newImage = image[0].copy(Bitmap.Config.ARGB_8888, true);
                newImage.setHasAlpha(true);
                Canvas canvas = new Canvas(newImage);

                // Get some properties
                int width = canvas.getWidth();
                int height = canvas.getHeight();
                int radius = Math.min(width, height) / 2 - borderWidth;
                int borderRadius = radius + borderWidth;
                int radSq = radius * radius;
                int borderRadSq = borderRadius * borderRadius;

                // Create the colors to use for the transparency and border
                Paint color = new Paint();
                color.setColor(Color.TRANSPARENT);
                color.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OUT));

                Paint borderColor = new Paint();
                borderColor.setColor(borderIntColor);
                borderColor.setAlpha(Color.alpha(borderIntColor));
                borderColor.setStyle(Paint.Style.FILL);
                borderColor.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

                //loop through pixels and turn them transparent when outside of the circle
                for (int x = 0; x < width; x++) {
                    int dX = x - width / 2;
                    for (int y = 0; y < height; y++) {
                        int dY = y - height / 2;
                        int dist = dX * dX + dY * dY;

                        // Check what color should be used
                        if (dist > borderRadSq) {
                            canvas.drawPoint(x, y, color);
                        } else if (dist > radSq) {
                            canvas.drawPoint(x, y, borderColor);
                        }
                    }
                }

                // Return the created image
                return newImage;
            }
            protected void onPostExecute(Bitmap result) {
                irl.onImageRecieve(result);
            }
        }).execute(image);
    }

    /**
     * A method to download an image from a url, and turn it into a bitmap
     * @param url  The url to get the image from
     * @param irl  The listener to return the retrieve image to
     */
    public static void downloadImage(final String url, final ImageResultListener irl){
        (new AsyncTask<String, Void, Bitmap>(){
            protected Bitmap doInBackground(String... urls) {
                String urldisplay = urls[0];
                Bitmap mIcon = null;
                try {
                    InputStream in = new java.net.URL(urldisplay).openStream();
                    return BitmapFactory.decodeStream(in);
                } catch (Exception e) {
                    Log.e("Error", e.getMessage());
                    e.printStackTrace();
                }
                return mIcon;
            }

            protected void onPostExecute(Bitmap result) {
                irl.onImageRecieve(result);
            }
        }).execute(url);
    }
    public interface ImageResultListener{
        void onImageRecieve(Bitmap image);
    }
}