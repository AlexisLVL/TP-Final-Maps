package com.lavieille.lavieille_guilland;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.lavieille.lavieille_guilland.entity.Location;

import java.util.ArrayList;

public class MapActivity extends AppCompatActivity {
    private static final int REQUEST_CODE = 1;
    private MapView mapView;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);

        mapView.getMapAsync(map -> {
            Intent responseIntent = getIntent();
            ArrayList<Location> arrayOfLocations = (ArrayList<Location>) responseIntent.getSerializableExtra("arrayOfLocations");

            for (Location location :
                    arrayOfLocations) {
                String[] latLng = location.getCoordinates().split(", ");
                LatLng park = new LatLng(Double.parseDouble(latLng[0]), Double.parseDouble(latLng[1]));
                map.addMarker(new MarkerOptions().position(park).title("Marker in " + location.getTitle()));
            }

            LatLng lyon = new LatLng(45.7640, 4.8357);
            map.addMarker(new MarkerOptions().position(lyon).title("Marker in lyon"));
            // Déplacer la caméra pour que le marqueur soit centré sur la carte
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(lyon, 12));
        });


        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.navigation_map);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    Intent intentPerform = new Intent(MapActivity.this, Perform.class);
                    finish();
                    startActivity(intentPerform);
                    return true;

                case R.id.navigation_map:
                    finish();
                    return true;

                case R.id.navigation_logout:
                    Intent intentLogIn = new Intent(this, LandingActivity.class);
                    intentLogIn.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intentLogIn);
                    return true;
            }
            return false;
        });

        findViewById(R.id.BackButton).setOnClickListener(view -> {
            finish();
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
}
