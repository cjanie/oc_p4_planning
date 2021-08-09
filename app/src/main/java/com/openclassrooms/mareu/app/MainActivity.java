package com.openclassrooms.mareu.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import com.openclassrooms.mareu.R;
import com.openclassrooms.mareu.app.ui.AddReunionActivity;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.reunions_fab_add)
    FloatingActionButton fab;

    @BindView(R.id.search_fragment)
    View searchFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        this.searchFragment.setVisibility(View.GONE);
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
     * MENU
     * https://developer.android.com/guide/topics/ui/menus
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
                if(this.searchFragment.getVisibility() == View.GONE) {
                    this.searchFragment.setVisibility(View.VISIBLE);
                    item.setIcon(R.drawable.ic_baseline_clear_24);
                } else {
                    this.searchFragment.setVisibility(View.GONE);
                    item.setIcon(R.drawable.ic_baseline_filter_list_24);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
