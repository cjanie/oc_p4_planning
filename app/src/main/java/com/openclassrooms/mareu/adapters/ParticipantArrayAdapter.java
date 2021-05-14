package com.openclassrooms.mareu.adapters;

import android.content.Context;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;

import com.openclassrooms.mareu.entities.Participant;

public class ParticipantArrayAdapter extends ArrayAdapter<Participant> {
    public ParticipantArrayAdapter(@NonNull Context context, int resource) {
        super(context, resource);
    }
}
