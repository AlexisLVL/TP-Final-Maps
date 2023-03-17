package com.lavieille.lavieille_guilland;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.lavieille.lavieille_guilland.adapter.LocationsAdapter;
import com.lavieille.lavieille_guilland.entity.Location;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {
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
        ((TextView) findViewById(R.id.note)).setText("Note " + location.getNote() +  "/10");

        System.out.println();

    }
}
