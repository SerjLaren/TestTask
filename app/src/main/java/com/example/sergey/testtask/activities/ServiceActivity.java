package com.example.sergey.testtask.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.content.LocalBroadcastManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sergey.testtask.R;
import com.example.sergey.testtask.helpers.SharedPreferencesHelper;
import com.example.sergey.testtask.services.GetXmlService;

public class ServiceActivity extends BaseActivity implements View.OnClickListener {

    private ProgressBar loadingProgress;
    private TextView tvXML;
    private String xmlData, noConnStr;
    private boolean serviceWorked = true;
    private SharedPreferencesHelper prefs;
    private BroadcastReceiver br;
    private Button btnRestart;
    private Toast toast;
    public final static String CONNECTION_TO_XMLSERVICE = "com.example.sergey.testtask.GetXmlService";
    private final static String XML_DATA = "xml_data", SAVED_DATA = "saved_data", XML_LOADED = "xml_loaded";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service);
        initReciever();
        initViews();
        initValues();
        if (hasConnection(this)) {
            btnRestart.setVisibility(View.INVISIBLE);
            serviceWorked = prefs.getServiceWorked();
            if (!serviceWorked) { // запускаем сервис только при "старте активности", а не при повороте экрана
                startService(new Intent(ServiceActivity.this, GetXmlService.class));
                prefs.setServiceWorkedTRUE();
            }
        } else {
            toast = Toast.makeText(getApplicationContext(),
                    noConnStr, Toast.LENGTH_SHORT);
            toast.show();
            btnRestart.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onBackPressed() {
        stopService(new Intent(ServiceActivity.this, GetXmlService.class));
        tvXML = (TextView) findViewById(R.id.tvXMLData);
        tvXML.setText("");
        super.onBackPressed();
    }

    @Override
    void initValues() {
        noConnStr = getString(R.string.noConnStr);
        prefs = new SharedPreferencesHelper(this);
        IntentFilter intentFilt = new IntentFilter(CONNECTION_TO_XMLSERVICE);
        LocalBroadcastManager.getInstance(this).registerReceiver(br, intentFilt);
    }

    @Override
    void initViews() {
        tvXML = (TextView) findViewById(R.id.tvXMLData);
        btnRestart = (Button) findViewById(R.id.btnRestartService);
        btnRestart.setOnClickListener(this);
        loadingProgress = (ProgressBar) findViewById(R.id.topProgressBar);
        loadingProgress.setVisibility(View.VISIBLE);
    }

    private void initReciever() {
        br = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) { // действия при получении данных от сервиса
                xmlData = intent.getStringExtra(XML_DATA);
                tvXML.setText(xmlData);
                boolean xmlLoaded = intent.getBooleanExtra(XML_LOADED, false);
                if (xmlLoaded) {
                    loadingProgress.setVisibility(View.INVISIBLE);
                }
            }
        };
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putString(SAVED_DATA, tvXML.getText().toString()); // сохраняем состояние списка при повороте экрана
        prefs.setServiceWorkedTRUE();
        super.onSaveInstanceState(savedInstanceState);
    }

    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        prefs.setServiceWorkedTRUE();
        xmlData = savedInstanceState.getString(SAVED_DATA);
        tvXML.setText(xmlData);
    }

    public static boolean hasConnection(final Context context)
    {
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (wifiInfo != null && wifiInfo.isConnected()) {
            return true;
        }
        wifiInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (wifiInfo != null && wifiInfo.isConnected()) {
            return true;
        }
        wifiInfo = cm.getActiveNetworkInfo();
        if (wifiInfo != null && wifiInfo.isConnected()) {
            return true;
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnRestartService:
                if (hasConnection(this)) {
                    prefs.setServiceWorkedFALSE();
                    serviceWorked = prefs.getServiceWorked();
                    if (!serviceWorked) { // запускаем сервис при включении интернет - соединения
                        startService(new Intent(ServiceActivity.this, GetXmlService.class));
                        prefs.setServiceWorkedTRUE();
                        btnRestart.setVisibility(View.INVISIBLE);
                    }
                } else {
                    btnRestart.setVisibility(View.VISIBLE);
                    toast = Toast.makeText(getApplicationContext(),
                            noConnStr, Toast.LENGTH_SHORT);
                    toast.show();
                }
                break;
        }
    }
}
