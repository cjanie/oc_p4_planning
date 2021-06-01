package com.openclassrooms.mareu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

    /**
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = this.getMenuInflater();
        inflater.inflate(R.menu.menu_action_bar, menu);
        return true;
    }

    /**
     * Menu
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_search:
                this.navigateToSearchActivity();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void navigateToSearchActivity() {
        Intent intent = new Intent(this, SearchActivity.class);
        this.startActivity(intent);
    }
}