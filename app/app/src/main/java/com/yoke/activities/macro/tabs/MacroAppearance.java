package com.yoke.activities.macro.tabs;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import com.example.yoke.R;
import com.theartofdev.edmodo.cropper.CropImage;
import com.yoke.database.types.Macro;
import com.yoke.utils.Callback;

import java.io.IOException;
import java.lang.ref.WeakReference;

import yuku.ambilwarna.AmbilWarnaDialog;

public class MacroAppearance extends Fragment {

    private static final String TAG = "MacroAppearance";

    private Long macroID;
    private Macro macro;

    private ImageView foregroundColorPicker;
    private ImageView backgroundColorPicker;
    private ImageView descriptionColorPicker;
    private ImageView previewImage;

    private ImageView foregroundImage;
    private ImageView backgroundImage;

    private Switch descriptionSwitch;
    private EditText descriptionValue;

    private SeekBar seekAlphaForeground,
            seekSizeForeground,
            seekAspectForeground,
            seekAlphaBackground,
            seekSizeBackground,
            seekAspectBackground;

    private int imageOption;
    private int mDefaultColor = 0x00000000; //TODO Check if this works, else declare in onCreateView

    private boolean textEnabled;
    private boolean hasSolidBackgroundColor;

    private int foregroundAlpha;
    private int backgroundAlpha;

    private int foregroundSize;
    private int backgroundSize;

    private int foregroundAspectRatio;
    private int backgroundAspectRatio;

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

        View view = inflater.inflate(R.layout.activity_macro_appearance,
                container, false);

        // Initialize ImageViews
        foregroundImage = (ImageView) view.findViewById(R.id.foregroundImage);
        backgroundImage = (ImageView) view.findViewById(R.id.backgroundImage);
        previewImage = (ImageView) view.findViewById(R.id.previewImage);

        // Initialize Color Pickers
        foregroundColorPicker = (ImageView) view.findViewById(R.id.foregroundColorPicker);
        backgroundColorPicker = (ImageView) view.findViewById(R.id.backgroundColorPicker);
        descriptionColorPicker = (ImageView) view.findViewById(R.id.descriptionColorPicker);


        //Initialize Switch and EditText
        descriptionSwitch = (Switch) view.findViewById(R.id.descriptionSwitch);
        descriptionValue = (EditText) view.findViewById(R.id.descriptionValue);

        //Set default enabled values for text
        descriptionSwitch.setChecked(textEnabled);
        descriptionValue.setEnabled(textEnabled);
        descriptionColorPicker.setEnabled(textEnabled);

        // Foreground Image OnclickListener
        foregroundImage
                .setOnClickListener(v -> {
                    imageOption = 0;
                    pickFromGallery();
                });

        // Background Image OnclickListener
        backgroundImage
                .setOnClickListener(v -> {
                    imageOption = 1;
                    pickFromGallery();
                });

        // Foreground Solid Color Picker OnclickListener
        foregroundColorPicker
                .setOnClickListener(v -> {
                    if (hasSolidBackgroundColor) {
                        imageOption = 0;
                        pickFromGallery();
                    }
                    else {
                        imageOption = 0;
                        openColorPicker();
                    }
                });

        // Background Solid Color Picker OnclickListener
        backgroundColorPicker
                .setOnClickListener(v -> {
                    if (hasSolidBackgroundColor) {
                        imageOption = 1;
                        pickFromGallery();
                    }
                    else {
                        imageOption = 1;
                        openColorPicker();
                    }
                });

        // Background Solid Color Picker OnclickListener
        descriptionColorPicker
                .setOnClickListener(v -> {
                    imageOption = 2;
                    openColorPicker();
                });

        //Check if switch is pressed
        descriptionSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            descriptionValue.setEnabled(isChecked);
            descriptionColorPicker.setEnabled(isChecked);
            macro.setTextEnabled(isChecked);
            updateImage();
        });

        // Load the macro, and afterwards assign all UI listeners
        loadMacro(() -> {
            // EditText Change Listener (sets macro.text and updates preview)
            descriptionValue.addTextChangedListener(new TextWatcher() {
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    String text = s.toString();
                    macro.setText(text);
                    updateImage();
                }
            });


            // Foreground image Alpha
            seekAlphaForeground = (SeekBar) view.findViewById(R.id.seekAlphaForeground);
            seekAlphaForeground.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    foregroundAlpha = progress;
                    // Can't just do this, need to edit the actual image as well at least.
                    foregroundImage.setImageAlpha(foregroundAlpha);
                    updateImage();
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });

            view.findViewById(R.id.seekAlphaForegroundDefault)
                    .setOnClickListener(viewAlphaFGDefault -> seekAlphaForeground.setProgress(foregroundAlpha));


            // Foreground image Size
            seekSizeForeground = (SeekBar) view.findViewById(R.id.seekSizeForeground);
            seekSizeForeground.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                        Bitmap oldBitmap = macro.getForegroundImage();
