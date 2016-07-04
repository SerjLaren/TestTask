package com.example.sergey.testtask.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.example.sergey.testtask.R;

public class ScalingActivity extends BaseActivity implements View.OnClickListener {

    private ImageButton btnGallery, btnCamera;
    private boolean btnGalFlag, btnCamFlag;
    private static final int GALLERY_RESULT = 1, CAMERA_RESULT = 0;
    private static final String TYPE_IMAGE = "image/*", CAMERA_DATA = "data",
            IMAGE_CAMERA_MULTITOUCH = "multitouch_camera", IMAGE_GALLERY_MULTITOUCH = "multitouch_gallery";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scaling);
        initValues();
        initViews();
    }

    @Override
    void initValues() {
        btnGalFlag = false;
        btnCamFlag = false;
    }

    @Override
    void initViews() {
        btnGallery = (ImageButton) findViewById(R.id.btnGallery);
        btnGallery.setBackgroundResource(R.drawable.icon_gallery_1);
        btnGallery.setOnClickListener(this);
        btnCamera = (ImageButton) findViewById(R.id.btnCamera);
        btnCamera.setBackgroundResource(R.drawable.icon_camera_1);
        btnCamera.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnGallery:
                if (!btnGalFlag) {
                    /* Немного непонятно, как применить здесь нажатые состояния у кнопок из задания,
                     * поэтому реализовал их так, не очень удобно пользователю, но другого варианта
                      * к сожалению не увидел */
                    btnGallery.setBackgroundResource(R.drawable.icon_gallery_2);
                    Intent intentGallery = new Intent(Intent.ACTION_PICK);
                    intentGallery.setType(TYPE_IMAGE);
                    startActivityForResult(intentGallery, GALLERY_RESULT);
                    btnGalFlag = true;
                } else {
                    btnGallery.setBackgroundResource(R.drawable.icon_gallery_1);
                    btnGalFlag = false;
                }
                break;
            case R.id.btnCamera:
                if (!btnCamFlag) {
                    btnCamera.setBackgroundResource(R.drawable.icon_camera_2);
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, CAMERA_RESULT);
                    btnCamFlag = true;
                } else {
                    btnCamera.setBackgroundResource(R.drawable.icon_camera_1);
                    btnCamFlag = false;
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent returnedIntent) {
        super.onActivityResult(requestCode, resultCode, returnedIntent);

        switch(requestCode) {
            case GALLERY_RESULT:
                if (resultCode == RESULT_OK){
                    Uri selectedImage = returnedIntent.getData();
                    Intent intentMultitouch = new Intent(this, WebViewActivity.class);
                    intentMultitouch.putExtra(IMAGE_GALLERY_MULTITOUCH, selectedImage);
                    startActivity(intentMultitouch);
                }
                break;
            case CAMERA_RESULT:
                if (resultCode == RESULT_OK) {
                    Bitmap imageCameraBitmap = (Bitmap) returnedIntent.getExtras().get(CAMERA_DATA);
                    Intent intentMultitouch = new Intent(this, WebViewActivity.class);
                    intentMultitouch.putExtra(IMAGE_CAMERA_MULTITOUCH, imageCameraBitmap);
                    startActivity(intentMultitouch);
                }
                break;
        }
    }
}
