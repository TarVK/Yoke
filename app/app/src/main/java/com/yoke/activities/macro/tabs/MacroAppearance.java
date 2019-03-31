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
import android.widget.SeekBar;
import android.widget.Switch;

import com.example.yoke.R;
import com.theartofdev.edmodo.cropper.CropImage;
import com.yoke.database.types.Macro;

import java.io.IOException;

import yuku.ambilwarna.AmbilWarnaDialog;

public class MacroAppearance extends Fragment {

    private static final String TAG = "MacroAppearance";

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


    private Macro macro;


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


        foregroundImage = (ImageView) view.findViewById(R.id.foregroundImage);
        backgroundImage = (ImageView) view.findViewById(R.id.backgroundImage);



        //get macro data
        retrieveData();


        backgroundColorPicker = (ImageView) view.findViewById(R.id.backgroundColorPicker);
        descriptionColorPicker = (ImageView) view.findViewById(R.id.descriptionColorPicker);

        previewImage = (ImageView) view.findViewById(R.id.previewImage);

        //Initialize Switch and EditText
        descriptionSwitch = (Switch) view.findViewById(R.id.descriptionSwitch);
        descriptionValue = (EditText) view.findViewById(R.id.descriptionValue);

        //Set default enabled values for text
        descriptionSwitch.setChecked(textEnabled);
        descriptionValue.setEnabled(textEnabled);
        descriptionColorPicker.setEnabled(textEnabled);



        // Foreground Image OnclickListener
        foregroundImage
                .setOnClickListener(viewFGImage -> {
                    imageOption = 0;
                    pickFromGallery();
                });

        // Background Image OnclickListener
        backgroundImage
                .setOnClickListener(viewBGImage -> {
                    imageOption = 1;
                    pickFromGallery();
                });

        // Background Solid Color Picker OnclickListener
        backgroundColorPicker
                .setOnClickListener(viewBGColorPicker -> {
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
                .setOnClickListener(viewBGColorPicker -> {
                    imageOption = 2;
                    openColorPicker();
                });

        //Check if switch is pressed
        descriptionSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            descriptionValue.setEnabled(isChecked);
            macro.setTextEnabled(isChecked);
        });

        // EditText Change Listener (sets macro.text and updates preview)
        descriptionValue.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                macro.createCombinedImage((combBitmap) -> {
                    String text = s.toString();
                    macro.setText(text);
                    previewImage.setImageBitmap(combBitmap);
                });
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });


        // Foreground image Alpha
        seekAlphaForeground = (SeekBar) view.findViewById(R.id.seekAlphaForeground);
        seekAlphaForeground.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                macro.createCombinedImage((combBitmap) -> {
//                    macro.setForegroundAlpha(progress); //TODO add alpha to macro.java
                    previewImage.setImageBitmap(combBitmap);
                });
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
                macro.createCombinedImage((combBitmap) -> {
//                    int height = macro.getForegroundHeight();//TODO add size
//                    int width = macro.getForegroundWidth();
//                    int sHeight = height * (progress / 100);
//                    int sWidth = width * (progress / 100);
//                    macro.setForegroundHeight(sHeight);
//                    macro.setForeGroundWidth(sWidth);
                    previewImage.setImageBitmap(combBitmap);
                });
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
                macro.createCombinedImage((combBitmap) -> {
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
                    previewImage.setImageBitmap(combBitmap);
                });
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
                macro.createCombinedImage((combBitmap) -> {
//                    macro.setBackgroundAlpha(progress); //TODO add alpha to macro.java
                    previewImage.setImageBitmap(combBitmap);
                });
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
                macro.createCombinedImage((combBitmap) -> {
//                    int height = macro.getBackgroundHeight();//TODO add size
//                    int width = macro.getBackgroundWidth();
//                    int sHeight = height * (progress / 100);
//                    int sWidth = width * (progress / 100);
//                    macro.setBackgroundHeight(sHeight);
//                    macro.setBackGroundWidth(sWidth);
                    previewImage.setImageBitmap(combBitmap);
                });
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
                macro.createCombinedImage((combBitmap) -> {
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
                    previewImage.setImageBitmap(combBitmap);
                });
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


        // Inflate the layout for this fragment
        return view;
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
                                macro.createCombinedImage((combBitmap) -> {
                                    macro.setForegroundImage(bitmap);
                                    foregroundImage.setImageBitmap(bitmap);
                                    previewImage.setImageBitmap(combBitmap);
                                });
                                break;
                            case 1:
                                macro.createCombinedImage((combBitmap) -> {

                                    // Remove solid color and reset color picker image and its color
                                    macro.setBackgroundColor(0x00000000);
                                    backgroundColorPicker.setImageResource(R.drawable.color_picker);
                                    backgroundColorPicker.setColorFilter(R.color.colorSecondary);

                                    macro.setBackgroundImage(bitmap);
                                    backgroundImage.setImageBitmap(bitmap);
                                    previewImage.setImageBitmap(combBitmap);
                                });
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
                mDefaultColor = color;

                //IF background
                if (imageOption == 1) {
                    macro.createCombinedImage((combBitmap) -> {
                        backgroundColorPicker.setImageResource(R.drawable.reset_button);
                        hasSolidBackgroundColor = true;

                        macro.setBackgroundColor(color);
                        backgroundImage.setImageResource(R.drawable.default_image);
                        previewImage.setImageBitmap(combBitmap);
                    });
                //IF text
                } else if (imageOption == 2) {
                    macro.createCombinedImage((combBitmap) -> {
                        macro.setTextColor(color);
                        previewImage.setImageBitmap(combBitmap);
                    });
                } else {
                    Log.e(TAG, "Color Picker imageOption not set correctly: " + imageOption);
                }
            }
        });
        dialog.show();
    }

    public void retrieveData() {
        Long macroId = getActivity().getIntent().getLongExtra("macro id", -1);
        Log.w(TAG, "retrieveData: " + macroId);

        if (macroId == -1) {
            //TODO createMacro
            Log.d(TAG, "Macro should be created: " + macroId);
        } else if (macroId > -1 && macroId < 6) {
            Macro.getByID(macroId, (macro) -> {
                if (macro != null) {
                    Log.w(TAG, "here: " + macroId);
                    textEnabled = macro.isTextEnabled();

                    //TODO add exceptions in Macro.java (e.g. no foregroundimage selected use default) and backgroundcolor instead of backgroundImage)
                    foregroundImage.setImageBitmap(macro.getForegroundImage()); //TODO replace with macro.getForeground()
//                    foregroundAlpha = macro.getForegroundAlpha();
                    foregroundSize = macro.resolution;
//                    foregroundAspectRatio = macro.getForegroundApectRatio();

                    backgroundImage.setImageBitmap(macro.getBackgroundImage()); //TODO replace with macro.getBackground()
//                    backgroundAlpha = macro.getBackgroundALpha();
                    backgroundSize = macro.resolution;
//                    backgroundAspectRatio = macro.getBackgroundApectRatio();


                    previewImage.setImageBitmap(macro.getCombinedImage());
                } else {
                    Log.e(TAG, "macro is null" + macroId);
                }
            });
        } else {
            Log.e(TAG, "MacroActivity incorrectly called; wrong macroId: " + macroId);
        }
    }

}