//
//                        int width = oldBitmap.getWidth();
//                        int height = oldBitmap.getHeight();
//
//                        int dWidth = Math.round(width * (progress / 100));
//                        int dHeight = Math.round(height * (progress / 100));
//
//                        Bitmap newBitmap = Bitmap.createScaledBitmap(oldBitmap, dWidth, dHeight, false);
//
//                        foregroundImage.setImageBitmap(newBitmap);
//                        macro.setForegroundImage(newBitmap);
                    updateImage();
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });

            view.findViewById(R.id.seekSizeForegroundDefault)
                    .setOnClickListener(viewSizeFGDefault -> seekSizeForeground.setProgress(foregroundSize));


            // Foreground image Aspect Ratio
            seekAspectForeground = (SeekBar) view.findViewById(R.id.seekAspectForeground);
            seekAspectForeground.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                    int height = macro.getForegroundHeight();//TODO add size
//                    int width = macro.getForegroundWidth();
//                    int aspectRatio = width / height * (progress / 100);
//                    if (progress < 100) {
//                        int sHeight = height + height * (progress / 50);
//                        int sWidth = width - width * (progress / 50);
//                    } else {
//                        int sHeight = height - height * (progress / 50);
//                        int sWidth = width + width * (progress / 50);
//                    }
//                    macro.setForegroundHeight(sHeight);
//                    macro.setForeGroundWidth(sWidth);
                    updateImage();
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });


            view.findViewById(R.id.seekAspectForegroundDefault)
                    .setOnClickListener(viewAspectFGDefault -> seekAspectForeground.setProgress(foregroundAspectRatio));


            // Background image alpha
            seekAlphaBackground = (SeekBar) view.findViewById(R.id.seekAlphaBackground);
            seekAlphaBackground.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    backgroundAlpha = progress;
                    backgroundImage.setImageAlpha(backgroundAlpha);
                    updateImage();
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });

            view.findViewById(R.id.seekAlphaBackgroundDefault)
                    .setOnClickListener(viewAlphaBGDefault -> seekAlphaBackground.setProgress(backgroundAlpha));


            // Background image Size
            seekSizeBackground = (SeekBar) view.findViewById(R.id.seekSizeBackground);
            seekSizeBackground.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                    int height = macro.getBackgroundHeight();//TODO add size
//                    int width = macro.getBackgroundWidth();
//                    int sHeight = height * (progress / 100);
//                    int sWidth = width * (progress / 100);
//                    macro.setBackgroundHeight(sHeight);
//                    macro.setBackGroundWidth(sWidth);
                    updateImage();
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });

            view.findViewById(R.id.seekSizeBackgroundDefault)
                    .setOnClickListener(viewSizeBGDefault -> seekSizeBackground.setProgress(backgroundSize));


            // Background image Aspect Ratio
            seekAspectBackground = (SeekBar) view.findViewById(R.id.seekAspectBackground);
            seekAspectBackground.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                    int height = macro.getBackgroundHeight();//TODO add size
