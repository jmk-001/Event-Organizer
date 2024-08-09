package com.example.assignment3;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.assignment3.provider.Event;
import com.example.assignment3.provider.EventManagementViewModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentListEvent#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentListEvent extends Fragment {
    private RecyclerView eventRecyclerView;
    public EventRecyclerAdapter eventRecyclerAdapter;
    private RecyclerView.LayoutManager layoutManager;
    Gson gson = new Gson();
    ArrayList<Event> data = new ArrayList<>();

    // Room DB variables
    EventManagementViewModel emViewModel;

    public FragmentListEvent() {
        // Required empty public constructor
    }

    public static FragmentListEvent newInstance(String param1, String param2) {
        FragmentListEvent fragment = new FragmentListEvent();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        emViewModel = new ViewModelProvider(this).get(EventManagementViewModel.class);
        emViewModel.getAllEvent().observe(this, newData -> {
            eventRecyclerAdapter.setData(new ArrayList<Event>(newData));
            eventRecyclerAdapter.notifyDataSetChanged();
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_event, container, false);

        eventRecyclerView = v.findViewById(R.id.eventRecyclerView);

        layoutManager = new LinearLayoutManager(this.getActivity());
        eventRecyclerView.setLayoutManager(layoutManager);

        eventRecyclerAdapter = new EventRecyclerAdapter();
        eventRecyclerView.setAdapter(eventRecyclerAdapter);

        loadFromSharedPreferences();

        return v;
    }

    public void addData(Event event) {
        this.data.add(event);
        eventRecyclerAdapter.setData(data);
        eventRecyclerAdapter.notifyDataSetChanged();

        saveArrayListAsText();
    }

    public void resetData(){
        this.data = new ArrayList<>();
        eventRecyclerAdapter.setData(this.data);
        eventRecyclerAdapter.notifyDataSetChanged();
        saveArrayListAsText();
    }

    public void saveArrayListAsText(){
        String arrayListString = gson.toJson(data);

        SharedPreferences.Editor edit = this.getActivity().getSharedPreferences(KeyStore.FILE_NAME, Context.MODE_PRIVATE).edit();
        edit.putString(KeyStore.EVENT_DATA, arrayListString);
        edit.apply();
    }

    public void loadFromSharedPreferences(){
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences(KeyStore.FILE_NAME, Context.MODE_PRIVATE);
        String eventsArrayString = sharedPreferences.getString(KeyStore.EVENT_DATA, "[]");

        Type type = new TypeToken<ArrayList<Event>>() {}.getType();
        data = gson.fromJson(eventsArrayString, type);

        if (eventRecyclerAdapter != null) {
            eventRecyclerAdapter.setData(data);
            eventRecyclerAdapter.notifyDataSetChanged();
        }
    }
}