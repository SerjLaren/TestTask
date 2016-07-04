package com.example.sergey.testtask.activities;

import android.os.Bundle;
import android.widget.Toast;

import com.example.sergey.testtask.R;
import com.example.sergey.testtask.fragments.SettingsFragment;

public class SettingsActivity extends BaseActivity {

    private SettingsFragment fragSettings;
    private Toast toast;
    private String settingsInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        initValues();
        initViews();
    }

    @Override
    void initValues() {
        settingsInfo = getString(R.string.settingsInfo);
    }

    @Override
    void initViews() {
        toast = Toast.makeText(getApplicationContext(),
                settingsInfo, Toast.LENGTH_LONG);
        toast.show();
        fragSettings = new SettingsFragment(); // Фрагмент с настройками
        getFragmentManager().beginTransaction().replace(R.id.fragSettings, fragSettings).commit();
    }
}
