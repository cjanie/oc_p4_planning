package com.openclassrooms.mareu.app.ui;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.openclassrooms.mareu.R;
import com.openclassrooms.mareu.app.adapters.PlaceArrayAdapter;
import com.openclassrooms.mareu.data.entities.Place;
import com.openclassrooms.mareu.domain.events.InitSearchEvent;
import com.openclassrooms.mareu.domain.events.SearchByDateEvent;
import com.openclassrooms.mareu.domain.events.SearchByPlaceAndDateEvent;
import com.openclassrooms.mareu.domain.events.SearchByPlaceEvent;
import com.openclassrooms.mareu.app.utils.CustomDateTimeFormatter;
import com.openclassrooms.mareu.domain.viewmodels.SearchViewModel;

import org.greenrobot.eventbus.EventBus;

import java.time.LocalDate;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchFragment extends Fragment implements AdapterView.OnItemClickListener, View.OnClickListener {

    private SearchViewModel searchViewModel;

    @BindView(R.id.reunion_place_spinner)
    AutoCompleteTextView placeSpinner;
    @BindView(R.id.reunion_start_layout)
    TextInputLayout dateLayout;
    @BindView(R.id.reunion_start)
    TextInputEditText selectedDate;
    @BindView(R.id.search_by_place_clear_button)
    ImageView resetPlaceButton;
    @BindView(R.id.search_by_date_clear_button)
    ImageView resetDateButton;
    @BindView(R.id.search_clear_all_button)
    ImageView resetButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_search, container, false);

        this.searchViewModel = new ViewModelProvider(this).get(SearchViewModel.class);

        ButterKnife.bind(this, root);
        this.placeSpinner.setOnItemClickListener(this);
        this.dateLayout.setOnClickListener(this);
        this.resetPlaceButton.setOnClickListener(this);
        this.resetDateButton.setOnClickListener(this);
        this.resetButton.setOnClickListener(this);
        this.selectedDate.setOnClickListener(this);
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        this.searchViewModel.getAllPlaces().observe(this, allPlaces -> {
            this.placeSpinner.setAdapter(new PlaceArrayAdapter(this.getContext(), 0, allPlaces));
        });
    }

    private void showDatePickerDialog() {
        LocalDate defaultDate = LocalDate.now();
        DatePickerDialog dialog = new DatePickerDialog(this.getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                LocalDate selected = LocalDate.of(year, month + 1, dayOfMonth);
                searchViewModel.setSelectedDate(selected);
                selectedDate.setText(new CustomDateTimeFormatter().formatDateToString(selected));
                onSelectDate();
            }
        }, defaultDate.getYear(), defaultDate.getMonthValue() - 1, defaultDate.getDayOfMonth());
        dialog.setTitle(getString(R.string.select_date));
        dialog.show();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        this.searchViewModel.setSelectedPlace((Place) this.placeSpinner.getAdapter().getItem(position));
        this.onSelectPlace();
    }



    @Override
    public void onClick(View v) {
        if(v == this.dateLayout || v == this.selectedDate) {
            this.showDatePickerDialog();
        } else if(v == this.resetPlaceButton) {
            this.resetPlace();
        } else if(v == this.resetDateButton) {
            this.resetDate();
        } else if(v == this.resetButton) {
            this.resetAll();
        }
    }



    private void onSelectPlace() {
        if(this.searchViewModel.getSelectedDate().getValue() == null) {
            EventBus.getDefault().post(new SearchByPlaceEvent(this.searchViewModel.getSelectedPlace().getValue()));
        } else {
            EventBus.getDefault().post(
                    new SearchByPlaceAndDateEvent(
                            this.searchViewModel.getSelectedPlace().getValue(),
                            this.searchViewModel.getSelectedDate().getValue()));
        }
    }

    private void onSelectDate() {
        if(this.searchViewModel.getSelectedPlace().getValue() == null) {
            EventBus.getDefault().post(new SearchByDateEvent(this.searchViewModel.getSelectedDate().getValue()));
        } else {
            EventBus.getDefault().post(
                    new SearchByPlaceAndDateEvent(this.searchViewModel.getSelectedPlace().getValue(),
                            this.searchViewModel.getSelectedDate().getValue()));
        }
    }

    private void resetPlace() {
        this.searchViewModel.setSelectedPlace(null);
        this.placeSpinner.setText("");
        if(searchViewModel.getSelectedDate().getValue() == null) {
            EventBus.getDefault().post(new InitSearchEvent());
        } else {
            EventBus.getDefault().post(new SearchByDateEvent(this.searchViewModel.getSelectedDate().getValue()));
        }
    }

    private void resetDate() {
        this.searchViewModel.setSelectedDate(null);
        this.selectedDate.setText("");
        if(searchViewModel.getSelectedPlace().getValue() == null) {
            EventBus.getDefault().post(new InitSearchEvent());
        } else {
            EventBus.getDefault().post(new SearchByPlaceEvent(searchViewModel.getSelectedPlace().getValue()));
        }
    }

    private void resetAll() {
        this.resetPlace();
        this.resetDate();
        EventBus.getDefault().post(new InitSearchEvent()); // To reset the list
    }
    
}
