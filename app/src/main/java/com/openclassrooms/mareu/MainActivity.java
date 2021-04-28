package com.openclassrooms.mareu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.openclassrooms.mareu.entities.Reunion;
import com.openclassrooms.mareu.ui.AddActivity;
import com.openclassrooms.mareu.ui.ListReunionRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    // Views
    private RecyclerView recyclerView;
    private FloatingActionButton fab;

    // Data
    private List<Reunion> reunions;

    private void loadData() {
        // TODO
    }

    // Override
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.recyclerView = this.findViewById(R.id.reunions_recycler_view);
        this.fab = this.findViewById(R.id.reunions_fab_add);
        this.reunions = new ArrayList<>();
        this.recyclerView.setAdapter(new ListReunionRecyclerViewAdapter(reunions));

        this.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        this.loadData();
    }

}