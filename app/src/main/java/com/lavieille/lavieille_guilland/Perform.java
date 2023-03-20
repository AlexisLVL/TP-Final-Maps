package com.lavieille.lavieille_guilland;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.lavieille.lavieille_guilland.utils.DBUsers;
import com.lavieille.lavieille_guilland.utils.PerformData;
import com.lavieille.lavieille_guilland.utils.signin.FirebaseConnection;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.Executors;

public class Perform extends AppCompatActivity {

    private static final int PERMISSIONS_REQUEST_LOCATION = 1;
    private CardView gymIcon;
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationCallback mLocationCallback;
    private Location mLastLocation;
    private double mTotalDistance = Double.NaN;
    private Instant mCurrentDate;
    private TextView mDistanceTextView, kmStepText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perform);

        mDistanceTextView = findViewById(R.id.kmRunText);
        kmStepText = findViewById(R.id.kmStepText);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                if (Double.isNaN(mTotalDistance)) return;

                for (Location location : locationResult.getLocations()) {
                    if (mLastLocation == null) {
                        mLastLocation = location;
                    }
                    if (isSameDay(mCurrentDate, Instant.now())) {
                        float distance = location.distanceTo(mLastLocation);
                        mTotalDistance += distance;
                        kmStepText.setText(String.format(Locale.getDefault(), "%.0f pas", mTotalDistance / 0.75));
                        mDistanceTextView.setText(String.format(Locale.getDefault(), "%.3f km", mTotalDistance / 1000));
                        mLastLocation = location;
                    } else {
                        mCurrentDate = Instant.now();
                        mTotalDistance = 0;
                        mDistanceTextView.setText(String.format(Locale.getDefault(), "%.2f km", mTotalDistance / 1000));
                    }
                }
            }
        };

        Executors.newSingleThreadExecutor().execute(() -> {
            DBUsers db = new DBUsers();

            PerformData perform = db.getPerform(FirebaseConnection.getUser().getUid());

            if (perform == null) {
                mTotalDistance = 0;
                mCurrentDate = Instant.now();
            } else {
                mTotalDistance = perform.getDistance();
                mCurrentDate = perform.getDate();
            }
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    // Gérer l'événement de sélection pour l'élément Home
                    return true;
                case R.id.navigation_map:
                    Intent intentListView = new Intent(Perform.this, ListLocationsActivity.class);
                    finish();
                    startActivity(intentListView);
                    return true;
                case R.id.navigation_settings:
                    return true;
                case R.id.navigation_logout:
                    Intent intentLogIn = new Intent(Perform.this, LandingActivity.class);
                    finish();
                    startActivity(intentLogIn);
                    return true;
            }
            return false;
        });
        bottomNavigationView.setSelectedItemId(R.id.navigation_home);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mFusedLocationClient.requestLocationUpdates(createLocationRequest(), mLocationCallback, Looper.getMainLooper());
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_LOCATION);
        }
    }
    @Override
    protected void onPause() {
        super.onPause();
        mFusedLocationClient.removeLocationUpdates(mLocationCallback);
    }

    private boolean isSameDay(Instant date1, Instant date2) {
        System.out.println(date1.compareTo(date2));
        System.out.println(date1.);
        //SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
        return true;
    }

    private static LocationRequest createLocationRequest() {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(1000);
        locationRequest.setFastestInterval(500);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        return locationRequest;
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSIONS_REQUEST_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mFusedLocationClient.requestLocationUpdates(createLocationRequest(), mLocationCallback, Looper.getMainLooper());
            } else {
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mFusedLocationClient.requestLocationUpdates(createLocationRequest(), mLocationCallback, Looper.getMainLooper());
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_LOCATION);
        }
    }

    private void checkLocationEnabled() {
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        boolean gpsEnabled = false;
        boolean networkEnabled = false;
        try {
            gpsEnabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        try {
            networkEnabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        if (!gpsEnabled && !networkEnabled) {
            Toast.makeText(this, "Please enable location services", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        Executors.newSingleThreadExecutor().execute(() -> {
            DBUsers db = new DBUsers();

            db.setPerform(
                    FirebaseConnection.getUser().getUid(),
                    mTotalDistance,
                    Instant.now().toString()
            );
        });

        super.onDestroy();
    }
}
