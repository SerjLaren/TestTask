package com.example.sergey.testtask.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.sergey.testtask.R;

abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
    }

    abstract void initValues();

    abstract void initViews();
}
