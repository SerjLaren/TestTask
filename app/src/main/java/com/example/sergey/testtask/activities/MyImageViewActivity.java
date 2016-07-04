package com.example.sergey.testtask.activities;

import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.os.Bundle;

import com.example.sergey.testtask.R;
import com.example.sergey.testtask.helpers.MyImageView;

import java.io.IOException;


public class MyImageViewActivity extends BaseActivity {

    private Bitmap imageCameraBtmp, imageGalleryBtmp;
    private static final String IMAGE_CAMERA_MULTITOUCH = "multitouch_camera",
            IMAGE_GALLERY_MULTITOUCH = "multitouch_gallery";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_image_view);
        initValues();
        initViews();
    }

    @Override
    void initValues() {
        imageCameraBtmp = getIntent().getParcelableExtra(IMAGE_CAMERA_MULTITOUCH);
        Uri imageGalleryUri = getIntent().getParcelableExtra(IMAGE_GALLERY_MULTITOUCH);
        if (imageGalleryUri != null) {
            try {
                imageGalleryBtmp = MediaStore.Images.Media.getBitmap(getContentResolver(), imageGalleryUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    void initViews() {
        MyImageView imageView = (MyImageView) findViewById(R.id.imageViewMultitouch);
        if (imageCameraBtmp != null) {
            imageView.setImageBitmap(imageCameraBtmp);
        } else {
            imageView.setImageBitmap(imageGalleryBtmp);
        }
    }
}
