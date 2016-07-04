package com.example.sergey.testtask.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.sergey.testtask.R;
import com.example.sergey.testtask.helpers.SharedPreferencesHelper;

import java.util.Locale;

public class SplashActivity extends AppCompatActivity {

    private Handler splashHandler;
    private final static int delayTime = 3000;
    private SharedPreferencesHelper prefs;
    private Locale locale;
    private final static String RUSSIAN = "Русский", LOCALE_RU = "ru", LOCALE_EN = "en";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        prefs = new SharedPreferencesHelper(this);
        if (prefs.getLanguage().contains(RUSSIAN)) {
            locale = new Locale(LOCALE_RU);
        } else {
            locale = new Locale(LOCALE_EN);
        }
        Configuration configuration = getResources().getConfiguration();
        configuration.locale = locale;
        getResources().updateConfiguration(configuration, getResources().getDisplayMetrics());

        splashHandler = new Handler();
        splashHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intentMainAct = new Intent (SplashActivity.this, MainActivity.class);
                startActivity(intentMainAct);
                finish();
            }
        }, delayTime);
    }
}
