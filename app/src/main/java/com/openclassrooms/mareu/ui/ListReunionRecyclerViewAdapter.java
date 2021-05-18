package com.openclassrooms.mareu.ui;

import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.openclassrooms.mareu.R;
import com.openclassrooms.mareu.entities.Reunion;
import com.openclassrooms.mareu.events.DeleteReunionEvent;
import com.openclassrooms.mareu.utils.CustomDateTimeFormatter;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

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

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull ListReunionRecyclerViewAdapter.ViewHolder holder, int position) {
        Reunion reunion = this.reunions.get(position);
        CustomDateTimeFormatter customDateTimeFormatter = new CustomDateTimeFormatter();
        holder.mainInformations.setText(
                holder.itemView.getContext().getString(R.string.reunion) + " " + reunion.getSubject()
                        + " - " + customDateTimeFormatter.formatTimeToString(reunion.getStart()) + "-" + customDateTimeFormatter.formatTimeToString(reunion.getEnd()) + " - " + reunion.getPlace().getName()
        );
        Glide.with(holder.image.getContext()).applyDefaultRequestOptions(RequestOptions.circleCropTransform());
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

        private ImageView image;
        private TextView mainInformations;
        private TextView participants;
        private ImageView deleteButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.image = itemView.findViewById(R.id.image);
            this.mainInformations = itemView.findViewById(R.id.main_informations);
            this.participants = itemView.findViewById(R.id.participants);
            this.deleteButton = itemView.findViewById(R.id.delete_button);
        }
    }
}
