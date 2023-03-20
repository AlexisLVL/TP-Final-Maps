package com.lavieille.lavieille_guilland;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

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
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.Executors;

public class ListLocationsActivity extends AppCompatActivity {
    private final ArrayList<Location> arrayOfLocations = new ArrayList<>();
    private LocationsAdapter adapter;
    private ListView listView;

    @SuppressLint({"NonConstantResourceId", "ResourceAsColor"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_locations);

        // Initialize the list view
        listView = findViewById(R.id.lvLocations);
        adapter = new LocationsAdapter(this, arrayOfLocations);
        listView.setAdapter(adapter);

        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                HttpURLConnection connection = (HttpURLConnection) new URL("http://tp3.hexteckgate.ga/api.php").openConnection();

                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK){
                    BufferedReader input = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String jsonData = input.readLine();
                    input.close();

                    ArrayList<LinkedTreeMap<String, String>> datas = convertIntoArray(jsonData);

                    for (LinkedTreeMap<String, String> data : datas) {
                        arrayOfLocations.add(
                                new Location(
                                        data.get("title"), data.get("address"),
                                        data.get("coordinates"), data.get("description"),
                                        data.get("note")
                                )
                        );
                    }
                    runOnUiThread(() -> {
                        adapter.notifyDataSetChanged();
                    });

                    colorFavorites();
                }
            } catch (Exception apiError) {
                System.out.println(apiError.getMessage());
                apiError.printStackTrace();
            }
        });

        // Handle simple click on items to show details about it
        listView.setOnItemClickListener(
                (adapterView, view, i, l) -> {
                    Location location = null;
                    for (Location locationInArray: arrayOfLocations) {
                        if (Objects.equals(((TextView) view.findViewById(R.id.title)).getText(), locationInArray.getTitle())){
                            location = locationInArray;
                        }
                    }
                    Intent intent = new Intent(this, LocationDetailActivity.class)
                            .putExtra("location", location);
                    startActivity(intent);
                }
        );

        // Handle long click on items to add it to favorites
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

        // Open the map activity
        findViewById(R.id.MapButton).setOnClickListener(view -> {
            Intent intentMapActivity = new Intent(this, MapActivity.class)
                    .putExtra("arrayOfLocations", arrayOfLocations);
            startActivity(intentMapActivity);
        });

        // Handle clicks on the navigation bar
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setSelectedItemId(R.id.navigation_map);
        bottomNav.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    Intent intentPerform = new Intent(ListLocationsActivity.this, Perform.class);
                    startActivity(intentPerform);
                    return true;

                case R.id.navigation_map:
                    return true;

                case R.id.navigation_logout:
                    Intent intentLogIn = new Intent(this, LandingActivity.class)
                            .addFlags(
                                    Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK
                            );
                    startActivity(intentLogIn);
                    return true;
            }
            return false;
        });
    }

    /**
     * Convert the data got from the API
     * @param json The datas
     * @return ArrayList<LinkedTreeMap<String, String>>
     */
    private ArrayList<LinkedTreeMap<String, String>> convertIntoArray(String json){
        return new Gson().fromJson(json, new TypeToken<ArrayList<LinkedTreeMap<String, String>>>(){}.getType());
    }

    /**
     * Used to change the background color of a list item
     * @param state True if the item is a favorite, false otherwise
     * @param view The view to apply the background
     */
    private void setFavorite(boolean state, View view) {
        if (state) {
            view.findViewById(R.id.item).setBackgroundColor(
                    getResources().getColor(R.color.orange_light)
            );
        } else {
            view.findViewById(R.id.item).setBackgroundColor(
                    getResources().getColor(R.color.white)
            );
        }
    }

    private void colorFavorites() {
        // Color the background of favorites cards in the list
        ArrayList<String> favorites = new DBUsers().getFavorites(FirebaseConnection.getUser().getUid());

        for (String favorite : favorites) {
            try {
                runOnUiThread(() -> {
                    setFavorite(true, listView.getChildAt(Integer.parseInt(favorite)));
                });
            }catch (Exception e) {
                throw e;
            }
        }
    }
}