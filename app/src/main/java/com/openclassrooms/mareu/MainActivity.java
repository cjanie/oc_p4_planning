package com.openclassrooms.mareu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import com.openclassrooms.mareu.ui.AddReunionActivity;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    private FloatingActionButton fab;



    // Override
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.fab = this.findViewById(R.id.reunions_fab_add);
        this.fab.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v == this.fab) {
            Intent intent = new Intent(this, AddReunionActivity.class);
            this.startActivity(intent);
            this.finish();
        }
    }
}