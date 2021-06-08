package com.openclassrooms.mareu.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.openclassrooms.mareu.R;
import com.openclassrooms.mareu.adapters.PlaceArrayAdapter;
import com.openclassrooms.mareu.entities.Place;
import com.openclassrooms.mareu.events.ReservationEvent;
import com.openclassrooms.mareu.exceptions.EmptyAvailableParticipantsException;
import com.openclassrooms.mareu.exceptions.NullReservationException;
import com.openclassrooms.mareu.exceptions.UnavailablePlacesException;
import com.openclassrooms.mareu.utils.ErrorHandler;
import com.openclassrooms.mareu.viewmodels.FormViewModel;
import com.openclassrooms.mareu.viewmodels.PlanningViewModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AvailableFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_available, container, false);

        return root;
    }


}
