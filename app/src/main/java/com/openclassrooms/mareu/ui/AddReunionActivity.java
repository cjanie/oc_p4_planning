package com.openclassrooms.mareu.ui;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TimePicker;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.openclassrooms.mareu.MainActivity;
import com.openclassrooms.mareu.R;
import com.openclassrooms.mareu.adapters.PlaceArrayAdapter;
import com.openclassrooms.mareu.api.PlaceService;
import com.openclassrooms.mareu.api.ReunionService;
import com.openclassrooms.mareu.entities.Place;
import com.openclassrooms.mareu.entities.Reservation;
import com.openclassrooms.mareu.entities.Reunion;
import com.openclassrooms.mareu.exceptions.InvalidEndTimeException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


public class AddReunionActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    ImageView backButton;

    TextInputLayout subject;

    TextInputLayout dateLayout;
    TextInputEditText dateInput;
    TextInputLayout startLayout;
    TextInputEditText startInput;
    TextInputLayout endLayout;
    TextInputEditText endInput;
    AutoCompleteTextView placeSpinner;

    Button saveButton;

    private LocalDate today;
    private LocalDate reunionDate;
    private LocalDateTime start;
    private LocalDateTime end;
    private Place place;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reunion);

        this.backButton = this.findViewById(R.id.back_to_main_button);

        this.subject = this.findViewById(R.id.reunion_subject_layout);

        this.dateLayout = this.findViewById(R.id.reunion_date_layout);
        this.dateInput = this.findViewById(R.id.reunion_date);
        this.startLayout = this.findViewById(R.id.reunion_start_layout);
        this.startInput = this.findViewById(R.id.reunion_start);
        this.endLayout = this.findViewById(R.id.reunion_end_layout);
        this.endInput =  this.findViewById(R.id.reunion_end);

        this.placeSpinner = this.findViewById(R.id.reunion_place_spinner);

        this.saveButton = this.findViewById(R.id.save_reunion_button);


        this.backButton.setOnClickListener(this);

        this.dateLayout.setOnClickListener(this);
        this.dateInput.setOnClickListener(this);
        this.startLayout.setOnClickListener(this);
        this.startInput.setOnClickListener(this);
        this.endLayout.setOnClickListener(this);
        this.endInput.setOnClickListener(this);

        this.placeSpinner.setOnItemClickListener(this);

        this.saveButton.setOnClickListener(this);

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onPostResume() {
        super.onPostResume();
        this.today = LocalDate.now();
        this.dateInput.setText(this.formatDateToString(today));

        // Set places into spinner
        List<String> placesNames = new ArrayList<>();
        PlaceService.getInstance().getAvailablePlaces().observe(this, new Observer<List<Place>>() {
            @Override
            public void onChanged(List<Place> places) {
                if(places != null) {
                    PlaceArrayAdapter placeArrayAdapter = new PlaceArrayAdapter(AddReunionActivity.this, 0, places);
                    placeSpinner.setAdapter(placeArrayAdapter);
                    placeSpinner.setOnItemClickListener(AddReunionActivity.this);
                    placeSpinner.setText(places.get(0).getName());
                    place = places.get(0);
                }
            }
        });

    }



    @RequiresApi(api = Build.VERSION_CODES.O)
    private String formatDateToString(LocalDate date) {
        return date.format(DateTimeFormatter.ISO_DATE);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private String formatTimeToString(LocalDateTime dateTime) {
        return dateTime.format(DateTimeFormatter.ISO_TIME).substring(0, 5);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void signalErrorInSnackbar(String error, View v) {
        Snackbar.make(v, error, Snackbar.LENGTH_LONG).setBackgroundTint(this.getColor(R.color.color_accent)).show();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void showDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                LocalDate selected = LocalDate.of(year, month, dayOfMonth);
                if(selected.isBefore(today)) {
                    signalErrorInSnackbar(getString(R.string.error_date_passed), dateInput);
                } else {
                    reunionDate = selected;
                    dateInput.setText(formatDateToString(reunionDate));
                    if(!TextUtils.isEmpty(startInput.getText())) {
                        start = null;
                        startInput.setText(null);
                    }
                    if(!TextUtils.isEmpty(endInput.getText())) {
                        end = null;
                        endInput.setText(null);
                    }
                }
            }
        }, today.getYear(), today.getMonthValue(), this.today.getDayOfMonth());
        datePickerDialog.setTitle(this.getString(R.string.select_date));
        datePickerDialog.show();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void showStartTimePickerDialog() {
        if(this.reunionDate == null) {
            this.reunionDate = this.today;
        }
        LocalDateTime now = LocalDateTime.of(this.reunionDate, LocalTime.now());
        int hourNow = now.getHour();
        int minNow = now.getMinute();
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                LocalDateTime selected = LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(), hourOfDay, minute);
                if(selected.isBefore(LocalDateTime.now())) {
                    signalErrorInSnackbar(getString(R.string.error_time_passed), startInput);
                } else {
                    start = selected;
                    startInput.setText(formatTimeToString(start));
                }
            }
        }, hourNow, minNow, true); // True for 24 hour time
        timePickerDialog.setTitle(this.getString(R.string.select_start_time));
        timePickerDialog.show();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void showEndTimePickerDialog() {
        if(this.start != null) {
            TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    LocalDateTime selected = LocalDateTime.of(start.getYear(), start.getMonth(), start.getDayOfMonth(), hourOfDay, minute);
                    if(selected.isBefore(start)) {
                        signalErrorInSnackbar(getString(R.string.error_invalid_end_time), endInput);
                    } else {
                        end = selected;
                        endInput.setText(formatTimeToString(end));
                    }
                }
            }, this.start.getHour(), this.start.getMinute(), true); // True for 24 hour time
            timePickerDialog.setTitle(this.getString(R.string.select_end_time));
            timePickerDialog.show();
        } else {
            this.signalErrorInSnackbar(this.getString(R.string.error_no_start_time_selected), this.endInput);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        this.place = (Place) placeSpinner.getAdapter().getItem(position);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onClick(View v) {
        if(v == this.backButton) {
            Intent intent = new Intent(this, MainActivity.class);
            this.startActivity(intent);
            this.finish();
        } else if(v == this.dateLayout || v == this.dateInput) {
            this.showDatePickerDialog();
        } else if(v == this.startLayout || v == this.startInput) {
            this.showStartTimePickerDialog();
        } else if(v == this.endLayout || v == this.endInput) {
            this.showEndTimePickerDialog();
        } else if(v == this.saveButton) {
            /*
            if(!TextUtils.isEmpty(this.subject.getEditText().getText())) {
                if(this.place != null) {
                    if(this.start != null) {
                        if(this.end != null) {
                            Reservation reservation = null;
                            try {
                                reservation = new Reservation(this.start, this.end);
                            } catch (InvalidEndTimeException e) {
                                e.printStackTrace();
                                this.signalErrorInSnackbar(this.getString(R.string.error_invalid_end_time), this.endInput);
                            }
                            this.place.getReservations().add(reservation);
                            Reunion reunion = new Reunion();
                            reunion.setSubject(this.subject.getEditText().getText().toString());
                            reunion.setPlace(this.place);
                            ReunionService.getInstance().addReunion(reunion);
                        } else {
                            this.signalErrorInSnackbar(this.getString(R.string.error_no_end_time_selected), this.endInput);
                        }
                    } else {
                        this.signalErrorInSnackbar(this.getString(R.string.error_no_start_time_selected), this.startInput);
                    }
                } else {
                    this.signalErrorInSnackbar(this.getString(R.string.error_no_place), this.placeSpinner);
                }
            } else {
                this.signalErrorInSnackbar(this.getString(R.string.error_empty_subject), this.subject);
            }

        */
        }
    }
}