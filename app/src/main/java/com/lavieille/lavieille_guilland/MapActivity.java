package com.lavieille.lavieille_guilland;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.lavieille.lavieille_guilland.entity.Location;

import java.util.ArrayList;

public class MapActivity extends AppCompatActivity {
    private static final int REQUEST_CODE = 1;
    private GoogleMap googleMap;
    private MapView mapView;
    private Button viewLikedPlace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);


        viewLikedPlace = findViewById(R.id.viewLikedPlaceButton);
        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);

        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap map) {

                googleMap = map;

                Intent responseIntent = getIntent();
                ArrayList<Location> arrayOfLocations = (ArrayList<Location>) responseIntent.getSerializableExtra("arrayOfLocations");

                for (Location location:
                     arrayOfLocations) {
                    String[] latLng = location.getCoordinates().split(", ");
                    LatLng park = new LatLng(Double.parseDouble(latLng[0]), Double.parseDouble(latLng[1]));
                    map.addMarker(new MarkerOptions().position(park).title("Marker in " + location.getTitle()));
                }

                LatLng lyon = new LatLng(45.7640, 4.8357);
                map.addMarker(new MarkerOptions().position(lyon).title("Marker in lyon"));
                // Déplacer la caméra pour que le marqueur soit centré sur la carte
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(lyon, 12));
            }
        });
        viewLikedPlace.setOnClickListener(view -> {
            Intent intentListLocations = new Intent(MapActivity.this, ListLocationsActivity.class);
            startActivityForResult(intentListLocations, REQUEST_CODE);
        });


        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        // Gérer l'événement de sélection pour l'élément Home
                        return true;
                    case R.id.navigation_map:
                        // Gérer l'événement de sélection pour l'élément Search
                        return true;
                    case R.id.navigation_settings:
                        Intent intentRegisterActivity = new Intent(MapActivity.this, LandingActivity.class);
                        startActivity(intentRegisterActivity);
                        return true;
                    case R.id.navigation_logout:
                        Intent intentLogIn = new Intent(MapActivity.this, RegisterActivity.class);
                        startActivity(intentLogIn);
                        return true;
                }
                return false;
            }
        });

        bottomNavigationView.setSelectedItemId(R.id.navigation_map);
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