//                    int width = macro.getBackgroundWidth();
//                    int aspectRatio = width / height * (progress / 100);
//                    if (progress < 100) {
//                        int sHeight = height + height * (progress / 50);
//                        int sWidth = width - width * (progress / 50);
//                    } else {
//                        int sHeight = height - height * (progress / 50);
//                        int sWidth = width + width * (progress / 50);
//                    }
//                    macro.setBackgroundHeight(sHeight);
//                    macro.setBackGroundWidth(sWidth);
                    updateImage();
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });

            view.findViewById(R.id.seekAspectBackgroundDefault)
                    .setOnClickListener(viewAspectBGDefault -> seekAspectBackground.setProgress(backgroundAspectRatio));
        });


        // Inflate the layout for this fragment
        return view;
    }

    /**
     * Retrieves the macro and stores it
     * @param callback  Gets called once the macro has been loaded
     */
    protected void loadMacro(Callback callback) {
        macroID = getActivity().getIntent().getLongExtra("macro id", -1);
        Log.w(TAG, "retrieveData: " + macroID);


        if (macroID == -1) {
            //TODO createMacro
            Log.d(TAG, "Macro should be created: " + macroID);
        } else {
            Macro.getByID(macroID, macro -> {
                getActivity().runOnUiThread(() -> {
                    MacroAppearance.this.macro = macro;

                    if (macro != null) {

                        textEnabled = macro.isTextEnabled();

                        //TODO add exceptions in Macro.java (e.g. no foregroundimage selected use default) and backgroundcolor instead of backgroundImage)
                        foregroundImage.setImageBitmap(macro.getForegroundImage());
//                    foregroundAlpha = macro.getForegroundAlpha();
                        foregroundSize = macro.resolution;
//                    foregroundAspectRatio = macro.getForegroundApectRatio();

                        backgroundImage.setImageBitmap(macro.getBackgroundImage());
//                    backgroundAlpha = macro.getBackgroundAlpha();
                        backgroundSize = macro.resolution;
//                    backgroundAspectRatio = macro.getBackgroundApectRatio();

                        previewImage.setImageBitmap(macro.getCombinedImage());

                        callback.call();
                    } else {
                        Log.e(TAG, "Macro is not initialized: " + macroID);
                    }
                });
            });
        }
    }

    /**
     * Updates the combined image preview
     */
    protected void updateImage() {
        macro.createCombinedImage((combBitmap) -> {
            getActivity().runOnUiThread(() -> {
                previewImage.setImageBitmap(combBitmap);
            });
        });
    }

    public void pickFromGallery() {
        //Create an Intent with action as ACTION_PICK
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);

        // Sets the type as image/*. This ensures only components of type image are selected
        intent.setType("image/*");

        //  Only show openable files (such as Image files)
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        //We pass an extra array with the accepted mime types. This will ensure only components with these MIME types as targeted.
        String[] mimeTypes = {"image/jpeg", "image/png"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);

        // Launching the Intent
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Don't do anything if no macro is present for whatever reason
        if (macro == null) {
            Log.w("Detect", "Detect");
            return;
        }

        switch (requestCode) {

            // Get (Uri) image and start cropping activity
            case CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE:
                Uri imageUri = CropImage.getPickImageResultUri(getContext(), data);
                CropImage.activity(imageUri)
                        .setFixAspectRatio(true)
                        .start(getContext(), this);
                break;

            // Get (CropImage.ActivityResult) image and replace current image based on (int) imageOption
            case CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE:
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                if (resultCode == Activity.RESULT_OK) {
                    Uri resultUri = result.getUri();
                    try {

                        // Get path from Uri and retrieve bitmap
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), resultUri);
                        switch (imageOption) {
                            case 0:
                                // Remove solid color and reset color picker image and its color
                                macro.setForegroundColor(0x00000000);
                                foregroundColorPicker.setImageResource(R.drawable.color_picker);

                                // Set appropriate images
                                macro.setForegroundImage(bitmap);
                                foregroundImage.setImageBitmap(bitmap);

                                // Update the preview
                                updateImage();

                                break;
                            case 1:
                                // Remove solid color and reset color picker image and its color
                                macro.setBackgroundColor(0x00000000);
                                backgroundColorPicker.setImageResource(R.drawable.color_picker);

                                // Set appropriate images
                                macro.setBackgroundImage(bitmap);
                                backgroundImage.setImageBitmap(bitmap);

                                // Update the preview
                                updateImage();

                                break;
                            default:
                                Log.e(TAG, "imageOption unintentionally changed");
                                break;
                        }
                    } catch (IOException e) {
                        Log.e(TAG, "Crop resultCode error: ", e);
                    }
                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    Exception e = result.getError(); // Skip if 'failed to load sampled bitmap'
                    Log.e(TAG, "Crop Activity error: ", e);
                }
                break;
            default:
                Log.e(TAG, "Crop Activity incorrectly called, rqCode: " + requestCode);
                break;
        }
    }

    public void openColorPicker() {
        AmbilWarnaDialog dialog = new AmbilWarnaDialog(getContext(), mDefaultColor, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onCancel(AmbilWarnaDialog dialog) {
                Log.d(TAG, "Color Picker action cancelled, dialog closed");
            }

            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {
                // Don't do anything if no macro is present for whatever reason
                if (macro == null) {
                    Log.w("Detect2", "Detect2");
                    return;
                }

                mDefaultColor = color;

                //IF foreground
                if (imageOption == 0) {
                    foregroundColorPicker.setImageResource(R.drawable.reset_button);
                    hasSolidBackgroundColor = true;

                    macro.setForegroundColor(color);
                    foregroundImage.setColorFilter(color);
                    foregroundImage.setImageResource(R.drawable.default_image);

                    // Update the preview
                    updateImage();

                //IF background
                } else if (imageOption == 1) {
                    backgroundColorPicker.setImageResource(R.drawable.reset_button);
                    hasSolidBackgroundColor = true;

                    macro.setBackgroundColor(color);
                    backgroundImage.setColorFilter(color);
                    backgroundImage.setImageResource(R.drawable.default_image);

                    // Update the preview
                    updateImage();
                //IF text
                } else if (imageOption == 2) {
                    macro.setTextColor(color);

                    // Update the preview
                    updateImage();
                } else {
                    Log.e(TAG, "Color Picker imageOption not set correctly: " + imageOption);
                }
            }
        });
        dialog.show();
    }

}
