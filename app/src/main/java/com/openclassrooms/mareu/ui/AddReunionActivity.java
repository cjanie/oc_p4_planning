package com.openclassrooms.mareu.ui;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;


import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
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
import com.openclassrooms.mareu.entities.Place;
import com.openclassrooms.mareu.entities.Reservation;
import com.openclassrooms.mareu.events.CommandEvent;
import com.openclassrooms.mareu.events.ReservationEvent;
import com.openclassrooms.mareu.exceptions.EmptyAvailableParticipantsException;
import com.openclassrooms.mareu.exceptions.EmptySelectedParticipantsException;
import com.openclassrooms.mareu.exceptions.EmptySubjectException;
import com.openclassrooms.mareu.exceptions.InvalidEndDateException;
import com.openclassrooms.mareu.exceptions.InvalidEndTimeException;
import com.openclassrooms.mareu.exceptions.NullReservationException;
import com.openclassrooms.mareu.exceptions.PassedStartDateException;
import com.openclassrooms.mareu.exceptions.UnavailablePlacesException;
import com.openclassrooms.mareu.exceptions.NullDateException;
import com.openclassrooms.mareu.exceptions.NullEndTimeException;
import com.openclassrooms.mareu.exceptions.NullPlaceException;
import com.openclassrooms.mareu.exceptions.NullStartTimeException;
import com.openclassrooms.mareu.exceptions.PassedDatesException;
import com.openclassrooms.mareu.exceptions.PassedStartTimeException;
import com.openclassrooms.mareu.utils.CustomDateTimeFormatter;
import com.openclassrooms.mareu.utils.ErrorHandler;
import com.openclassrooms.mareu.viewmodels.FormViewModel;
import com.openclassrooms.mareu.viewmodels.PlanningViewModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.time.LocalDateTime;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddReunionActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, TextWatcher, View.OnClickListener {

    private FormViewModel formViewModel;

    private PlanningViewModel planningViewModel;

    private ErrorHandler errorHandler;

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

    @BindView(R.id.reunion_subject_layout)
    TextInputLayout subject;
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
        this.planningViewModel = new ViewModelProvider(this).get(PlanningViewModel.class);
        this.errorHandler = new ErrorHandler(this);


        ButterKnife.bind(this);
        this.startDateLayout.setOnClickListener(this);
        this.startDateInput.setOnClickListener(this);
        this.startTimeLayout.setOnClickListener(this);
        this.startTimeInput.setOnClickListener(this);
        this.endDateLayout.setOnClickListener(this);
        this.endDateInput.setOnClickListener(this);
        this.endTimeLayout.setOnClickListener(this);
        this.endTimeInput.setOnClickListener(this);

        this.placeSpinner.setOnItemClickListener(this);
        this.subject.getEditText().addTextChangedListener(this);
        this.saveButton.setOnClickListener(this);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();

        // ui observes data provided by view model
        CustomDateTimeFormatter formatter = new CustomDateTimeFormatter();
        this.formViewModel.getStart().observe(this, dateTime -> {
            this.startDateLayout.getEditText().setText(formatter.formatDateToString(dateTime.toLocalDate()));
            this.startDateLayout.setError(null);
            this.startTimeLayout.getEditText().setText(formatter.formatTimeToString(dateTime.toLocalTime()));
            this.startTimeLayout.setError(null);
        });
        this.formViewModel.getEnd().observe(this, dateTime -> {
            this.endDateLayout.getEditText().setText(formatter.formatDateToString(dateTime.toLocalDate()));
            this.endDateLayout.setError(null);
            this.endTimeLayout.getEditText().setText(formatter.formatTimeToString(dateTime.toLocalTime()));
            this.endTimeLayout.setError(null);
        });

        this.onReserve(); // the activity sends an event at init for data to be set according to reservation

    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void getAvailable(ReservationEvent event) {

        this.planningViewModel.setReservation(event.reservation);

        this.placeSpinner.setError(null);
        this.participantsSpinner.setError(null);

        try {
            this.planningViewModel.getAvailablePlaces(event.reservation).observe(this, availablePlaces -> {
                this.placeSpinner.setAdapter(new PlaceArrayAdapter(this, 0, availablePlaces));
                this.placeSpinner.setText(availablePlaces.get(0).getName());
                this.formViewModel.setPlace(availablePlaces.get(0));
            });
            this.planningViewModel.getAvailableParticipants(event.reservation).observe(this, availableParticipants -> {
                // TODO: dialog
            });
        } catch (NullReservationException e) {
            this.errorHandler.signalErrorInSnackbar(this.errorHandler.getMessage(e), this.placeSpinner);
        } catch (UnavailablePlacesException e) {
            this.errorHandler.signalErrorInSnackbar(this.errorHandler.getMessage(e), this.placeSpinner);
            this.placeSpinner.setError(this.errorHandler.getMessage(e));
        } catch (EmptyAvailableParticipantsException e) {
            this.errorHandler.signalErrorInSnackbar(this.errorHandler.getMessage(e), this.participantsSpinner);
            this.participantsSpinner.setError(this.errorHandler.getMessage(e));
        }
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        this.formViewModel.setPlace((Place) this.placeSpinner.getAdapter().getItem(position));
        System.out.println(this.formViewModel.getPlace().getValue().getName() + " *****************");
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

    // START DATE PICKER
    private void showStartDatePickerDialog() {

        // Set UI from Data
        // Test considering default getMonthValue: 5

        LocalDateTime defaultDateTime = this.formViewModel.getStart().getValue();


        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                // Set selected
                // Month is 4 since it is an index value

                LocalDateTime selected = LocalDateTime.of(year, month + 1, dayOfMonth, // Month is 4 since it is an index value
                        defaultDateTime.getHour(), defaultDateTime.getMinute()).plusMinutes(5); // start in a delay human friendly of 5 minutes


                try {
                    // setStart doesn't throw passed start date exception in the delay of 5 minutes!!!
                    // Action on reserve creates reservation and sends ReservationEvent carrying the reservation
                    formViewModel.setStart(selected);
                    onReserve();

                } catch (PassedStartDateException e) {
                    errorHandler.signalErrorInSnackbar(errorHandler.getMessage(e), startDateLayout);
                    startDateLayout.setError(errorHandler.getMessage(e));
                    startDateLayout.getEditText().setText("");
                } catch (NullDateException e) {
                    errorHandler.signalErrorInSnackbar(errorHandler.getMessage(e), startDateLayout);
                    startDateLayout.setError(errorHandler.getMessage(e));
                    startDateLayout.getEditText().setText("");
                } catch (PassedStartTimeException e) {
                    errorHandler.signalErrorInSnackbar(errorHandler.getMessage(e), startTimeLayout);
                    startTimeLayout.setError(errorHandler.getMessage(e));
                    startTimeLayout.getEditText().setText("");
                }
            }
        }, defaultDateTime.getYear(),
                defaultDateTime.getMonthValue() - 1, // Picker constructor requires index starting from 0: month is at index getMonthValue - 1 = 4
                defaultDateTime.getDayOfMonth());

        datePickerDialog.setTitle(getString(R.string.select_date));
        datePickerDialog.show();
    }

    // END DATE PICKER

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
                    errorHandler.signalErrorInSnackbar(errorHandler.getMessage(e), endDateLayout);
                    endDateLayout.setError(errorHandler.getMessage(e));
                    endDateLayout.getEditText().setText("");
                } catch (NullDateException e) {
                    errorHandler.signalErrorInSnackbar(errorHandler.getMessage(e), endDateLayout);
                    endDateLayout.setError(errorHandler.getMessage(e));
                    endDateLayout.getEditText().setText("");
                }
            }
        }, defaultDateTime.getYear(), defaultDateTime.getMonthValue() - 1, defaultDateTime.getDayOfMonth());
        datePickerDialog.setTitle(getString(R.string.select_date));
        datePickerDialog.show();
    }

    // START TIME PICKER
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
                    errorHandler.signalErrorInSnackbar(errorHandler.getMessage(e), this.startTimeLayout);
                    this.startTimeLayout.setError(errorHandler.getMessage(e));
                    this.startTimeLayout.getEditText().setText("");
                } catch (NullDateException e) {
                    errorHandler.signalErrorInSnackbar(errorHandler.getMessage(e), this.startTimeLayout);
                    this.startTimeLayout.setError(errorHandler.getMessage(e));
                    this.startTimeLayout.getEditText().setText("");
                } catch (PassedStartTimeException e) {
                    errorHandler.signalErrorInSnackbar(errorHandler.getMessage(e), this.startTimeLayout);
                    this.startTimeLayout.setError(errorHandler.getMessage(e));
                    this.startTimeLayout.getEditText().setText("");
                }

            }, defaultDateTime.getHour(), defaultDateTime.getMinute(), true); // True for 24 hour time
            timePickerDialog.setTitle(this.getString(R.string.select_start_time));
            timePickerDialog.show();
        }
    }

    // END TIME PICKER

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
                    this.errorHandler.signalErrorInSnackbar(this.errorHandler.getMessage(e), this.endTimeLayout);
                    this.endTimeLayout.setError(this.errorHandler.getMessage(e));
                    this.endTimeLayout.getEditText().setText("");
                } catch (NullDateException e) {
                    this.errorHandler.signalErrorInSnackbar(this.errorHandler.getMessage(e), this.endTimeLayout);
                    this.endTimeLayout.setError(this.errorHandler.getMessage(e));
                    this.endTimeLayout.getEditText().setText("");
                }

            }, defaultDateTime.getHour(), defaultDateTime.getMinute(), true);
            timePickerDialog.setTitle(getString(R.string.select_end_time));
            timePickerDialog.show();
        }
    }

    private void onReserve() {

        Reservation reservation = null;

        try {

            reservation = new Reservation(this.formViewModel.getStart().getValue(), this.formViewModel.getEnd().getValue());

        } catch (NullStartTimeException e) {
            this.errorHandler.signalErrorInSnackbar(this.errorHandler.getMessage(e), this.startTimeInput);
        } catch (PassedStartTimeException e) {
            this.errorHandler.signalErrorInSnackbar(this.errorHandler.getMessage(e), this.startTimeInput);
        } catch (NullEndTimeException e) {
            this.errorHandler.signalErrorInSnackbar(this.errorHandler.getMessage(e), this.endTimeInput);
        } catch (PassedDatesException e) {
            this.errorHandler.signalErrorInSnackbar(this.errorHandler.getMessage(e), this.endDateInput);
        } catch (InvalidEndTimeException e) {
            this.errorHandler.signalErrorInSnackbar(this.errorHandler.getMessage(e), this.endDateInput);
        } catch (NullDateException e) {
            this.errorHandler.signalErrorInSnackbar(this.errorHandler.getMessage(e), this.startDateInput);
        }

        EventBus.getDefault().post(new ReservationEvent(reservation));
    }

    private void navigateToMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        this.startActivity(intent);
    }

    private void onSave() {

        ErrorHandler errorHandler = new ErrorHandler(this.getApplicationContext());
        try {
            this.planningViewModel.addReunion(this.formViewModel.createReunion());
            this.navigateToMainActivity();

        } catch (NullStartTimeException e) {
            errorHandler.signalErrorInSnackbar(errorHandler.getMessage(e), this.saveButton);
        } catch (EmptySelectedParticipantsException e) {
            errorHandler.signalErrorInSnackbar(errorHandler.getMessage(e), this.saveButton);
        } catch (PassedStartTimeException e) {
            errorHandler.signalErrorInSnackbar(errorHandler.getMessage(e), this.saveButton);
        } catch (NullEndTimeException e) {
            errorHandler.signalErrorInSnackbar(errorHandler.getMessage(e), this.saveButton);
        } catch (PassedDatesException e) {
            errorHandler.signalErrorInSnackbar(errorHandler.getMessage(e), this.saveButton);
        } catch (InvalidEndTimeException e) {
            errorHandler.signalErrorInSnackbar(errorHandler.getMessage(e), this.saveButton);
        } catch (EmptySubjectException e) {
            errorHandler.signalErrorInSnackbar(errorHandler.getMessage(e), this.saveButton);
        } catch (NullDateException e) {
            errorHandler.signalErrorInSnackbar(errorHandler.getMessage(e), this.saveButton);
        } catch (UnavailablePlacesException e) {
            errorHandler.signalErrorInSnackbar(errorHandler.getMessage(e), this.saveButton);
        } catch (NullPlaceException e) {
            errorHandler.signalErrorInSnackbar(errorHandler.getMessage(e), this.saveButton);
        } catch (NullReservationException e) {
            errorHandler.signalErrorInSnackbar(errorHandler.getMessage(e), this.saveButton);
        } catch (EmptyAvailableParticipantsException e) {
            errorHandler.signalErrorInSnackbar(errorHandler.getMessage(e), this.saveButton);
        }
    }
}