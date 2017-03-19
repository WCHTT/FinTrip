package com.irene.fintrip.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.irene.fintrip.R;
import com.irene.fintrip.models.Trip;

import java.util.ArrayList;

/**
 * Created by Irene on 2017/3/19.
 */

public class TripListAdapter extends ArrayAdapter<Trip> {

    public TripListAdapter(Context context, ArrayList<Trip> items) {
        super(context, 0, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Trip item = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_trip, parent, false);
        }
        // Lookup view for data population
        TextView tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
        // Populate the data into the template view using the data object
        tvTitle.setText(item.getListName());
        // Return the completed view to render on screen
        return convertView;
    }
}
