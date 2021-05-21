package com.openclassrooms.mareu.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
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
import com.openclassrooms.mareu.adapters.ParticipantArrayAdapter;
import com.openclassrooms.mareu.adapters.PlaceArrayAdapter;
import com.openclassrooms.mareu.api.ReunionService;
import com.openclassrooms.mareu.entities.Participant;
import com.openclassrooms.mareu.entities.Place;
import com.openclassrooms.mareu.entities.Reservation;
import com.openclassrooms.mareu.entities.Reunion;
import com.openclassrooms.mareu.exceptions.InvalidEndTimeException;
import com.openclassrooms.mareu.exceptions.NullDatesException;
import com.openclassrooms.mareu.exceptions.NullEndTimeException;
import com.openclassrooms.mareu.exceptions.NullStartTimeException;
import com.openclassrooms.mareu.exceptions.PassedDatesException;
import com.openclassrooms.mareu.exceptions.PassedStartTimeException;
import com.openclassrooms.mareu.exceptions.IsUnavailableException;
import com.openclassrooms.mareu.utils.CustomDateTimeFormatter;
import com.openclassrooms.mareu.viewmodels.PlanningViewModel;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;


public class AddReunionActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    TextInputLayout subject;

    TextInputLayout dateLayout;
    TextInputEditText dateInput;
    TextInputLayout startLayout;
    TextInputEditText startInput;
    TextInputLayout endLayout;
    TextInputEditText endInput;
    AutoCompleteTextView placeSpinner;

    AutoCompleteTextView participantsSpinner;

    Button saveButton;

    private PlanningViewModel planningViewModel;

    private LocalDate today;
    private LocalDate reunionDate;
    private LocalDateTime start;
    private LocalDateTime end;

    private Reservation reservation;
    private Place place;
    private List<Participant> selectedParticipants;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reunion);
        ActionBar actionBar = this.getSupportActionBar(); // calling the action bar
        actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_24); // customize the back button (optional)
        actionBar.setDisplayHomeAsUpEnabled(true); // showing the back button in action bar

        this.planningViewModel = new ViewModelProvider(this).get(PlanningViewModel.class);

        this.selectedParticipants = new ArrayList<>();

        this.subject = this.findViewById(R.id.reunion_subject_layout);

        this.dateLayout = this.findViewById(R.id.reunion_date_layout);
        this.dateInput = this.findViewById(R.id.reunion_date);
        this.startLayout = this.findViewById(R.id.reunion_start_layout);
        this.startInput = this.findViewById(R.id.reunion_start);
        this.endLayout = this.findViewById(R.id.reunion_end_layout);
        this.endInput =  this.findViewById(R.id.reunion_end);

        this.placeSpinner = this.findViewById(R.id.reunion_place_spinner);
        this.participantsSpinner = this.findViewById(R.id.reunion_participants_spinner);

        this.saveButton = this.findViewById(R.id.save_reunion_button);

        this.dateLayout.setOnClickListener(this);
        this.dateInput.setOnClickListener(this);
        this.startLayout.setOnClickListener(this);
        this.startInput.setOnClickListener(this);
        this.endLayout.setOnClickListener(this);
        this.endInput.setOnClickListener(this);

        this.placeSpinner.setOnItemClickListener(this);
        this.participantsSpinner.setOnClickListener(this);

        this.saveButton.setOnClickListener(this);

    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        this.today = LocalDate.now();
        this.dateInput.setText(new CustomDateTimeFormatter().formatDateToString(today));
    }

    private void configurePlaceSpinner(List<Place> places) {
        if(places != null && !places.isEmpty()) {
            PlaceArrayAdapter placeArrayAdapter = new PlaceArrayAdapter(this, 0, places);
            placeSpinner.setAdapter(placeArrayAdapter);
            placeSpinner.setOnItemClickListener(this);
            placeSpinner.setText(places.get(0).getName());
            place = places.get(0);
        }
    }

    private void signalErrorInSnackbar(String error, View v) {
        Snackbar.make(v, error, Snackbar.LENGTH_LONG).setBackgroundTint(this.getResources().getColor(R.color.color_accent)).show();
    }

    private void showDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                LocalDate selected = LocalDate.of(year, month + 1, dayOfMonth);
                if(selected.isBefore(today)) {
                    signalErrorInSnackbar(getString(R.string.error_date_passed), dateInput);
                } else {
                    reunionDate = selected;
                    dateInput.setText(new CustomDateTimeFormatter().formatDateToString(reunionDate));
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
        }, today.getYear(), today.getMonthValue() - 1, this.today.getDayOfMonth());
        datePickerDialog.setTitle(this.getString(R.string.select_date));
        datePickerDialog.show();
    }

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
                    startInput.setText(new CustomDateTimeFormatter().formatTimeToString(start));
                    endInput.setText(null);
                }
            }
        }, hourNow, minNow, true); // True for 24 hour time
        timePickerDialog.setTitle(this.getString(R.string.select_start_time));
        timePickerDialog.show();
    }

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
                        endInput.setText(new CustomDateTimeFormatter().formatTimeToString(end));
                        try {
                            reservation = new Reservation(start, end);
                            planningViewModel.getAvailablePlaces(reservation).observe(AddReunionActivity.this, new Observer<List<Place>>() {
                                @Override
                                public void onChanged(List<Place> places) {
                                    configurePlaceSpinner(places);
                                }
                            });

                        } catch (NullDatesException e) {
                            e.printStackTrace();
                            signalErrorInSnackbar(e.getMessage(), startInput);
                        } catch (NullStartTimeException e) {
                            e.printStackTrace();
                            signalErrorInSnackbar(e.getMessage(), startInput);
                        } catch (NullEndTimeException e) {
                            e.printStackTrace();
                            signalErrorInSnackbar(e.getMessage(), endInput);
                        } catch (PassedDatesException e) {
                            e.printStackTrace();
                            signalErrorInSnackbar(e.getMessage(), startInput);
                        } catch (PassedStartTimeException e) {
                            e.printStackTrace();
                            signalErrorInSnackbar(e.getMessage(), startInput);
                        } catch (InvalidEndTimeException e) {
                            e.printStackTrace();
                            signalErrorInSnackbar(e.getMessage(), endInput);
                        }
                    }
                }
            }, this.start.getHour(), this.start.getMinute(), true); // True for 24 hour time
            timePickerDialog.setTitle(this.getString(R.string.select_end_time));
            timePickerDialog.show();
        } else {
            this.signalErrorInSnackbar(this.getString(R.string.error_no_start_time_selected), this.endInput);
        }
    }

    private void showParticipantsSelectionDialog() {
        this.planningViewModel.getParticipants().observe(this, new Observer<List<Participant>>() {
            @Override
            public void onChanged(List<Participant> participants) {
                if(participants != null) {
                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(AddReunionActivity.this);
                    dialogBuilder.setTitle(R.string.select_participants);
                    List<String> participantsLabel = new ArrayList<>(); // Make the list of participants (name + is available state)
                    for(int i=0; i<participants.size(); i++) {
                        String participantLabel = participants.get(i).getFirstName() + " - Available: " + participants.get(i).isAvailable(reservation);
                        participantsLabel.add(participantLabel);
                    }
                    String[] participantsLabelArray = new String[participants.size()]; // Convert the list into array
                    participantsLabel.toArray(participantsLabelArray);
                    boolean[] checkedParticipants = new boolean[participants.size()]; // Make an array of boolean in regard to the array of participants
                    for(int i=0; i<participants.size(); i++) {
                        if(selectedParticipants.contains(participants.get(i))) {
                            checkedParticipants[i] = true;
                        }
                    }
                    dialogBuilder.setMultiChoiceItems(participantsLabelArray, checkedParticipants, new DialogInterface.OnMultiChoiceClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                            checkedParticipants[which] = isChecked;

                            Participant currentParticipant = participants.get(which);
                            if(checkedParticipants[which] == true) {
                                if(!selectedParticipants.contains(currentParticipant)) {
                                    selectedParticipants.add(currentParticipant);
                                }
                            } else {
                                if(selectedParticipants.contains(currentParticipant)) {
                                    selectedParticipants.remove(currentParticipant);
                                }
                            }
                        }
                    });

                    dialogBuilder.setCancelable(false);

                    dialogBuilder.setPositiveButton(R.string.done, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            for(int i=0; i<checkedParticipants.length; i++) {
                                if(checkedParticipants[i]) {
                                    List<String> selectedParticipantsToString = new ArrayList<>();
                                    for(int j=0; j<selectedParticipants.size(); j++) {
                                        selectedParticipantsToString.add(selectedParticipants.get(j).getFirstName());
                                    }
                                    String listAsString = "";
                                    for(int j=0; j<selectedParticipantsToString.size(); j++) {
                                        listAsString += selectedParticipantsToString.get(j) + ", ";
                                    }
                                    listAsString = listAsString.substring(0, listAsString.length() - 2);
                                    participantsSpinner.setText(listAsString);
                                }
                            }
                        }
                    });

                    dialogBuilder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // TODO
                        }
                    });

                    dialogBuilder.setNeutralButton(R.string.clear, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            for(int i=0; i<checkedParticipants.length; i++) {
                                checkedParticipants[i] = false;
                                selectedParticipants.clear();
                                participantsSpinner.setText("");
                            }
                        }
                    });

                    AlertDialog alertDialog = dialogBuilder.create();
                    alertDialog.show();
                }
            }
        });
    }

    private void navigateToMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        this.startActivity(intent);
        this.finish();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        this.place = (Place) placeSpinner.getAdapter().getItem(position);
    }

    @Override
    public void onClick(View v) {
        if(v == this.dateLayout || v == this.dateInput) {
            this.showDatePickerDialog();
        } else if(v == this.startLayout || v == this.startInput) {
            this.showStartTimePickerDialog();
        } else if(v == this.endLayout || v == this.endInput) {
            this.showEndTimePickerDialog();
        } else if(v == this.participantsSpinner) {
            this.showParticipantsSelectionDialog();
        } else if(v == this.saveButton) {

            if(!TextUtils.isEmpty(this.subject.getEditText().getText())) {
                if(this.place != null) {
                    if(this.start != null) {
                        if(this.end != null) {
                            if(this.reservation != null) {
                                try {
                                    this.place.reserve(reservation);
                                } catch (IsUnavailableException e) {
                                    e.printStackTrace();
                                    this.signalErrorInSnackbar(e.getMessage(), this.placeSpinner);
                                }
                            }
                            Reunion reunion = null;
                            try {
                                reunion = new Reunion(this.reservation.getStart(), this.reservation.getEnd());
                            } catch (NullDatesException e) {
                                e.printStackTrace();
                            } catch (NullStartTimeException e) {
                                e.printStackTrace();
                            } catch (NullEndTimeException e) {
                                e.printStackTrace();
                            } catch (PassedDatesException e) {
                                e.printStackTrace();
                            } catch (PassedStartTimeException e) {
                                e.printStackTrace();
                            } catch (InvalidEndTimeException e) {
                                e.printStackTrace();
                            }
                            reunion.setSubject(this.subject.getEditText().getText().toString());
                            reunion.setPlace(this.place);
                            for(int i=0; i<selectedParticipants.size(); i++) {
                                selectedParticipants.get(i).getReservations().add(this.reservation);
                            }
                            reunion.setParticipants(this.selectedParticipants);
                            ReunionService.getInstance().addReunion(reunion);
                            this.navigateToMainActivity();

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
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.navigateToMainActivity();
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}