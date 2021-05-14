package com.openclassrooms.mareu.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.openclassrooms.mareu.R;
import com.openclassrooms.mareu.entities.Place;

import java.util.List;

public class PlaceArrayAdapter extends ArrayAdapter<Place> {

    private List<Place> places;


    public PlaceArrayAdapter(@NonNull Context context, int resource, @NonNull List<Place> places) {
        super(context, resource);
        this.places = places;
    }

    @Nullable
    @Override
    public Place getItem(int position) {
        return this.places.get(position);
    }

    @Override
    public int getCount() {
        return this.places.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Place place = this.getItem(position);
        LayoutInflater inflater = LayoutInflater.from(this.getContext());
        View itemView = inflater.inflate(R.layout.fragment_place_list_item, null);
        ((TextView) itemView.findViewById(R.id.item_place_name)).setText(place.getName());
        return itemView;
    }
}
