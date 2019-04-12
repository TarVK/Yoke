package com.yoke.activities.macro.tabs;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;

import com.example.yoke.R;
import com.theartofdev.edmodo.cropper.CropImage;
import com.yoke.activities.macro.MacroActivity;
import com.yoke.utils.DataCallback;

import java.io.IOException;

import yuku.ambilwarna.AmbilWarnaDialog;

public class MacroAppearance extends Fragment {

    private static final String TAG = "MacroAppearance";

    private ImageView foregroundColorPicker;
    private ImageView backgroundColorPicker;
    private ImageView descriptionColorPicker;
    private ImageView previewImage;

    private ImageView foregroundImage;
    private ImageView backgroundImage;

    private Switch descriptionSwitch;
    private EditText textValue;

    private boolean textEnabled;
    private boolean isForeground;

    // A reference to the macro activity
    private MacroActivity activity;

    public MacroAppearance() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        activity = (MacroActivity) getActivity();
        View view = inflater.inflate(R.layout.activity_macro_appearance,
                container, false);

        // Initialize ImageViews
        foregroundImage = (ImageView) view.findViewById(R.id.foregroundImage);
        backgroundImage = (ImageView) view.findViewById(R.id.backgroundImage);
        previewImage = (ImageView) view.findViewById(R.id.previewImage);

        // Initialize Color Pickers
        foregroundColorPicker = (ImageView) view.findViewById(R.id.foregroundColorPicker);
        backgroundColorPicker = (ImageView) view.findViewById(R.id.backgroundColorPicker);
        descriptionColorPicker = (ImageView) view.findViewById(R.id.textColorPicker);

        //Initialize Switch and EditText
        descriptionSwitch = (Switch) view.findViewById(R.id.textSwitch);
        textValue = (EditText) view.findViewById(R.id.textValue);


        // Load the macro, and afterwards assign all UI listeners
        activity.loadMacro(() -> {

            // Set default Foreground Image
            if (activity.macro.getForegroundImage() != null) {
                foregroundImage.setImageBitmap(activity.macro.getForegroundImage());
            } else {
                // Create new colored bitmap to reset the old
                Bitmap bitmap = Bitmap.createBitmap(300,300, Bitmap.Config.ARGB_8888);
                bitmap.eraseColor(activity.macro.getForegroundColor());
                foregroundImage.setImageBitmap(bitmap);
            }

            // Set default Background Image
            if (activity.macro.getBackgroundImage() != null) {
                backgroundImage.setImageBitmap(activity.macro.getBackgroundImage());
            } else {
                // Create new colored bitmap to reset the old
                Bitmap bitmap = Bitmap.createBitmap(300,300, Bitmap.Config.ARGB_8888);
                bitmap.eraseColor(activity.macro.getBackgroundColor());
                backgroundImage.setImageBitmap(bitmap);
            }

            // Set default Preview Image
            previewImage.setImageBitmap(activity.macro.getCombinedImage());

            //Set default enabled values for text
            descriptionSwitch.setChecked(activity.macro.isTextEnabled());
            descriptionColorPicker.setEnabled(activity.macro.isTextEnabled());
            textValue.setEnabled(activity.macro.isTextEnabled());
            textValue.setText(activity.macro.getText());
            textValue.setTextColor(activity.macro.getTextColor());

            // Set Foreground Image
            foregroundImage
                    .setOnClickListener(v -> {
                        CropImage.activity()
                                .setFixAspectRatio(true)
                                .start(getContext(), this);
                        isForeground = true;
                    });

            // Set Background Image
            backgroundImage
                    .setOnClickListener(v -> {
                        CropImage.activity()
                                .setFixAspectRatio(true)
                                .start(getContext(), this);
                        isForeground = false;
                    });

            // Set Foreground Color
            foregroundColorPicker
                    .setOnClickListener(v -> {
                        openColorPicker(activity.macro.getForegroundColor(), (color) -> {
                            // Create new colored bitmap to reset the old
                            Bitmap bitmap = Bitmap.createBitmap(300,300, Bitmap.Config.ARGB_8888);
                            bitmap.eraseColor(color);
                            foregroundImage.setImageBitmap(bitmap);
                            activity.macro.setForegroundImage(null);

                            // Set new color
                            foregroundImage.setColorFilter(color);
                            activity.macro.setForegroundColor(color);
                            updateImage();
                        });
                    });

            // Set Background Color
            backgroundColorPicker
                    .setOnClickListener(v -> {
                        openColorPicker(activity.macro.getBackgroundColor(), (color) -> {
                            // Create new colored bitmap to reset the old
                            Bitmap bitmap = Bitmap.createBitmap(300,300, Bitmap.Config.ARGB_8888);
                            bitmap.eraseColor(color);
                            backgroundImage.setImageBitmap(bitmap);
                            activity.macro.setBackgroundImage(null);

                            // Set new color
                            backgroundImage.setColorFilter(color);
                            activity.macro.setBackgroundColor(color);
                            updateImage();
                        });
                    });

            // Set Text Color
            descriptionColorPicker
                    .setOnClickListener(v -> {
                        openColorPicker(0x00000000, (color) -> {
                            // Set text color
                            textValue.setTextColor(color);
                            activity.macro.setTextColor(color);
                            updateImage();
                        });
                    });

            // Check if (isTextEnabled) switch is pressed
            descriptionSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
                textValue.setEnabled(isChecked);
                descriptionColorPicker.setEnabled(isChecked);
                activity.macro.setTextEnabled(isChecked);
                updateImage();
            });

            // Set Text value
            textValue.addTextChangedListener(new TextWatcher() {
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    String text = s.toString();
                    activity.macro.setText(text);
                    updateImage();
                }
            });
        });

        // Inflate the layout for this fragment
        return view;
    }

    /**
     * Updates the combined image preview
     */
    protected void updateImage() {
        activity.macro.createCombinedImage((combBitmap) -> {
            activity.runOnUiThread(() -> {
                previewImage.setImageBitmap(combBitmap);
            });
        });
    }

    /**
     * Creates a new Color Picker to choose a color from
     * @param callback color that will be returned by the color picker
     */
    public void openColorPicker(int defaultColor, DataCallback<Integer> callback) {
        AmbilWarnaDialog dialog = new AmbilWarnaDialog(getContext(), defaultColor, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onCancel(AmbilWarnaDialog dialog) {

            }

            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {
                callback.retrieve(color);
            }
        });
        dialog.show();
    }
    /**
     * Creates a new Color Picker to choose a color from
     * @param requestCode code needed to check from which object the method is called
     * @param resultCode code to check if result is valid
     * @param data result from the image cropper
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            Log.d(TAG, "Start ImageCropper: " + requestCode);

            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == Activity.RESULT_OK) {
                Uri resultUri = result.getUri();
                try {
                    // Get path from Uri and retrieve bitmap
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), resultUri);
                    if (isForeground) {
                        foregroundImage.setColorFilter(null);
                        foregroundImage.setImageBitmap(bitmap);
                        activity.macro.setForegroundImage(bitmap);
                    } else {
                        backgroundImage.setColorFilter(null);
                        backgroundImage.setImageBitmap(bitmap);
                        activity.macro.setBackgroundImage(bitmap);
                    }
                    updateImage();
                } catch (IOException e) {
                    Log.e(TAG, "Crop resultCode error: ", e);
                }
            }
        }
    }
}
