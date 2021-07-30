package com.openclassrooms.mareu.app.ui;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.openclassrooms.mareu.R;
import com.openclassrooms.mareu.app.MainActivity;
import com.openclassrooms.mareu.app.adapters.PlaceArrayAdapter;
import com.openclassrooms.mareu.data.entities.Place;
import com.openclassrooms.mareu.data.entities.Reservation;
import com.openclassrooms.mareu.data.exceptions.EmptyAvailableParticipantsException;
import com.openclassrooms.mareu.data.exceptions.EmptySelectedParticipantsException;
import com.openclassrooms.mareu.data.exceptions.EmptySubjectException;
import com.openclassrooms.mareu.data.exceptions.InvalidEndDateException;
import com.openclassrooms.mareu.data.exceptions.InvalidEndException;
import com.openclassrooms.mareu.data.exceptions.InvalidEndTimeException;
import com.openclassrooms.mareu.data.exceptions.NullReservationException;
import com.openclassrooms.mareu.data.exceptions.NullReunionException;
import com.openclassrooms.mareu.data.exceptions.PassedStartDateException;
import com.openclassrooms.mareu.data.exceptions.PassedStartTimeException;
import com.openclassrooms.mareu.data.exceptions.UnavailablePlacesException;
import com.openclassrooms.mareu.data.exceptions.NullDatesException;
import com.openclassrooms.mareu.data.exceptions.NullEndException;
import com.openclassrooms.mareu.data.exceptions.NullPlaceException;
import com.openclassrooms.mareu.data.exceptions.NullStartException;
import com.openclassrooms.mareu.data.exceptions.PassedDatesException;
import com.openclassrooms.mareu.data.exceptions.PassedStartException;
import com.openclassrooms.mareu.app.utils.CustomDateTimeFormatter;
import com.openclassrooms.mareu.app.utils.ErrorHandler;
import com.openclassrooms.mareu.domain.events.SetParticipantsEvent;
import com.openclassrooms.mareu.domain.events.SetDateTimeEvent;
import com.openclassrooms.mareu.domain.events.SetEndEvent;
import com.openclassrooms.mareu.domain.events.SetStartEvent;
import com.openclassrooms.mareu.domain.viewmodels.FormViewModel;
import com.openclassrooms.mareu.domain.viewmodels.PlanningViewModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddReunionActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, TextWatcher {

    private FormViewModel formViewModel;
    private PlanningViewModel planningViewModel;
    private ErrorHandler errorHandler;

    @BindView(R.id.reunion_start_date_layout)
    TextInputLayout startDateLayout;
    @BindView(R.id.reunion_start_time_layout)
    TextInputLayout startTimeLayout;
    @BindView(R.id.reunion_end_date_layout)
    TextInputLayout endDateLayout;
    @BindView(R.id.reunion_end_time_layout)
    TextInputLayout endTimeLayout;
    @BindView(R.id.reunion_place_layout)
    TextInputLayout placeLayout;
    @BindView(R.id.reunion_place_spinner)
    AutoCompleteTextView placeSpinner;
    @BindView(R.id.reunion_participants_layout)
    TextInputLayout participantsLayout;
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
        this.placeSpinner.setOnItemClickListener(this);
        this.subject.getEditText().addTextChangedListener(this);

    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        // ui observes view model's data for default start and end
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
        this.getAvailableData();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        // Nothing
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        this.formViewModel.setSubject(this.subject.getEditText().getText().toString());
        if(!this.subject.getEditText().getText().toString().isEmpty()) {
            this.subject.setError(null);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
        // Nothing
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        this.formViewModel.setPlace((Place) this.placeSpinner.getAdapter().getItem(position));
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
    public void setDateTime(SetDateTimeEvent event) {
        try {
            if(event instanceof SetStartEvent) {
                this.formViewModel.setStart(event.dateTime);
            } else if(event instanceof SetEndEvent) {
                this.formViewModel.setEnd(event.dateTime);
            }
            this.getAvailableData();
        } catch (NullDatesException | NullStartException | NullEndException e) {
            errorHandler.signalError(errorHandler.getMessage(new NullDatesException()), startDateLayout);
        } catch (PassedStartDateException e) {
            errorHandler.signalError(errorHandler.getMessage(e), startDateLayout);
        } catch (PassedStartTimeException e) {
            errorHandler.signalError(errorHandler.getMessage(e), startTimeLayout);
        } catch (InvalidEndDateException e) {
            errorHandler.signalError(errorHandler.getMessage(e), endDateLayout);
        } catch (InvalidEndTimeException e) {
            errorHandler.signalError(errorHandler.getMessage(e), endTimeLayout);
        }
    }

    @Subscribe
    public void setParticipants(SetParticipantsEvent event) {
        this.formViewModel.setParticipants(event.selected);
        this.formViewModel.getParticipantsNames().observe(this, string -> this.participantsSpinner.setText(string));
    }

    @OnClick(R.id.reunion_start_date)
    public void showStartDateDialog() {
        //this.formViewModel.showStartDateDialog();
        new StartPicker(this, this.formViewModel.getStart().getValue()).showDatePickerDialog();
    }

    @OnClick(R.id.reunion_start_time)
    public void showStartTimeDialog(){
        new StartPicker(this, this.formViewModel.getStart().getValue()).showTimePickerDialog();
    }

    @OnClick(R.id.reunion_end_date)
    public void showEndDatePickerDialog() {
        new EndPicker(this, this.formViewModel.getEnd().getValue()).showDatePickerDialog();
    }

    @OnClick(R.id.reunion_end_time)
    public void showEndTimePickerDialog() {
        new EndPicker(this, this.formViewModel.getEnd().getValue()).showTimePickerDialog();
    }

    @OnClick(R.id.reunion_participants_spinner)
    public void showParticipantsDialog() {
        try {
            this.planningViewModel.getAvailableParticipants(new Reservation(this.formViewModel.getStart().getValue(), this.formViewModel.getEnd().getValue())).observe(this, availableParticipants -> {
                boolean[] checkedArray = new boolean[availableParticipants.size()];
                for (int i = 0; i < availableParticipants.size(); i++) {
                    if (this.formViewModel.getParticipants().getValue().contains(availableParticipants.get(i))) {
                        checkedArray[i] = true;
                    }
                }
                new ParticipantsPicker(this, availableParticipants, checkedArray).showParticipantsPickerDialog();
            });
        } catch (NullDatesException | NullStartException | NullEndException | PassedDatesException | PassedStartException | InvalidEndException | NullReservationException e) {
            this.errorHandler.signalError(this.errorHandler.getMessage(new NullReservationException()), this.startDateLayout);
        } catch (EmptyAvailableParticipantsException e) {
            this.errorHandler.signalError(this.errorHandler.getMessage(e), this.participantsLayout);
        }
        /*
        this.planningViewModel.getAllParticipants().observe(this, allParticipants -> {
            // Prepare items
            String[] labels = new String[0];
            try {
                labels = this.planningViewModel.getParticipantsLabels(allParticipants, new Reservation(this.formViewModel.getStart().getValue(), this.formViewModel.getEnd().getValue())).getValue();
            } catch (NullDatesException | NullStartException | NullEndException | PassedDatesException | PassedStartException | InvalidEndException e) {
                this.errorHandler.signalError(this.errorHandler.getMessage(new NullReservationException()), this.participantsLayout);
            }
            boolean[] checkedArray = new boolean[allParticipants.size()];
            for (int i = 0; i < allParticipants.size(); i++) {
                if (this.formViewModel.getParticipants().getValue().contains(allParticipants.get(i))) {
                    checkedArray[i] = true;
                }
            }
            new ParticipantsPicker(this, allParticipants, labels, checkedArray).showParticipantsPickerDialog();
        });

         */
    }

    /*
    @OnClick(R.id.next_reservation_fab)
    public void onForward() {
        this.formViewModel.forward();
        this.getAvailableData();
    }
    */


    @OnClick(R.id.save_reunion_button)
    public void onSave() {
        try {
            this.formViewModel.save();
            Intent intent = new Intent(this, MainActivity.class);
            this.startActivity(intent);
        } catch (NullStartException | NullEndException | NullDatesException | PassedDatesException | PassedStartException | InvalidEndException e) {
            this.errorHandler.signalError(this.errorHandler.getMessage(new NullReservationException()), this.startDateLayout);
        } catch (EmptySubjectException e) {
            this.errorHandler.signalError(this.errorHandler.getMessage(e), this.subject);
        } catch (NullPlaceException e) {
            this.errorHandler.signalError(this.errorHandler.getMessage(e), this.placeLayout);
        } catch (EmptySelectedParticipantsException e) {
            this.errorHandler.signalError(this.errorHandler.getMessage(e), this.participantsLayout);
        } catch (NullReunionException e) {
            this.errorHandler.signalError(this.errorHandler.getMessage(e), this.saveButton);
        }
    }

    private void getAvailableData() {
        // reset spinners data
        this.placeLayout.getEditText().setError(null);
        this.placeLayout.setError(null);
        this.placeSpinner.setAdapter(new PlaceArrayAdapter(this, 0, new ArrayList<>()));
        this.formViewModel.setPlace(null);
        this.participantsLayout.getEditText().setError(null);
        this.participantsLayout.setError(null);
        this.formViewModel.getParticipants().getValue().clear();

        try {
            Reservation reservation = new Reservation(this.formViewModel.getStart().getValue(), this.formViewModel.getEnd().getValue());
            // Observe data for place spinner
            this.planningViewModel.getAvailablePlaces(reservation).observe(this, availablePlaces -> {
                this.placeSpinner.setAdapter(new PlaceArrayAdapter(this, 0, availablePlaces));
                this.formViewModel.setPlace(availablePlaces.get(0));
            });
            // Observe data to set defaut selected participants
            this.planningViewModel.getAvailableParticipants(reservation).observe(this, availableParticipants -> {
                this.formViewModel.getParticipants().getValue().add(availableParticipants.get(0));
                if(availableParticipants.size() > 1) {
                    this.formViewModel.getParticipants().getValue().add(availableParticipants.get(1));
                }
            });
        } catch (NullReservationException | NullDatesException | NullStartException | NullEndException | PassedDatesException | PassedStartException | InvalidEndException e) {
            this.errorHandler.signalError(this.errorHandler.getMessage(new NullReservationException()), this.placeLayout);
        } catch (UnavailablePlacesException e) {
            this.errorHandler.signalError(this.errorHandler.getMessage(e), this.placeLayout);
        } catch (EmptyAvailableParticipantsException e) {
            this.errorHandler.signalError(this.errorHandler.getMessage(e), this.participantsLayout);
        }
        // Observe selected for place spinner
        this.formViewModel.getPlace().observe(this, selectedPlace -> {
            if(selectedPlace != null) {
                this.placeSpinner.setText(selectedPlace.getName());
            }
        });
        // Observe selected for participants spinner
        this.formViewModel.getParticipantsNames().observe(this, string -> this.participantsSpinner.setText(string));
    }

}