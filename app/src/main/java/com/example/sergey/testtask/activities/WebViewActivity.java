package com.example.sergey.testtask.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;

import com.example.sergey.testtask.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class WebViewActivity extends BaseActivity implements View.OnClickListener {

    private Intent intentMultitouch;
    private Bitmap imageCameraBtmp, imageGalleryBtmp;
    private Uri imageGalleryUri;
    private int compressQuality;
    private static final String TYPE_DATA = "text/html", ENCODING = "UTF-8";
    private static final String IMAGE_CAMERA_MULTITOUCH = "multitouch_camera",
            IMAGE_GALLERY_MULTITOUCH = "multitouch_gallery";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        initValues();
        initViews();
    }

    @Override
    void initValues() {
        imageCameraBtmp = getIntent().getParcelableExtra(IMAGE_CAMERA_MULTITOUCH);
        imageGalleryUri = getIntent().getParcelableExtra(IMAGE_GALLERY_MULTITOUCH);
        compressQuality = 50;
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
        WebView imageWebView = (WebView) findViewById(R.id.imageWebView);
        Button btnMyImageview = (Button) findViewById(R.id.btnToMyImageview);
        btnMyImageview.setOnClickListener(this);
        if (imageCameraBtmp != null) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            imageCameraBtmp.compress(Bitmap.CompressFormat.PNG, compressQuality, stream);
            byte[] bitmapData = stream.toByteArray();
            String image64 = Base64.encodeToString(bitmapData, Base64.DEFAULT); // кодируем картинку для вставки в WebView
            String pageData = "<html><head><style type='text/css'>" +
                    "body{margin:auto auto;text-align:center;} img{height:100%25;} </style></head>" +
                    "<body>" + "<img src=\"data:image/jpeg;base64," + image64 + "\" /></body></html>";
            imageWebView.loadData(pageData, TYPE_DATA, ENCODING); // вставка картинки в WebView
            imageWebView.getSettings().setSupportZoom(true);
            imageWebView.getSettings().setBuiltInZoomControls(true);
        } else {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            imageGalleryBtmp.compress(Bitmap.CompressFormat.JPEG, compressQuality, stream);
            byte[] bitmapData = stream.toByteArray();
            String image64 = Base64.encodeToString(bitmapData, Base64.DEFAULT); // кодируем картинку для вставки в WebView
            String pageData = "<html><head><style type='text/css'>" +
                    "body{margin:auto auto;text-align:center;} img{width:100%25;} </style></head>" +
                    "<body>" + "<img src=\"data:image/jpeg;base64," + image64 + "\" /></body></html>";
            imageWebView.loadData(pageData, TYPE_DATA, ENCODING); // вставка картинки в WebView
            imageWebView.getSettings().setSupportZoom(true);
            imageWebView.getSettings().setBuiltInZoomControls(true);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnToMyImageview: // Просмотр картинки в ImageView с реализацией multitouch
                if (imageCameraBtmp != null) {
                    intentMultitouch = new Intent(this, MyImageViewActivity.class);
                    intentMultitouch.putExtra(IMAGE_CAMERA_MULTITOUCH, imageCameraBtmp);
                    startActivity(intentMultitouch);
                } else {
                    intentMultitouch = new Intent(this, MyImageViewActivity.class);
                    intentMultitouch.putExtra(IMAGE_GALLERY_MULTITOUCH, imageGalleryUri);
                    startActivity(intentMultitouch);
                }
                break;
        }
    }
}
