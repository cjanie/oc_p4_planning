package com.openclassrooms.mareu.ui;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
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

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.openclassrooms.mareu.MainActivity;
import com.openclassrooms.mareu.R;
import com.openclassrooms.mareu.adapters.PlaceArrayAdapter;
import com.openclassrooms.mareu.entities.Participant;
import com.openclassrooms.mareu.entities.Place;
import com.openclassrooms.mareu.entities.Reservation;
import com.openclassrooms.mareu.events.ReservationEvent;
import com.openclassrooms.mareu.exceptions.EmptyAvailableParticipantsException;
import com.openclassrooms.mareu.exceptions.EmptySelectedParticipantsException;
import com.openclassrooms.mareu.exceptions.EmptySubjectException;
import com.openclassrooms.mareu.exceptions.InvalidEndDateException;
import com.openclassrooms.mareu.exceptions.InvalidEndException;
import com.openclassrooms.mareu.exceptions.InvalidEndTimeException;
import com.openclassrooms.mareu.exceptions.NullReservationException;
import com.openclassrooms.mareu.exceptions.NullReunionException;
import com.openclassrooms.mareu.exceptions.PassedStartDateException;
import com.openclassrooms.mareu.exceptions.PassedStartTimeException;
import com.openclassrooms.mareu.exceptions.UnavailablePlacesException;
import com.openclassrooms.mareu.exceptions.NullDatesException;
import com.openclassrooms.mareu.exceptions.NullEndException;
import com.openclassrooms.mareu.exceptions.NullPlaceException;
import com.openclassrooms.mareu.exceptions.NullStartException;
import com.openclassrooms.mareu.exceptions.PassedDatesException;
import com.openclassrooms.mareu.exceptions.PassedStartException;
import com.openclassrooms.mareu.utils.CustomDateTimeFormatter;
import com.openclassrooms.mareu.utils.ErrorHandler;
import com.openclassrooms.mareu.viewmodels.FormViewModel;
import com.openclassrooms.mareu.viewmodels.PlanningViewModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    @BindView(R.id.next_reservation_fab)
    FloatingActionButton nextButton;

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
        this.participantsSpinner.setOnClickListener(this);
        this.subject.getEditText().addTextChangedListener(this);
        this.saveButton.setOnClickListener(this);
        this.nextButton.setOnClickListener(this);
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
        this.planningViewModel.setReservation(null);
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void getAvailable(ReservationEvent event) {

        this.planningViewModel.setReservation(event.reservation);
        try {
            this.formViewModel.setStart(event.reservation.getStart());
            this.formViewModel.setEnd(event.reservation.getEnd());
        } catch (NullDatesException e) {
            this.errorHandler.signalErrorInSnackbar(this.errorHandler.getMessage(e), this.startDateLayout);
            this.startDateLayout.setError(this.errorHandler.getMessage(e));
        } catch (InvalidEndDateException e) {
            this.errorHandler.signalErrorInSnackbar(this.errorHandler.getMessage(e), this.endDateLayout);
            this.endDateLayout.setError(this.errorHandler.getMessage(e));
        } catch (NullStartException e) {
            this.errorHandler.signalErrorInSnackbar(this.errorHandler.getMessage(e), this.startDateLayout);
            this.startDateLayout.setError(this.errorHandler.getMessage(e));
        } catch (NullEndException e) {
            this.errorHandler.signalErrorInSnackbar(this.errorHandler.getMessage(e), this.endDateLayout);
            this.endDateLayout.setError(this.errorHandler.getMessage(e));
        } catch (PassedStartTimeException e) {
            this.errorHandler.signalErrorInSnackbar(this.errorHandler.getMessage(e), this.startTimeLayout);
            this.startTimeLayout.setError(this.errorHandler.getMessage(e));;
        } catch (PassedStartDateException e) {
            this.errorHandler.signalErrorInSnackbar(this.errorHandler.getMessage(e), this.startDateLayout);
            this.startDateLayout.setError(this.errorHandler.getMessage(e));
        } catch (InvalidEndTimeException e) {
            this.errorHandler.signalErrorInSnackbar(this.errorHandler.getMessage(e), this.endTimeLayout);
            this.endTimeLayout.setError(this.errorHandler.getMessage(e));
        }


        this.placeSpinner.setError(null);
        this.participantsSpinner.setError(null);

        try {
            // Observe data for place spinner
            this.planningViewModel.getAvailablePlaces(event.reservation).observe(this, availablePlaces -> {
                this.placeSpinner.setAdapter(new PlaceArrayAdapter(this, 0, availablePlaces));
                this.formViewModel.setPlace(availablePlaces.get(0));
            });

            // Observe data to set defaut selected participants
            this.planningViewModel.getAvailableParticipants(event.reservation).observe(this, availableParticipants -> {
                if(this.formViewModel.getParticipants().getValue().isEmpty()) {
                    this.formViewModel.getParticipants().getValue().add(availableParticipants.get(0));
                    if(availableParticipants.size() > 1) {
                        this.formViewModel.getParticipants().getValue().add(availableParticipants.get(1));
                    }
                }
            });

            // Observe selected for place spinner
            this.formViewModel.getPlace().observe(this, selectedPlace -> {
                this.placeSpinner.setText(selectedPlace.getName());
            });

            // Observe selected for participants spinner
            this.formViewModel.getParticipants().observe(this, selectedParticipants -> {
                this.participantsSpinner.setText(this.formatParticipantsToString(selectedParticipants));
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
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        this.formViewModel.setSubject(this.subject.getEditText().getText().toString());
    }

    @Override
    public void afterTextChanged(Editable s) {

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
        } else if(v == this.participantsSpinner) {
            this.showParticipantsDialog();
        } else if(v == this.saveButton) {
            this.onSave();
        } else if(v == this.nextButton) {
            try {

                EventBus.getDefault().post(new ReservationEvent(this.planningViewModel.getNextAvailableReservation(this.planningViewModel.getReservation().getValue())));
            } catch (NullReservationException e) {
                this.errorHandler.signalErrorInSnackbar(this.errorHandler.getMessage(e), this.nextButton);
            } catch (PassedDatesException e) {
                this.errorHandler.signalErrorInSnackbar(this.errorHandler.getMessage(e), this.nextButton);
            } catch (InvalidEndException e) {
                this.errorHandler.signalErrorInSnackbar(this.errorHandler.getMessage(e), this.nextButton);
            } catch (PassedStartException e) {
                this.errorHandler.signalErrorInSnackbar(this.errorHandler.getMessage(e), this.nextButton);
            } catch (NullStartException e) {
                this.errorHandler.signalErrorInSnackbar(this.errorHandler.getMessage(e), this.nextButton);
            } catch (NullEndException e) {
                this.errorHandler.signalErrorInSnackbar(this.errorHandler.getMessage(e), this.nextButton);
            } catch (NullDatesException e) {
                this.errorHandler.signalErrorInSnackbar(this.errorHandler.getMessage(e), this.nextButton);
            }
        }
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

                } catch (NullDatesException e) {
                    errorHandler.signalErrorInSnackbar(errorHandler.getMessage(e), startDateLayout);
                    startDateLayout.setError(errorHandler.getMessage(e));
                    startDateLayout.getEditText().setText("");
                } catch (NullStartException e) {
                    errorHandler.signalErrorInSnackbar(errorHandler.getMessage(e), startTimeLayout);
                    startTimeLayout.setError(errorHandler.getMessage(e));
                    startTimeLayout.getEditText().setText("");
                } catch (NullEndException e) {
                    errorHandler.signalErrorInSnackbar(errorHandler.getMessage(e), endTimeLayout);
                    endTimeLayout.setError(errorHandler.getMessage(e));
                    endTimeLayout.getEditText().setText("");
                } catch (PassedStartTimeException e) {
                    errorHandler.signalErrorInSnackbar(errorHandler.getMessage(e), startTimeLayout);
                    startTimeLayout.setError(errorHandler.getMessage(e));
                    startTimeLayout.getEditText().setText("");
                } catch (PassedStartDateException e) {
                    errorHandler.signalErrorInSnackbar(errorHandler.getMessage(e), startDateLayout);
                    startDateLayout.setError(errorHandler.getMessage(e));
                    startDateLayout.getEditText().setText("");
                } catch (InvalidEndTimeException e) {
                    errorHandler.signalErrorInSnackbar(errorHandler.getMessage(e), endTimeLayout);
                    endTimeLayout.setError(errorHandler.getMessage(e));
                    endTimeLayout.getEditText().setText("");
                } catch (InvalidEndDateException e) {
                    errorHandler.signalErrorInSnackbar(errorHandler.getMessage(e), endDateLayout);
                    endDateLayout.setError(errorHandler.getMessage(e));
                    endDateLayout.getEditText().setText("");
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
                } catch (NullDatesException e) {
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

                } catch (NullDatesException e) {
                    errorHandler.signalErrorInSnackbar(errorHandler.getMessage(e), this.startDateLayout);
                    this.startDateLayout.setError(errorHandler.getMessage(e));
                    this.startDateLayout.getEditText().setText("");
                }  catch (NullStartException e) {
                    errorHandler.signalErrorInSnackbar(errorHandler.getMessage(e), this.startDateLayout);
                    this.startDateLayout.setError(errorHandler.getMessage(e));
                    this.startDateLayout.getEditText().setText("");
                } catch (NullEndException e) {
                    errorHandler.signalErrorInSnackbar(errorHandler.getMessage(e), this.endDateLayout);
                    this.endDateLayout.setError(errorHandler.getMessage(e));
                    this.endDateLayout.getEditText().setText("");
                } catch (PassedStartTimeException e) {
                    errorHandler.signalErrorInSnackbar(errorHandler.getMessage(e), this.startTimeLayout);
                    this.startTimeLayout.setError(errorHandler.getMessage(e));
                    this.startTimeLayout.getEditText().setText("");
                } catch (PassedStartDateException e) {
                    errorHandler.signalErrorInSnackbar(errorHandler.getMessage(e), this.startDateLayout);
                    this.startDateLayout.setError(errorHandler.getMessage(e));
                    this.startDateLayout.getEditText().setText("");
                } catch (InvalidEndTimeException e) {
                    errorHandler.signalErrorInSnackbar(errorHandler.getMessage(e), this.endTimeLayout);
                    this.endTimeLayout.setError(errorHandler.getMessage(e));
                    this.endTimeLayout.getEditText().setText("");
                } catch (InvalidEndDateException e) {
                    errorHandler.signalErrorInSnackbar(errorHandler.getMessage(e), this.endDateLayout);
                    this.endDateLayout.setError(errorHandler.getMessage(e));
                    this.endDateLayout.getEditText().setText("");
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
                } catch (NullDatesException e) {
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

        } catch (NullStartException e) {
            this.errorHandler.signalErrorInSnackbar(this.errorHandler.getMessage(e), this.startTimeInput);
        } catch (PassedStartException e) {
            this.errorHandler.signalErrorInSnackbar(this.errorHandler.getMessage(e), this.startTimeInput);
        } catch (NullEndException e) {
            this.errorHandler.signalErrorInSnackbar(this.errorHandler.getMessage(e), this.endTimeInput);
        } catch (PassedDatesException e) {
            this.errorHandler.signalErrorInSnackbar(this.errorHandler.getMessage(e), this.endDateInput);
        } catch (InvalidEndException e) {
            this.errorHandler.signalErrorInSnackbar(this.errorHandler.getMessage(e), this.endDateInput);
        } catch (NullDatesException e) {
            this.errorHandler.signalErrorInSnackbar(this.errorHandler.getMessage(e), this.startDateInput);
        }

        EventBus.getDefault().post(new ReservationEvent(reservation));
    }

    // PARTICIPANTS DIALOG
    // https://www.geeksforgeeks.org/alert-dialog-with-multipleitemselection-in-android/
    private void showParticipantsDialog() {

        this.planningViewModel.getAllParticipants().observe(this, allParticipants -> {

            // Prepare items
            List<String> labels = new ArrayList<>();
            for (Participant participant : allParticipants) {
                StringBuilder stringBuilder = new StringBuilder("");
                stringBuilder.append(participant.getFirstName() + " : ");
                if (participant.isAvailable(this.planningViewModel.getReservation().getValue())) {
                    stringBuilder.append(this.getString(R.string.available));
                } else {
                    stringBuilder.append(this.getString(R.string.unavailable));
                }
                labels.add(stringBuilder.toString());
            }

            String[] labelsArray = new String[allParticipants.size()];
            labels.toArray(labelsArray);
            boolean[] checkedArray = new boolean[allParticipants.size()];
            for (int i = 0; i < allParticipants.size(); i++) {
                if (this.formViewModel.getParticipants().getValue().contains(allParticipants.get(i))) {
                    checkedArray[i] = true;
                }
            }

            List<Participant> selected = new ArrayList<>();

            // Build the dialog
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
            dialogBuilder.setTitle(getString(R.string.select_participants));
            dialogBuilder.setIcon(getDrawable(R.drawable.ic_baseline_supervised_user_circle_24));

            dialogBuilder.setMultiChoiceItems(labelsArray, checkedArray, new DialogInterface.OnMultiChoiceClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                    checkedArray[which] = isChecked;

                }
            });

            dialogBuilder.setPositiveButton(this.getString(R.string.done), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    for(int i=0; i<checkedArray.length; i++) {
                        if(checkedArray[i]) {
                            selected.add(allParticipants.get(i));
                        }
                    }
                    formViewModel.setParticipants(selected);
                }
            });

            dialogBuilder.setNegativeButton(this.getString(R.string.cancel), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            dialogBuilder.setNeutralButton(this.getString(R.string.clear), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    selected.clear();
                    formViewModel.setParticipants(selected);
                }
            });
            Dialog dialog = dialogBuilder.create();
            dialog.show();
        });
    }

    private String formatParticipantsToString(List<Participant> participants) {
        StringBuilder stringBuilder = new StringBuilder("");
        if(participants != null && !participants.isEmpty()) {
            for(Participant participant: participants) {
                stringBuilder.append(participant.getFirstName() + ", ");
            }
        }
        String names = names = stringBuilder.toString();
        if(!names.isEmpty()) {
            names = names.substring(0, names.length() - 2);
        }
        return names;
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

        } catch (NullStartException e) {
            errorHandler.signalErrorInSnackbar(errorHandler.getMessage(e), this.saveButton);
        } catch (EmptySelectedParticipantsException e) {
            errorHandler.signalErrorInSnackbar(errorHandler.getMessage(e), this.saveButton);
        } catch (PassedStartException e) {
            errorHandler.signalErrorInSnackbar(errorHandler.getMessage(e), this.saveButton);
        } catch (NullEndException e) {
            errorHandler.signalErrorInSnackbar(errorHandler.getMessage(e), this.saveButton);
        } catch (PassedDatesException e) {
            errorHandler.signalErrorInSnackbar(errorHandler.getMessage(e), this.saveButton);
        } catch (InvalidEndException e) {
            errorHandler.signalErrorInSnackbar(errorHandler.getMessage(e), this.saveButton);
        } catch (EmptySubjectException e) {
            errorHandler.signalErrorInSnackbar(errorHandler.getMessage(e), this.saveButton);
        } catch (NullDatesException e) {
            errorHandler.signalErrorInSnackbar(errorHandler.getMessage(e), this.saveButton);
        } catch (UnavailablePlacesException e) {
            errorHandler.signalErrorInSnackbar(errorHandler.getMessage(e), this.saveButton);
        } catch (NullPlaceException e) {
            errorHandler.signalErrorInSnackbar(errorHandler.getMessage(e), this.saveButton);
        } catch (NullReunionException e) {
            errorHandler.signalErrorInSnackbar(errorHandler.getMessage(e), this.saveButton);
        }
    }

}