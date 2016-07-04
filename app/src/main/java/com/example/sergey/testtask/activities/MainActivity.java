package com.example.sergey.testtask.activities;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.example.sergey.testtask.R;
import com.example.sergey.testtask.helpers.SharedPreferencesHelper;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    private Resources myResources;
    private String[] displaysArr;
    private ArrayAdapter myArrAdapter;
    private int positionSpinner;
    private Button btnGoToDisplay;
    private Spinner spinnerSetDisplay;
    private Intent intentToDisplay, intentSettings;
    private SharedPreferencesHelper prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initValues();
        initViews();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.mainSettings:
                intentSettings = new Intent(this, SettingsActivity.class);
                startActivity(intentSettings);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    void initValues() {
        positionSpinner = 0;
        myResources = getResources();
        displaysArr = myResources.getStringArray(R.array.displays_array);
        myArrAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, displaysArr);
        myArrAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        prefs = new SharedPreferencesHelper(this);
    }

    @Override
    void initViews() {
        btnGoToDisplay = (Button) findViewById(R.id.btnGotoDisplay);
        btnGoToDisplay.setOnClickListener(this);
        spinnerSetDisplay = (Spinner) findViewById(R.id.spinnerSetDisplay);
        spinnerSetDisplay.setAdapter(myArrAdapter);
        spinnerSetDisplay.setSelection(positionSpinner);
        spinnerSetDisplay.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                positionSpinner = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnGotoDisplay:
                switch (positionSpinner) {
                    case 0: // List
                        intentToDisplay = new Intent(this, ListActivity.class);
                        startActivity(intentToDisplay);
                        break;
                    case 1: // Scaling
                        intentToDisplay = new Intent(this, ScalingActivity.class);
                        startActivity(intentToDisplay);
                        break;
                    case 2: // Service
                        prefs.setServiceWorkedFALSE();
                        intentToDisplay = new Intent(this, ServiceActivity.class);
                        startActivity(intentToDisplay);
                        break;
                    case 3: // Map
                        intentToDisplay = new Intent(this, MapActivity.class);
                        startActivity(intentToDisplay);
                        break;
                }
                break;
        }
    }
}
