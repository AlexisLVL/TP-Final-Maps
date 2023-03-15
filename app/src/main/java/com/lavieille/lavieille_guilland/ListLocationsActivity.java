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
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

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

public class ListLocationsActivity extends AppCompatActivity {
    public ArrayList<Location> arrayOfLocations = new ArrayList<>();
    public LocationsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_locations);

        Intent intent = new Intent(this, MainActivity.class);

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
                (adapterView, view, i, l) -> {
                    Location location = new Location(
                            ((String) ((TextView) view.findViewById(R.id.title)).getText()),
                            ((String) ((TextView) view.findViewById(R.id.address)).getText()),
                            ((String) ((TextView) view.findViewById(R.id.coordinates)).getText()),
                            ((String) ((TextView) view.findViewById(R.id.description)).getText()),
                            ((String) ((TextView) view.findViewById(R.id.note)).getText())
                    );
                    intent.putExtra("location", location);
                    startActivity(intent);
                    System.out.println("Short Click");
                });

        listView.setOnItemLongClickListener(
                (adapterView, view, i, l) -> {
                    System.out.println("long Click");
                    return false;
                }
        );

        backButton.setOnClickListener(view -> {
            Intent intentMapActivity = new Intent(this, MapActivity.class);
            startActivity(intentMapActivity);
        });
    }

    private ArrayList<LinkedTreeMap<String, String>> makeIntoTable(String json){
        return new Gson().fromJson(json, ArrayList.class);
    }
}