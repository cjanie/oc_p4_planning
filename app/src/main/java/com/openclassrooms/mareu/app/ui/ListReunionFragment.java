package com.openclassrooms.mareu.app.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.openclassrooms.mareu.R;
import com.openclassrooms.mareu.data.entities.Reunion;
import com.openclassrooms.mareu.domain.events.DeleteReunionEvent;
import com.openclassrooms.mareu.domain.events.InitSearchEvent;
import com.openclassrooms.mareu.domain.events.SearchByDateEvent;
import com.openclassrooms.mareu.domain.events.SearchByPlaceAndDateEvent;
import com.openclassrooms.mareu.domain.events.SearchByPlaceEvent;
import com.openclassrooms.mareu.data.exceptions.NullReunionException;
import com.openclassrooms.mareu.domain.viewmodels.PlanningViewModel;
import com.openclassrooms.mareu.domain.viewmodels.SearchViewModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

public class ListReunionFragment extends Fragment {

    private PlanningViewModel planningViewModel;

    private SearchViewModel searchViewModel;

    private RecyclerView recyclerView;



    private LiveData<List<Reunion>> getReunions() {
        return planningViewModel.getAllReunions();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        this.planningViewModel = new ViewModelProvider(this).get(PlanningViewModel.class);
        this.searchViewModel = new ViewModelProvider(this).get(SearchViewModel.class);

        View root = inflater.inflate(R.layout.fragment_reunion_list, container, false);
        Context context = root.getContext();
        this.recyclerView = (RecyclerView) root;
        this.recyclerView.setLayoutManager(new LinearLayoutManager(context));
        this.recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));

        return root;

    }

    @Override
    public void onResume() {
        super.onResume();
        this.initRecyclerView();
    }

    private void initRecyclerView() {
        if(this.getReunions() != null) {
            this.getReunions().observe(this, new Observer<List<Reunion>>() {
                @Override
                public void onChanged(List<Reunion> reunions) {
                    if(reunions != null) {
                        recyclerView.setAdapter(new ListReunionRecyclerViewAdapter(reunions));
                    }
                }
            });

        }
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onDeleteReunionEventFired(DeleteReunionEvent event) throws NullReunionException {
        this.planningViewModel.removeReunion(event.reunion);
        this.planningViewModel.getAllReunions().observe(this, reunions -> {
            recyclerView.setAdapter(new ListReunionRecyclerViewAdapter(reunions));
        });

    }

    @Subscribe
    public void onSearchByPlaceEventFired(SearchByPlaceEvent event) {
        List<Reunion> found = this.searchViewModel.searchReunionsByPlace(event.place).getValue();
        recyclerView.setAdapter(new ListReunionRecyclerViewAdapter(found));

    }

    @Subscribe
    public void onSearchByDateEventFired(SearchByDateEvent event) {
        // TODO
        List<Reunion> found = this.searchViewModel.searchReunionsByDate(event.localDate).getValue();
        this.recyclerView.setAdapter(new ListReunionRecyclerViewAdapter(found));

    }

    @Subscribe
    public void onSearchByPlaceAndDateEventFired(SearchByPlaceAndDateEvent event) {
        System.out.println("List Reunion Fragment received Fired event **************** on Search By Place And Date");
        List<Reunion> found = this.searchViewModel.searchReunionsByPlaceAndDate(event.place, event.date).getValue();
        this.recyclerView.setAdapter(new ListReunionRecyclerViewAdapter(found));
    }

    @Subscribe
    public void onInitSearchEventFired(InitSearchEvent event) {
        this.initRecyclerView();
    }

}
