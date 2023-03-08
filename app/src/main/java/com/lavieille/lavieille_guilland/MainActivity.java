package com.lavieille.lavieille_guilland;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Button;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.lavieille.lavieille_guilland.adapter.LocationsAdapter;
import com.lavieille.lavieille_guilland.entity.Location;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {
    public ArrayList<Location> arrayOfLocations = new ArrayList<Location>();
    public LocationsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_locations);

        adapter = new LocationsAdapter(this, arrayOfLocations);

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        ListView listView = findViewById(R.id.lvLocations);
        listView.setAdapter(adapter);

        executor.execute(() -> {
            try {
                URL url = new URL   ("http://tp3.hexteckgate.ga/api.php");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK){
                    BufferedReader input = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String jsonData = input.readLine();
                    input.close();

                    ArrayList<LinkedTreeMap<String, String>> jsonDataTable = makeIntoTable(jsonData);
                    System.out.println(jsonDataTable);
                    handler.post(() -> {
                        for (LinkedTreeMap<String, String> tmp :
                        jsonDataTable) {
                            Location newLocation = new Location(tmp.get("title"), tmp.get("address"), tmp.get("coordinates"), tmp.get("description"),tmp.get("note"));
                            arrayOfLocations.add(newLocation);
                        }
                        adapter.notifyDataSetChanged();
                    });
                }
            } catch (Exception e) {
                Log.e("Error : connexion", e.getLocalizedMessage());
            }
        });
    }

    private ArrayList<LinkedTreeMap<String, String>> makeIntoTable(String json){
        return new Gson().fromJson(json, ArrayList.class);
    }
}