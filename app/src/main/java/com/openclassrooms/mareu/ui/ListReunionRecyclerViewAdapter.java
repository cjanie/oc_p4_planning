package com.openclassrooms.mareu.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.openclassrooms.mareu.R;
import com.openclassrooms.mareu.entities.Reunion;

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

    @Override
    public void onBindViewHolder(@NonNull ListReunionRecyclerViewAdapter.ViewHolder holder, int position) {
        Reunion reunion = this.reunions.get(position);
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
