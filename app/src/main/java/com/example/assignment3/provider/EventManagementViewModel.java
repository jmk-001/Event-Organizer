package com.example.assignment3.provider;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class EventManagementViewModel extends AndroidViewModel {
    private EventManagementRepository repository;
    // private class variable to temporary hold all the items retrieved and pass outside of this class
    private LiveData<List<EventCategory>> allEventCategoryLiveData;
    private LiveData<List<Event>> allEventLiveData;

    public EventManagementViewModel(@NonNull Application application) {
        super(application);

        // get reference to the repository class
        repository = new EventManagementRepository(application);

        // get all items by calling method defined in repository class
        allEventCategoryLiveData = repository.getAllEventCategory();
        allEventLiveData = repository.getAllEvent();
    }

    // Event Category
    public LiveData<List<EventCategory>> getAllEventCategory() {
        return allEventCategoryLiveData;
    }

    public void insert(EventCategory category) {
        repository.insert(category);
    }

    public void deleteAll(){
        repository.deleteAllEventsCategory();
    }
    public boolean categoryExists(String categoryId){
        return repository.categoryExists(categoryId);
    }

    public void incrementCategoryEventCount(String categoryId){
        repository.incrementCategoryEventCount(categoryId);
    }

    public void decrementCategoryEventCount(String categoryId){
        repository.decrementCategoryEventCount(categoryId);
    }

    // Event
    public LiveData<List<Event>> getAllEvent() {
        return allEventLiveData;
    }

    public void insert(Event event) {
        repository.insert(event);
    }

    public void deleteEventById(String eventId){
        repository.deleteEventById(eventId);
    }
}
