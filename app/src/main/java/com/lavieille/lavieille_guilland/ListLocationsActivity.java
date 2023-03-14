package com.lavieille.lavieille_guilland;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;

import com.lavieille.lavieille_guilland.adapter.LocationsAdapter;
import com.lavieille.lavieille_guilland.entity.Location;

import java.util.ArrayList;

public class ListLocationsActivity extends AppCompatActivity {

    public ArrayList<Location> arrayOfLocations = new ArrayList<Location>();
    public LocationsAdapter adapter;

    ActivityResultLauncher<Intent> mainActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK){
                    Intent data = result.getData();
                    if (data != null){
                        Location newLocation = (Location) data.getSerializableExtra("rep");
                        arrayOfLocations.add(newLocation);
                        adapter.notifyDataSetChanged();
                    }
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_locations);

        Intent intent = new Intent(this, MainActivity.class);
        final Button button = findViewById(R.id.button);
        adapter = new LocationsAdapter(this, arrayOfLocations);

        ListView listView = findViewById(R.id.lvLocations);
        listView.setAdapter(adapter);

        button.setOnClickListener(view -> {
            mainActivityResultLauncher.launch(intent);
        });
    }
}