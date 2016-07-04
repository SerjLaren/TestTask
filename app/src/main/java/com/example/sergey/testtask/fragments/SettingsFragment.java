package com.example.sergey.testtask.fragments;

import android.preference.PreferenceFragment;
import android.os.Bundle;

import com.example.sergey.testtask.R;

public class SettingsFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.main_settings);
    }
}
