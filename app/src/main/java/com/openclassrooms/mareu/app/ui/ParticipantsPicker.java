package com.openclassrooms.mareu.app.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;

import androidx.annotation.NonNull;

import com.openclassrooms.mareu.R;
import com.openclassrooms.mareu.data.entities.Participant;
import com.openclassrooms.mareu.domain.events.SetParticipantsEvent;


import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

public class ParticipantsPicker extends Dialog {


    private List<Participant> participants;

    private String[] labels;

    private boolean[] checkedArray;

    public ParticipantsPicker(@NonNull Context context, @NonNull List<Participant> participants, @NonNull boolean[] checkedArray) {
        super(context);
        this.participants = participants;
        this.makeLabels();
        this.checkedArray = checkedArray;
    }

    private void makeLabels() {
        this.labels = new String[participants.size()];
        for(int i=0; i<participants.size(); i++) {
            labels[i] = participants.get(i).getFirstName();
        }
    }

    public void showParticipantsPickerDialog() {
        List<Participant> selected = new ArrayList<>();
        // Build the dialog
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this.getContext());
        dialogBuilder.setTitle(this.getContext().getString(R.string.select_participants));
        dialogBuilder.setMultiChoiceItems(this.labels, this.checkedArray, (DialogInterface dialog, int which, boolean isChecked) -> this.checkedArray[which] = isChecked);
        dialogBuilder.setPositiveButton(this.getContext().getString(R.string.done), (DialogInterface dialog, int which) -> {
            for(int i=0; i<checkedArray.length; i++) {
                if(checkedArray[i]) {
                    selected.add(this.participants.get(i));
                }
            }
            EventBus.getDefault().post(new SetParticipantsEvent(selected));
        });
        dialogBuilder.setNegativeButton(this.getContext().getString(R.string.cancel), (DialogInterface dialog, int which) -> dialog.dismiss());
        dialogBuilder.setNeutralButton(this.getContext().getString(R.string.clear), (DialogInterface dialog, int which) -> {
            selected.clear();
            EventBus.getDefault().post(new SetParticipantsEvent(selected));
        });
        Dialog dialog = dialogBuilder.create();
        dialog.show();
    }
}
