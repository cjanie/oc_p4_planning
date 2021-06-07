package com.openclassrooms.mareu.ui;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.openclassrooms.mareu.R;
import com.openclassrooms.mareu.entities.Reservation;
import com.openclassrooms.mareu.events.ReservationEvent;
import com.openclassrooms.mareu.exceptions.InvalidEndDateException;
import com.openclassrooms.mareu.exceptions.InvalidEndTimeException;
import com.openclassrooms.mareu.exceptions.NullDateException;
import com.openclassrooms.mareu.exceptions.NullEndTimeException;
import com.openclassrooms.mareu.exceptions.NullStartTimeException;
import com.openclassrooms.mareu.exceptions.PassedDatesException;
import com.openclassrooms.mareu.exceptions.PassedStartDateException;
import com.openclassrooms.mareu.exceptions.PassedStartTimeException;
import com.openclassrooms.mareu.utils.CustomDateTimeFormatter;
import com.openclassrooms.mareu.utils.ErrorHandler;
import com.openclassrooms.mareu.viewmodels.FormViewModel;

import org.greenrobot.eventbus.EventBus;

import java.time.LocalDateTime;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReservationFragment extends Fragment implements View.OnClickListener {

    private FormViewModel formViewModel;

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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        this.formViewModel = new ViewModelProvider(this).get(FormViewModel.class);
        this.errorHandler = new ErrorHandler(this.getContext());

        View root = inflater.inflate(R.layout.fragment_reservation, container, false);

        ButterKnife.bind(this, root);

        this.startDateLayout.setOnClickListener(this);
        this.startDateInput.setOnClickListener(this);
        this.startTimeLayout.setOnClickListener(this);
        this.startTimeInput.setOnClickListener(this);
        this.endDateLayout.setOnClickListener(this);
        this.endDateInput.setOnClickListener(this);
        this.endTimeLayout.setOnClickListener(this);
        this.endTimeInput.setOnClickListener(this);

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
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

        this.onReserve(); // the fragment sends a reservation event at init so that available fragment cant set ui with data depending of the reservation
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
        }
    }

    // START DATE PICKER
    private void showStartDatePickerDialog() {

        // Set UI from Data
        // Test considering default getMonthValue: 5

        LocalDateTime defaultDateTime = this.formViewModel.getStart().getValue();


        DatePickerDialog datePickerDialog = new DatePickerDialog(this.getContext(), new DatePickerDialog.OnDateSetListener() {
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

    private void showEndDatePickerDialog() {
        LocalDateTime defaultDateTime = formViewModel.getEnd().getValue();
        DatePickerDialog datePickerDialog = new DatePickerDialog(this.getContext(), new DatePickerDialog.OnDateSetListener() {
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


    private void showStartTimePickerDialog() {
        LocalDateTime defaultDateTime = this.formViewModel.getStart().getValue();
        if(defaultDateTime != null) {
            TimePickerDialog timePickerDialog = new TimePickerDialog(this.getContext(), (TimePicker view, int hourOfDay, int minute) -> {
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


    private void showEndTimePickerDialog() {
        LocalDateTime defaultDateTime = this.formViewModel.getEnd().getValue();
        if(defaultDateTime != null) {
            TimePickerDialog timePickerDialog = new TimePickerDialog(this.getContext(), (TimePicker view, int hourOfDay, int minute) -> {
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
}
