package com.openclassrooms.mareu.ui;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.openclassrooms.mareu.MainActivity;
import com.openclassrooms.mareu.R;
import com.openclassrooms.mareu.adapters.PlaceArrayAdapter;
import com.openclassrooms.mareu.entities.Participant;
import com.openclassrooms.mareu.entities.Place;
import com.openclassrooms.mareu.entities.Reservation;
import com.openclassrooms.mareu.events.ReservationEvent;
import com.openclassrooms.mareu.exceptions.EmptySelectedParticipantsException;
import com.openclassrooms.mareu.exceptions.EmptySubjectException;
import com.openclassrooms.mareu.exceptions.InvalidEndDateException;
import com.openclassrooms.mareu.exceptions.InvalidEndTimeException;
import com.openclassrooms.mareu.exceptions.NullReservationException;
import com.openclassrooms.mareu.exceptions.UnavailablePlacesException;
import com.openclassrooms.mareu.exceptions.NullDatesException;
import com.openclassrooms.mareu.exceptions.NullEndTimeException;
import com.openclassrooms.mareu.exceptions.NullPlaceException;
import com.openclassrooms.mareu.exceptions.NullStartTimeException;
import com.openclassrooms.mareu.exceptions.PassedDatesException;
import com.openclassrooms.mareu.exceptions.PassedStartDateException;
import com.openclassrooms.mareu.exceptions.PassedStartTimeException;
import com.openclassrooms.mareu.utils.CustomDateTimeFormatter;
import com.openclassrooms.mareu.utils.ErrorHandler;
import com.openclassrooms.mareu.viewmodels.FormViewModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddReunionActivity extends AppCompatActivity implements TextWatcher, View.OnClickListener, AdapterView.OnItemClickListener {

    private FormViewModel formViewModel;

    @BindView(R.id.reunion_subject_layout)
    TextInputLayout subject;

    @BindView(R.id.reunion_start_date_layout)
    TextInputLayout startDateLayout;
    @BindView(R.id.reunion_start_date)
    TextInputEditText startDateInput;
    @BindView(R.id.reunion_start_time_layout)
    TextInputLayout startTimeLayout;
    @BindView(R.id.reunion_start_time)
    TextInputEditText startTimeInput;
    @BindView(R.id.reunion_end_date_layout)
    TextInputLayout endDateLayout;
    @BindView(R.id.reunion_end_date)
    TextInputEditText endDateInput;
    @BindView(R.id.reunion_end_time_layout)
    TextInputLayout endTimeLayout;
    @BindView(R.id.reunion_end_time)
    TextInputEditText endTimeInput;

    @BindView(R.id.reunion_place_spinner)
    AutoCompleteTextView placeSpinner;
    @BindView(R.id.reunion_participants_spinner)
    AutoCompleteTextView participantsSpinner;
    @BindView(R.id.save_reunion_button)
    Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reunion);

        ActionBar actionBar = this.getSupportActionBar(); // To call the action bar
        actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_24); // To customize the back button (optional)
        actionBar.setDisplayHomeAsUpEnabled(true); // to show the back button in action bar; implemented by Android

        this.formViewModel = new ViewModelProvider(this).get(FormViewModel.class);

        try {

            this.formViewModel.makeReservationThenLoadAvailablePlaces(); // From default start and end values at init to search for available places
            this.formViewModel.makeReservationThenLoadAvailableParticipants();

        } catch (InvalidEndTimeException e) {
            signalErrorInSnackbar(new ErrorHandler(this).getMessage(e), this.startTimeInput);
        } catch (PassedStartTimeException e) {
            e.printStackTrace();
        } catch (PassedDatesException e) {
            e.printStackTrace();
        } catch (NullEndTimeException e) {
            e.printStackTrace();
        } catch (NullDatesException e) {
            e.printStackTrace();
        } catch (NullStartTimeException e) {
            e.printStackTrace();
        } catch (NullReservationException e) {
            e.printStackTrace();
        }

        ButterKnife.bind(this);
        this.subject.getEditText().addTextChangedListener(this);
        this.startDateLayout.setOnClickListener(this);
        this.startDateInput.setOnClickListener(this);
        this.startTimeLayout.setOnClickListener(this);
        this.startTimeInput.setOnClickListener(this);
        this.endDateLayout.setOnClickListener(this);
        this.endDateInput.setOnClickListener(this);
        this.endTimeLayout.setOnClickListener(this);
        this.endTimeInput.setOnClickListener(this);
        this.placeSpinner.setOnItemClickListener(this);
        this.participantsSpinner.setOnClickListener(this);
        this.saveButton.setOnClickListener(this);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        // Get data from view model and observe to set UI with values
        this.getDataToSetUIWithStartEndValues();
        this.getDataToSetUIWithAvailablePlacesValue();
        this.getDataToSetUIWithPlaceValue();
        this.getDataToSetUIWithSelectedParticipantsValue();
    }

    private void getDataToSetUIWithStartEndValues() {
        CustomDateTimeFormatter formatter = new CustomDateTimeFormatter();
        this.formViewModel.getStart().observe(this, dateTime -> {
            startDateInput.setText(formatter.formatDateToString(dateTime.toLocalDate()));
            startTimeInput.setText(formatter.formatTimeToString(dateTime.toLocalTime()));
        });
        this.formViewModel.getEnd().observe(this, dateTime -> {
            endDateInput.setText(formatter.formatDateToString(dateTime.toLocalDate()));
            endTimeInput.setText(formatter.formatTimeToString(dateTime.toLocalTime()));
        });
    }

    private void getDataToSetUIWithAvailablePlacesValue() {
        try {
            this.formViewModel.getAvailablePlaces().observe(this, availablePlaces -> {

                placeSpinner.setAdapter(new PlaceArrayAdapter(this, 0, availablePlaces));
                if(!availablePlaces.isEmpty()) {
                    formViewModel.setPlace(availablePlaces.get(0));
                }
            });

        } catch (NullReservationException e) {
            e.printStackTrace();
            this.signalErrorInSnackbar(new ErrorHandler(this).getMessage(e), this.placeSpinner);
        }
    }

    private void getDataToSetUIWithPlaceValue() {
        this.formViewModel.getPlace().observe(this, place -> {
            if(place != null) {
                placeSpinner.setText(place.getName());
            }
        });
    }

    private void getDataToSetUIWithSelectedParticipantsValue() {
        this.formViewModel.getSelectedParticipants().observe(this, selectedParticipants -> {
            StringBuilder stringBuilder = new StringBuilder();
            if(!selectedParticipants.isEmpty()) {
                for(Participant participant: selectedParticipants) {
                    stringBuilder.append(participant.getFirstName() + ", ");
                }
            }
            String names = "";
            if(!stringBuilder.toString().isEmpty()) {
                names = stringBuilder.toString().substring(0, stringBuilder.length() - 2);
            }
            participantsSpinner.setText(names);
        });
    }

    private void signalErrorInSnackbar(String error, View v) {
        int colorError = ContextCompat.getColor(this, R.color.color_accent);
        Snackbar.make(v, error, BaseTransientBottomBar.LENGTH_LONG).setBackgroundTint(colorError).show();
    }

    private void signalSuccessInSnackbar(String success, View v) {
        int colorSuccess = ContextCompat.getColor(this, R.color.color_success);
        Snackbar.make(v, success, BaseTransientBottomBar.LENGTH_LONG).setBackgroundTint(colorSuccess).show();
    }

    private void showStartDatePickerDialog() {

        // Set default
        LocalDateTime defaultDateTime = formViewModel.getStart().getValue(); // Test considering default getMonthValue: 5

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                // Set selected
                LocalDateTime selected = LocalDateTime.of(year, month + 1, dayOfMonth, // Month is 4 since it is an index value
                        defaultDateTime.getHour(), defaultDateTime.getMinute()).plusMinutes(5); // start in a delay human friendly of 5 minutes
                try {

                    formViewModel.setStart(selected); // setStart doesn't throw passed start date exception in the delay of 5 minutes
                    onReserve(); // Action on reserve

                } catch (PassedStartDateException e) {
                    signalErrorInSnackbar(getString(R.string.error_passed_start_date), startDateInput);
                } catch (InvalidEndDateException e) {
                    signalErrorInSnackbar(getString(R.string.error_invalid_end_date), endDateInput);
                }
            }
        }, defaultDateTime.getYear(),
                defaultDateTime.getMonthValue() - 1, // Picker constructor requires index starting from 0: month is at index getMonthValue - 1 = 4
                defaultDateTime.getDayOfMonth());

        datePickerDialog.setTitle(getString(R.string.select_date));
        datePickerDialog.show();
    }

    private void showEndDatePickerDialog() {
        LocalDateTime defaultDateTime = formViewModel.getEnd().getValue();
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                LocalDateTime selected = LocalDateTime.of(year, month + 1, dayOfMonth,
                        defaultDateTime.getHour(), defaultDateTime.getMinute());
                try {
                    formViewModel.setEnd(selected);
                    onReserve();
                } catch (InvalidEndDateException e) {
                    signalErrorInSnackbar(getString(R.string.error_invalid_end_date), endDateInput);
                }
            }
        }, defaultDateTime.getYear(), defaultDateTime.getMonthValue() - 1, defaultDateTime.getDayOfMonth());
        datePickerDialog.setTitle(getString(R.string.select_date));
        datePickerDialog.show();
    }

    private void showStartTimePickerDialog() {
        LocalDateTime defaultDateTime = this.formViewModel.getStart().getValue();
        if(defaultDateTime != null) {
            TimePickerDialog timePickerDialog = new TimePickerDialog(this, (TimePicker view, int hourOfDay, int minute) -> {
                LocalDateTime selected = LocalDateTime.of(defaultDateTime.getYear(), defaultDateTime.getMonth(), defaultDateTime.getDayOfMonth(),
                        hourOfDay, minute);
                try {
                    formViewModel.setStart(selected);
                    onReserve();
                }  catch (PassedStartDateException e) {
                    this.signalErrorInSnackbar(new ErrorHandler(this).getMessage(e), this.startTimeInput);
                } catch (InvalidEndDateException e) {
                    this.signalErrorInSnackbar(new ErrorHandler(this).getMessage(e), this.startTimeInput);
                }

            }, defaultDateTime.getHour(), defaultDateTime.getMinute(), true); // True for 24 hour time
            timePickerDialog.setTitle(this.getString(R.string.select_start_time));
            timePickerDialog.show();
        }
    }

    private void showEndTimePickerDialog() {
        LocalDateTime defaultDateTime = this.formViewModel.getEnd().getValue();
        if(defaultDateTime != null) {
            TimePickerDialog timePickerDialog = new TimePickerDialog(this, (TimePicker view, int hourOfDay, int minute) -> {
                LocalDateTime selected = LocalDateTime.of(defaultDateTime.getYear(), defaultDateTime.getMonthValue(), defaultDateTime.getDayOfMonth(),
                        hourOfDay, minute);
                try {
                    formViewModel.setEnd(selected);
                    onReserve();
                } catch (InvalidEndDateException e) {
                    signalErrorInSnackbar(new ErrorHandler(this).getMessage(e), endTimeInput);
                }
            }, defaultDateTime.getHour(), defaultDateTime.getMinute(), true);
            timePickerDialog.setTitle(getString(R.string.select_end_time));
            timePickerDialog.show();
        }
    }

    private void showParticipantsSelectionDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(AddReunionActivity.this);
        dialogBuilder.setTitle(R.string.select_participants);

        List<Participant> participants = this.formViewModel.getAllParticipants().getValue();
            String[] participantsCheckboxesLabels = new String[participants.size()]; // Checkboxes labels with participants names
            boolean[] participantsCheckboxesAreChecked = new boolean[participants.size()]; // Checkboxes as an array of booleans in regard to the participants names

            List<String> listOfCheckboxesLabels = new ArrayList<>(); // Each checkbox has a text that corresponds to a participant

            for(int i=0; i<participants.size(); i++) { // Index required to check checkboxes
                if(formViewModel.getSelectedParticipants().getValue().contains(participants.get(i))) {
                    participantsCheckboxesAreChecked[i] = true;
                }
                String label = participants.get(i).getFirstName() + " - "; // Build the label for each participant
                if(formViewModel.getReservation().getValue() != null) {
                    if(participants.get(i).isAvailable(formViewModel.getReservation().getValue())) {
                        label += getString(R.string.available);
                    } else {
                        label += getString(R.string.unavailable);
                    }
                    listOfCheckboxesLabels.add(label); // Add the label to the list of checkboxes labels
                }

            }

            listOfCheckboxesLabels.toArray(participantsCheckboxesLabels); // Wrap the list into the array

            dialogBuilder.setMultiChoiceItems(participantsCheckboxesLabels, participantsCheckboxesAreChecked, (DialogInterface dialog, int which, boolean isChecked) -> {
                participantsCheckboxesAreChecked[which] = isChecked;
            });

            dialogBuilder.setCancelable(false);

            dialogBuilder.setPositiveButton(R.string.done, (DialogInterface dialog, int which) -> {
                List<Participant> selectedParticipants = new ArrayList<>();
                for(int i=0; i<participantsCheckboxesAreChecked.length; i++) {
                    if(participantsCheckboxesAreChecked[i]) {
                        selectedParticipants.add(participants.get(i));
                    }
                }
                formViewModel.setSelectedParticipants(selectedParticipants);
            });

            dialogBuilder.setNegativeButton(R.string.cancel, (DialogInterface dialog, int which) -> dialog.dismiss());

            dialogBuilder.setNeutralButton(R.string.clear, (DialogInterface dialog, int which) -> {
                for(int i=0; i<participantsCheckboxesAreChecked.length; i++) {
                    participantsCheckboxesAreChecked[i] = false;
                    formViewModel.getSelectedParticipants().getValue().clear();
                    participantsSpinner.setText("");
                }
            });

            AlertDialog alertDialog = dialogBuilder.create();
            alertDialog.show();

    }

    private void navigateToMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        this.startActivity(intent);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        this.formViewModel.setPlace((Place) placeSpinner.getAdapter().getItem(position));
    }

    @Override
    public void onClick(View v) {
        if(v == this.startDateLayout || v == this.startDateInput) {
            this.showStartDatePickerDialog();
        } else if(v == this.endDateLayout || v == this.endDateInput) {
            this.showEndDatePickerDialog();
        } else if(v == this.startTimeLayout || v == this.startTimeInput) {
            this.showStartTimePickerDialog();
        } else if(v == this.endTimeLayout || v == this.endTimeInput) {
            this.showEndTimePickerDialog();
        } else if(v == this.participantsSpinner) {
            this.showParticipantsSelectionDialog();
        } else if(v == this.saveButton) {
            this.onSave();
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        this.formViewModel.setSubject(this.subject.getEditText().getText().toString());
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    private void onReserve() {
        EventBus.getDefault().post(new ReservationEvent());
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);

    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void loadDataAtReservationEvent(ReservationEvent event) {
        this.loadAvailablePlacesAtReservation();
        this.loadAvailableParticipantsAtReservation();
    }

    private void loadAvailablePlacesAtReservation() {
        try {

            this.formViewModel.makeReservationThenLoadAvailablePlaces(); // Same as in onCreate

        } catch (InvalidEndTimeException e) {
            e.printStackTrace();
        } catch (PassedStartTimeException e) {
            e.printStackTrace();
        } catch (PassedDatesException e) {
            e.printStackTrace();
        } catch (NullEndTimeException e) {
            e.printStackTrace();
        } catch (NullDatesException e) {
            e.printStackTrace();
        } catch (NullStartTimeException e) {
            e.printStackTrace();
        } catch (NullReservationException e) {
            e.printStackTrace();
        }
        this.signalSuccessInSnackbar(this.getApplicationContext().getString(R.string.success), this.startDateInput);
    }

    private void loadAvailableParticipantsAtReservation() {
        try {

            this.formViewModel.makeReservationThenLoadAvailableParticipants();

        } catch (PassedDatesException e) {
            e.printStackTrace();
        } catch (InvalidEndTimeException e) {
            e.printStackTrace();
        } catch (NullDatesException e) {
            e.printStackTrace();
        } catch (NullStartTimeException e) {
            e.printStackTrace();
        } catch (NullEndTimeException e) {
            e.printStackTrace();
        } catch (PassedStartTimeException e) {
            e.printStackTrace();
        } catch (NullReservationException e) {
            e.printStackTrace();
        }
    }






    private void onSave() {
        try {
            this.formViewModel.saveReunion();
            this.navigateToMainActivity();

        } catch (NullPlaceException e) {
            e.printStackTrace();
            this.signalErrorInSnackbar(new ErrorHandler(this).getMessage(e), this.placeSpinner);
        } catch (NullStartTimeException e) {
            e.printStackTrace();
            this.signalErrorInSnackbar(new ErrorHandler(this).getMessage(e), this.startTimeInput);
        } catch (EmptySelectedParticipantsException e) {
            e.printStackTrace();
            this.signalErrorInSnackbar(new ErrorHandler(this).getMessage(e), this.participantsSpinner);
        } catch (PassedStartTimeException e) {
            e.printStackTrace();
            this.signalErrorInSnackbar(new ErrorHandler(this).getMessage(e), this.startTimeInput);
        } catch (NullEndTimeException e) {
            e.printStackTrace();
            this.signalErrorInSnackbar(new ErrorHandler(this).getMessage(e), this.endTimeInput);
        } catch (PassedDatesException e) {
            e.printStackTrace();
            this.signalErrorInSnackbar(new ErrorHandler(this).getMessage(e), this.startDateInput);
        } catch (InvalidEndTimeException e) {
            e.printStackTrace();
            this.signalErrorInSnackbar(new ErrorHandler(this).getMessage(e), this.endTimeInput);
        } catch (EmptySubjectException e) {
            e.printStackTrace();
            this.signalErrorInSnackbar(new ErrorHandler(this).getMessage(e), this.subject);
        } catch (NullDatesException e) {
            e.printStackTrace();
            this.signalErrorInSnackbar(new ErrorHandler(this).getMessage(e), this.startDateInput);
        } catch (UnavailablePlacesException e) {
            e.printStackTrace();
            this.signalErrorInSnackbar(new ErrorHandler(this).getMessage(e), this.placeSpinner);
        }
    }
}