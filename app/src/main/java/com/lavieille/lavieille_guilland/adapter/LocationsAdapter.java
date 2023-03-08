package com.lavieille.lavieille_guilland.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.lavieille.lavieille_guilland.R;
import com.lavieille.lavieille_guilland.entity.Location;

import java.util.ArrayList;

public class LocationsAdapter extends ArrayAdapter<Location> {

    public LocationsAdapter(Context context, ArrayList<Location> locations) {
        super(context, 0, locations);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Location location = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_locations, parent, false);
        }
        // Lookup view for data population
        if (location != null) {
            TextView title = convertView.findViewById(R.id.title);
            TextView address = convertView.findViewById(R.id.address);
            TextView coordinates = convertView.findViewById(R.id.coordinates);
            TextView description = convertView.findViewById(R.id.description);
            TextView note = convertView.findViewById(R.id.note);
            // Populate the data into the template view using the data object

            String gap = " ";
            title.setText(location.getTitle() +  gap);
            address.setText(location.getAddress() +  gap);
            coordinates.setText(location.getCoordinates() +  gap);
            description.setText(location.getDescription() +  gap);
            note.setText(location.getNote() +  gap);
        }
        // Return the completed view to render on screen
        return convertView;
    }
}
