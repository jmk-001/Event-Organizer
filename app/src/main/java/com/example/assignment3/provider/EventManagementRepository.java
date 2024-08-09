package com.example.assignment3.provider;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class EventManagementRepository {
    private EventManagementDAO eventManagementDAO;
    private LiveData<List<EventCategory>> allEventCategoryLiveData;
    private LiveData<List<Event>> allEventLiveData;

    EventManagementRepository(Application application) {
        EventManagementDatabase db = EventManagementDatabase.getDatabase(application);

        eventManagementDAO = db.eventManagementDAO();

        // initialise live data
        allEventCategoryLiveData = eventManagementDAO.getAllEventCategory();
        allEventLiveData = eventManagementDAO.getAllEvent();
    }

    // Event Category
    LiveData<List<EventCategory>> getAllEventCategory() {
        return allEventCategoryLiveData;
    }

    void insert(EventCategory category) {
        EventManagementDatabase.databaseWriteExecutor.execute(() -> eventManagementDAO.addEventCategory(category));
    }

    void deleteAllEventsCategory(){
        EventManagementDatabase.databaseWriteExecutor.execute(() -> eventManagementDAO.deleteAllEventCategory());
    }

    boolean categoryExists(String categoryId){
        return eventManagementDAO.categoryExists(categoryId);
    }

    void incrementCategoryEventCount(String categoryId){
        EventManagementDatabase.databaseWriteExecutor.execute(() -> eventManagementDAO.incrementCategoryEventCount(categoryId));
    }

    void decrementCategoryEventCount(String categoryId){
        EventManagementDatabase.databaseWriteExecutor.execute(() -> eventManagementDAO.decrementCategoryEventCount(categoryId));
    }

    // Event
    LiveData<List<Event>> getAllEvent() {
        return allEventLiveData;
    }

    void insert(Event event) {
        EventManagementDatabase.databaseWriteExecutor.execute(() -> eventManagementDAO.addEvent(event));
    }

    void deleteEventById(String eventId){
        EventManagementDatabase.databaseWriteExecutor.execute(() -> eventManagementDAO.deleteEventById(eventId));
    }
}
