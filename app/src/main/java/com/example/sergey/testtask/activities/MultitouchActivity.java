package com.example.sergey.testtask.activities;

import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.os.Bundle;
import android.widget.ImageView;

import com.example.sergey.testtask.R;

import java.io.IOException;

public class MultitouchActivity extends BaseActivity {

    private ImageView imageView;
    private Bitmap imageCameraBtmp, imageGalleryBtmp;
    private Uri imageGalleryUri;
    private static final String IMAGE_CAMERA_MULTITOUCH = "multitouch_camera", IMAGE_GALLERY_MULTITOUCH = "multitouch_gallery";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multitouch);
        initValues();
        initViews();
    }

    @Override
    void initValues() {
        imageCameraBtmp = getIntent().getParcelableExtra(IMAGE_CAMERA_MULTITOUCH);
        imageGalleryUri = getIntent().getParcelableExtra(IMAGE_GALLERY_MULTITOUCH);
        try {
            imageGalleryBtmp = MediaStore.Images.Media.getBitmap(getContentResolver(), imageGalleryUri);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    void initViews() {
        imageView = (ImageView) findViewById(R.id.imageViewMultitouch);
        if (imageCameraBtmp != null) {
            imageView.setImageBitmap(imageCameraBtmp);
        } else {
            imageView.setImageBitmap(imageGalleryBtmp);
        }
    }
}
