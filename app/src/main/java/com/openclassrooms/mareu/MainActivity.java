package com.openclassrooms.mareu;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import com.openclassrooms.mareu.ui.AddReunionActivity;
import com.openclassrooms.mareu.ui.SearchActivity;
import com.openclassrooms.mareu.viewmodels.SearchViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.reunions_fab_add)
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = this.getSupportActionBar(); // To call the action bar



        ButterKnife.bind(this);
        this.fab.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v == this.fab) {
            Intent intent = new Intent(this, AddReunionActivity.class);
            this.startActivity(intent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }
}