package com.example.sergey.testtask.helpers;

import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SharedPreferencesHelper {

    private final static String MAIN_LANGUAGE = "selected_language", SERVICE_WORKING = "serviceWork";
    private SharedPreferences settingPrefs;
    private SharedPreferences.Editor editSettingPrefs;

    public SharedPreferencesHelper(Activity ctx) {
        settingPrefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        editSettingPrefs = settingPrefs.edit();
    }

    public String getLanguage() {
        return settingPrefs.getString(MAIN_LANGUAGE, "");
    }

    public void setServiceWorkedTRUE() {
        editSettingPrefs.putBoolean(SERVICE_WORKING, true);
        editSettingPrefs.commit();
    }

    public void setServiceWorkedFALSE() {
        editSettingPrefs.putBoolean(SERVICE_WORKING, false);
        editSettingPrefs.commit();
    }

    public boolean getServiceWorked() {
        return settingPrefs.getBoolean(SERVICE_WORKING, false);
    }
}
