package com.lavieille.lavieille_guilland;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;

import com.lavieille.lavieille_guilland.entity.Location;

public class LocationDetailActivity extends AppCompatActivity {
    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent responseIntent = getIntent();
        Location location = (Location) responseIntent.getSerializableExtra("location");

        ((TextView) findViewById(R.id.title)).setText(location.getTitle());
        ((TextView) findViewById(R.id.address)).setText(location.getAddress());
        ((TextView) findViewById(R.id.coordinates)).setText(location.getCoordinates());
        ((TextView) findViewById(R.id.description)).setText(location.getDescription());
        ((TextView) findViewById(R.id.note)).setText(String.format("Note %s/10", location.getNote()));
    }
}
