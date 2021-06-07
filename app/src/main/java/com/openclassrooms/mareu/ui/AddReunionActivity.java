package com.openclassrooms.mareu.ui;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;


import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.openclassrooms.mareu.MainActivity;
import com.openclassrooms.mareu.R;
import com.openclassrooms.mareu.events.CommandEvent;
import com.openclassrooms.mareu.exceptions.EmptySelectedParticipantsException;
import com.openclassrooms.mareu.exceptions.EmptySubjectException;
import com.openclassrooms.mareu.exceptions.InvalidEndTimeException;
import com.openclassrooms.mareu.exceptions.NullReservationException;
import com.openclassrooms.mareu.exceptions.UnavailablePlacesException;
import com.openclassrooms.mareu.exceptions.NullDateException;
import com.openclassrooms.mareu.exceptions.NullEndTimeException;
import com.openclassrooms.mareu.exceptions.NullPlaceException;
import com.openclassrooms.mareu.exceptions.NullStartTimeException;
import com.openclassrooms.mareu.exceptions.PassedDatesException;
import com.openclassrooms.mareu.exceptions.PassedStartTimeException;
import com.openclassrooms.mareu.utils.ErrorHandler;
import com.openclassrooms.mareu.viewmodels.FormViewModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddReunionActivity extends AppCompatActivity implements TextWatcher, View.OnClickListener {

    private FormViewModel formViewModel;

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

        ButterKnife.bind(this);
        this.subject.getEditText().addTextChangedListener(this);
        this.saveButton.setOnClickListener(this);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        
    }

    private void signalSuccessInSnackbar(String success, View v) {
        int colorSuccess = ContextCompat.getColor(this, R.color.color_success);
        Snackbar.make(v, success, BaseTransientBottomBar.LENGTH_LONG).setBackgroundTint(colorSuccess).show();
    }

    private void navigateToMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        this.startActivity(intent);
    }


    @Override
    public void onClick(View v) {
         if(v == this.saveButton) {
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

    @Subscribe // At Command
    public void setData(CommandEvent event) {
        this.formViewModel.setPlace(event.place);
        this.formViewModel.setParticipants(event.participants);
    }

    private void onSave() {

        ErrorHandler errorHandler = new ErrorHandler(this.getApplicationContext());
        try {
            this.formViewModel.addReunion();
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
        }
    }
}