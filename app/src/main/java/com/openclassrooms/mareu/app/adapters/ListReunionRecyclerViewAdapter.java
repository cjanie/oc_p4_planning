package com.openclassrooms.mareu.app.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.openclassrooms.mareu.R;
import com.openclassrooms.mareu.data.entities.Reunion;
import com.openclassrooms.mareu.domain.events.DeleteReunionEvent;
import com.openclassrooms.mareu.app.utils.CustomDateTimeFormatter;

import org.greenrobot.eventbus.EventBus;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ListReunionRecyclerViewAdapter extends RecyclerView.Adapter<ListReunionRecyclerViewAdapter.ViewHolder> {

    private final List<Reunion> reunions;

    public ListReunionRecyclerViewAdapter(List<Reunion> reunions) {
        this.reunions = reunions;
    }

    @NonNull
    @Override
    public ListReunionRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_reunion_list_item, parent, false);
        return new ListReunionRecyclerViewAdapter.ViewHolder(view);
    }

    private String formatTime(LocalTime time) {

        CustomDateTimeFormatter customDateTimeFormatter = new CustomDateTimeFormatter();
        String formatted = customDateTimeFormatter.formatTimeToString(time);
        formatted = formatted.replace(':', 'h');
        return formatted;
    }

    @Override
    public void onBindViewHolder(@NonNull ListReunionRecyclerViewAdapter.ViewHolder holder, int position) {
        Reunion reunion = this.reunions.get(position);
        CustomDateTimeFormatter customDateTimeFormatter = new CustomDateTimeFormatter();
        holder.mainInformations.setText(
                reunion.getSubject() + " - "
                        + this.formatTime(reunion.getStart().toLocalTime()) + " - "
                        + reunion.getPlace().getName());

        LocalDateTime now = LocalDateTime.now();
         if(reunion.getStart().isBefore(now)) {
             if(reunion.getEnd().isBefore(now)) {
                 holder.image.setImageResource(R.drawable.circle_red);
             } else if(reunion.getEnd().isAfter(now)) {
                 holder.image.setImageResource(R.drawable.circle_orange);
             }
        } else if(reunion.getStart().isAfter(LocalDateTime.now())) {
            holder.image.setImageResource(R.drawable.circle_green);
        }
        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new DeleteReunionEvent(reunion));
            }
        });
        if(reunion.getParticipants() != null) {
            if(!reunion.getParticipants().isEmpty()) {
                String string = "";
                for(int i = 0; i<reunion.getParticipants().size(); i++) {
                    string += reunion.getParticipants().get(i).getEmail() + ", ";
                }
                string = string.substring(0, string.length() - 2);
                holder.participants.setText(string);
            }
        }
    }


    @Override
    public int getItemCount() {
        return this.reunions.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.image)
        ImageView image;
        @BindView(R.id.main_informations)
        TextView mainInformations;
        @BindView(R.id.participants)
        TextView participants;
        @BindView(R.id.delete_button)
        ImageView deleteButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.main_informations)
        public void showMoreMainInformations() {
            this.mainInformations.setMaxLines(5);
        }

        @OnClick(R.id.participants)
        public void showMoreParticipants() {
            this.participants.setMaxLines(6);
        }
    }
}
