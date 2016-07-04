package com.example.sergey.testtask.activities;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sergey.testtask.R;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapActivity extends BaseActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks {

    private GoogleMap googleMap;
    private MapFragment mapFrag;
    private LatLng myLocation;
    private String myLatitude, myLongitude, titleLatitude, titleLongitude, noGpsStr;
    private int myZoom = 12;
    private Location mLastLocation;
    private GoogleApiClient googleClient;
    private LocationManager locManager;
    private TextView tvCoords;
    private Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        initViews();
        initValues();
        locManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        boolean gpsEnabled = locManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!gpsEnabled) {
            toast = Toast.makeText(getApplicationContext(),
                    noGpsStr, Toast.LENGTH_LONG);
            toast.show();
        }
    }

    @Override
    void initValues() {
        titleLatitude = getString(R.string.titleLatitude);
        titleLongitude = getString(R.string.titleLongitude);
        noGpsStr = getString(R.string.noGpsStr);
        googleClient = new GoogleApiClient.Builder(this).addConnectionCallbacks(this).addApi(LocationServices.API).build();
    }

    @Override
    void initViews() {
        tvCoords = (TextView) findViewById(R.id.tvCoords);
        mapFrag = ((MapFragment) getFragmentManager().findFragmentById(R.id.mapViewFrag));
        mapFrag.getMapAsync(this);
    }

    protected void onStart() {
        googleClient.connect();
        super.onStart();
    }

    protected void onStop() {
        googleClient.disconnect();
        super.onStop();
    }

    @Override
    public void onMapReady(GoogleMap map) {
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        this.googleMap = map;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        try {
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(googleClient);
        } catch (SecurityException e) {

        }
        if (mLastLocation != null) {
            myLatitude = String.valueOf(mLastLocation.getLatitude());
            myLongitude = String.valueOf(mLastLocation.getLongitude());
            tvCoords.setText(titleLatitude + myLatitude + ";  " + titleLongitude + myLongitude);
            myLocation = new LatLng(Double.valueOf(mLastLocation.getLatitude()), Double.valueOf(mLastLocation.getLongitude()));
            googleMap.addMarker(new MarkerOptions().position(myLocation).title(String.valueOf(mLastLocation.getLatitude()) +
                    " " + String.valueOf(mLastLocation.getLongitude())));
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, myZoom));
        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }
}
