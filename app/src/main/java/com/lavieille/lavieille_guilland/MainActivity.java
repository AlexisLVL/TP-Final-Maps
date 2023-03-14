package com.lavieille.lavieille_guilland;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

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
    public ArrayList<Location> arrayOfLocations = new ArrayList<>();
    public LocationsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_locations);

        Button backButton = findViewById(R.id.BackButton);
        Button mapButton = findViewById(R.id.MapButton);

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        ListView listView = findViewById(R.id.lvLocations);
        adapter = new LocationsAdapter(this, arrayOfLocations);
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

                    handler.post(() -> {
                        for (LinkedTreeMap<String, String> tmp :
                        jsonDataTable) {
                            Location newLocation = new Location(tmp.get("title"), tmp.get("address"), tmp.get("coordinates"), tmp.get("description"),tmp.get("note"));
                            arrayOfLocations.add(newLocation);
                            adapter.notifyDataSetChanged();
                        }
                    });
                }
            } catch (Exception e) {
                Log.e("Error : connexion", e.getLocalizedMessage());
            }
        });

        listView.setOnItemClickListener(
            new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    System.out.println("Short Click");
                }
        });

        listView.setOnItemLongClickListener(
            new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                    System.out.println("long Click");
                    return false;
                }
            }
        );

        backButton.setOnClickListener(view -> {
            //Intent intent = new Intent(this, MainActivity.class);
            //startActivity(intent);
        });
    }

    private ArrayList<LinkedTreeMap<String, String>> makeIntoTable(String json){
        return new Gson().fromJson(json, ArrayList.class);
    }
}
