package com.openclassrooms.mareu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import com.openclassrooms.mareu.ui.AddReunionActivity;
import com.openclassrooms.mareu.ui.SearchFragment;

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
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        this.isSearchFragmentVisible(false);
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
                isSearchFragmentVisible(true);
                return true;
            case R.id.action_list:
                isSearchFragmentVisible(false);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void isSearchFragmentVisible(boolean isVisible) {
        if(isVisible) {
            this.searchFragment.setVisibility(View.VISIBLE); //  TODO with fragment layout manager?
        } else {
            this.searchFragment.setVisibility(View.GONE); // TODO
        }
    }

}