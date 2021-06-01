package com.openclassrooms.mareu.ui;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;

import com.openclassrooms.mareu.R;
import com.openclassrooms.mareu.adapters.PlaceArrayAdapter;
import com.openclassrooms.mareu.entities.Place;
import com.openclassrooms.mareu.events.SearchByPlaceEvent;
import com.openclassrooms.mareu.viewmodels.SearchViewModel;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private SearchViewModel searchViewModel;

    @BindView(R.id.reunion_place_spinner)
    AutoCompleteTextView placeSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        ActionBar actionBar = this.getSupportActionBar(); // To call the action bar
        actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_24); // To customize the back button (optional)
        actionBar.setDisplayHomeAsUpEnabled(true); // to show the back button in action bar; implemented by Android

        this.searchViewModel = new ViewModelProvider(this).get(SearchViewModel.class);

        ButterKnife.bind(this);

        this.placeSpinner.setOnItemClickListener(this);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        this.searchViewModel.getAllPlaces().observe(this, allPlaces -> {
            this.placeSpinner.setAdapter(new PlaceArrayAdapter(this, 0, allPlaces));
            this.placeSpinner.setText(allPlaces.get(0).getName());
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //EventBus.getDefault().post(new SearchByPlaceEvent((Place) placeSpinner.getAdapter().getItem(position)));
        this.searchViewModel.searchReunionByPlace((Place) placeSpinner.getAdapter().getItem(position));

    }
}