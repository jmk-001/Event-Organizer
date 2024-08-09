package com.example.assignment3.provider;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface EventManagementDAO {
    // Queries for EventCategory
    @Query("select * from EventCategory")
    LiveData<List<EventCategory>> getAllEventCategory();

    @Insert
    void addEventCategory(EventCategory category);

    @Query("delete from EventCategory")
    void deleteAllEventCategory();

    @Query("select exists(select * from EventCategory where categoryId like :categoryId)")
    boolean categoryExists(String categoryId);

    @Query("update EventCategory set eventCount = eventCount + 1 where categoryId like :categoryId")
    void incrementCategoryEventCount(String categoryId);
    @Query("update EventCategory set eventCount = eventCount - 1 where categoryId like :categoryId")
    void decrementCategoryEventCount(String categoryId);

    // Queries for Event
    @Query("select * from Event")
    LiveData<List<Event>> getAllEvent();

    @Insert
    void addEvent(Event event);

    @Query("delete from Event where eventId like :eventId")
    void deleteEventById(String eventId);
}
