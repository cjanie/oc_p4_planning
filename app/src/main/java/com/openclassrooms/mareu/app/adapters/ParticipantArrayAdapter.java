package com.openclassrooms.mareu.app.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.openclassrooms.mareu.R;
import com.openclassrooms.mareu.data.entities.Participant;

import java.util.List;

public class ParticipantArrayAdapter extends ArrayAdapter<Participant> {

    private List<Participant> participants;
    public ParticipantArrayAdapter(@NonNull Context context, int resource, @NonNull List<Participant> participants) {
        super(context, resource);
        this.participants = participants;
    }

    @Nullable
    @Override
    public Participant getItem(int position) {
        return this.participants.get(position);
    }

    @Override
    public int getCount() {
        return this.participants.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Participant participant = this.getItem(position);
        LayoutInflater inflater = LayoutInflater.from(this.getContext());
        View itemView = inflater.inflate(R.layout.fragment_participant_list_item, null);
        ((CheckBox) itemView.findViewById(R.id.participant_check_box)).setText(participant.getFirstName());
        return itemView;
    }
}
