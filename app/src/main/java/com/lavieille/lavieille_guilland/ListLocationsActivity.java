package com.lavieille.lavieille_guilland;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import com.lavieille.lavieille_guilland.adapter.LocationsAdapter;
import com.lavieille.lavieille_guilland.entity.Location;
import com.lavieille.lavieille_guilland.utils.DBUsers;
import com.lavieille.lavieille_guilland.utils.signin.FirebaseConnection;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ListLocationsActivity extends AppCompatActivity {
    public ArrayList<Location> arrayOfLocations = new ArrayList<>();
    public LocationsAdapter adapter;
    private ImageButton mapButton;

    @SuppressLint({"NonConstantResourceId", "ResourceAsColor"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_locations);

        mapButton = findViewById(R.id.MapButton);

        Intent intent = new Intent(this, LocationDetailActivity.class);

        ImageButton mapButton = findViewById(R.id.MapButton);

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

                    ArrayList<String> favorites = new DBUsers().getFavorites(FirebaseConnection.getUser().getUid());
                    for (String favorite : favorites) {
                        setFavorite(true, listView.getChildAt(Integer.parseInt(favorite)));
                    }
                }
            } catch (Exception e) {
                Log.e("Error : connexion", e.getLocalizedMessage());
            }
        });

        listView.setOnItemClickListener(
                (adapterView, view, i, l) -> {
                    Location location = null;
                    for (Location locationInArray:
                         arrayOfLocations) {
                        if (Objects.equals((String) ((TextView) view.findViewById(R.id.title)).getText(), locationInArray.getTitle())){
                            location = locationInArray;
                        }
                    }
                    intent.putExtra("location", location);
                    startActivity(intent);
                }
        );

        listView.setOnItemLongClickListener(
                (adapterView, view, i, l) -> {
                    Executors.newSingleThreadExecutor().execute(() -> {
                        DBUsers db = new DBUsers();
                        if (db.isFavorite(FirebaseConnection.getUser().getUid(), String.valueOf(i))) {
                            db.removeFavorite(FirebaseConnection.getUser().getUid(), String.valueOf(i));
                            setFavorite(false, view);
                        } else {
                            db.addFavorite(FirebaseConnection.getUser().getUid(), String.valueOf(i));
                            setFavorite(true, view);
                        }
                    });
                    return true;
                }
        );

        mapButton.setOnClickListener(view -> {
            Intent intentMapActivity = new Intent(this, MapActivity.class);
            intentMapActivity.putExtra("arrayOfLocations", arrayOfLocations);
            startActivity(intentMapActivity);
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.navigation_map);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    Intent intentPerform = new Intent(ListLocationsActivity.this, Perform.class);
                    finish();
                    startActivity(intentPerform);
                     return true;

                case R.id.navigation_map:
                    return true;

                case R.id.navigation_logout:
                    Intent intentLogIn = new Intent(ListLocationsActivity.this, LandingActivity.class);
                    finish();
                    startActivity(intentLogIn);
                    return true;
            }
            return false;
        });
    }

    private ArrayList<LinkedTreeMap<String, String>> makeIntoTable(String json){
        return new Gson().fromJson(json, new TypeToken<ArrayList<LinkedTreeMap<String, String>>>(){}.getType());
    }

    private void setFavorite(boolean state, View view) {
        if (state) {
            view.findViewById(R.id.item).setBackgroundColor(getResources().getColor(R.color.orange_light));
        } else {
            view.findViewById(R.id.item).setBackgroundColor(getResources().getColor(R.color.white));
        }
    }
}