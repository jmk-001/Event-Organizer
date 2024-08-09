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
import com.example.assignment3.provider.EventCategory;
import com.example.assignment3.provider.EventManagementViewModel;
import com.google.gson.Gson;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentListCategory#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentListCategory extends Fragment {
    Gson gson = new Gson();
    ArrayList<EventCategory> data = new ArrayList<>();
    private RecyclerView categoryRecyclerView;
    CategoryRecyclerAdapter categoryRecyclerAdapter;
    EventManagementViewModel emViewModel;
    RecyclerView.LayoutManager layoutManager;

    public FragmentListCategory() {
        // Required empty public constructor
    }

    public static FragmentListCategory newInstance(String param1, String param2) {
        FragmentListCategory fragment = new FragmentListCategory();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        emViewModel = new ViewModelProvider(this).get(EventManagementViewModel.class);
        emViewModel.getAllEventCategory().observe(this, newData -> {
            categoryRecyclerAdapter.setData(new ArrayList<EventCategory>(newData));
            categoryRecyclerAdapter.notifyDataSetChanged();
        });
    }

//    public void addData(EventCategory category) {
//        boolean duplicateFound = false;
//        for (EventCategory d : data) {
//            if (d.getCategoryId().equals(category.getCategoryId()) ||
//                    d.getCategoryName().equals(category.getCategoryName())) {
//                Toast.makeText(getActivity(), "Category Already exists", Toast.LENGTH_SHORT).show();
//                duplicateFound = true;
//            }
//        }
//        if (!duplicateFound) {
//            this.data.add(category);
//            categoryRecyclerAdapter.setData(data);
//            categoryRecyclerAdapter.notifyDataSetChanged();
//
//            saveArrayListAsText();
//        }
//    }

//    public void resetData(){
//        this.data = new ArrayList<>();
//        categoryRecyclerAdapter.setData(this.data);
//        categoryRecyclerAdapter.notifyDataSetChanged();
//        saveArrayListAsText();
//    }

    public void decrementEventCount(ArrayList<Event> deletedData){
        for (Event e: deletedData){
            for (EventCategory c: this.data){
                if (e.getCategoryId().equals(c.getCategoryId())){
                    c.setEventCount(c.getEventCount()-1);
                }
            }
        }
        categoryRecyclerAdapter.notifyDataSetChanged();
        saveArrayListAsText();
    }

    public void saveArrayListAsText(){
        String arrayListString = gson.toJson(data);

        SharedPreferences.Editor edit = this.getActivity().getSharedPreferences(KeyStore.FILE_NAME, Context.MODE_PRIVATE).edit();
        edit.putString(KeyStore.CATEGORY_DATA, arrayListString);
        edit.apply();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_event_category, container, false);

        categoryRecyclerView = v.findViewById(R.id.categoryRecyclerView);

        // A Linear RecyclerView.LayoutManager implementation which provides similar functionality to ListView.
        layoutManager = new LinearLayoutManager(this.getActivity());
        // Also StaggeredGridLayoutManager and GridLayoutManager or a custom Layout manager
        categoryRecyclerView.setLayoutManager(layoutManager);

        categoryRecyclerAdapter = new CategoryRecyclerAdapter();
        categoryRecyclerView.setAdapter(categoryRecyclerAdapter);

        //loadFromSharedPreferences();

        return v;
    }

//    public void loadFromSharedPreferences(){
//        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences(KeyStore.FILE_NAME, Context.MODE_PRIVATE);
//        String categoryArrayString = sharedPreferences.getString(KeyStore.CATEGORY_DATA, "[]");
//
//        Type type = new TypeToken<ArrayList<EventCategory>>() {}.getType();
//        data = gson.fromJson(categoryArrayString, type);
//
//        categoryRecyclerAdapter.setData(data);
//        categoryRecyclerAdapter.notifyDataSetChanged();
//    }
}