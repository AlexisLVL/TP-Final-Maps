package com.lavieille.lavieille_guilland;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Perform extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perform);


        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        // Gérer l'événement de sélection pour l'élément Home
                        return true;
                    case R.id.navigation_map:
                        Intent intentListView = new Intent(Perform.this, ListLocationsActivity.class);
                        startActivity(intentListView);
                        return true;
                    case R.id.navigation_settings:
                        return true;
                    case R.id.navigation_logout:
                        Intent intentLogIn = new Intent(Perform.this, LandingActivity.class);
                        startActivity(intentLogIn);
                        return true;
                }
                return false;
            }
        });
        bottomNavigationView.setSelectedItemId(R.id.navigation_home);
    }
}