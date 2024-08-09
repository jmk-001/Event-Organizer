package com.example.assignment3.provider;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = Event.TABLE_NAME)
public class Event {

    public static final String TABLE_NAME = "Event";

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;
    @ColumnInfo(name = "eventId")
    private String eventId;
    @ColumnInfo(name = "categoryId")
    private String categoryId;
    @ColumnInfo(name = "eventName")
    private String eventName;
    @ColumnInfo(name = "ticketsAvailable")
    private int ticketsAvailable;
    @ColumnInfo(name = "isActive")
    private boolean isActive;

    public Event (String eventId, String categoryId, String eventName, int ticketsAvailable, boolean isActive){
        this.eventId = eventId;
        this.categoryId = categoryId;
        this.eventName = eventName;
        this.ticketsAvailable = ticketsAvailable;
        this.isActive = isActive;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEventId() {
        return eventId;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public String getEventName() {
        return eventName;
    }

    public int getTicketsAvailable() {
        return ticketsAvailable;
    }

    public boolean isActive() {
        return isActive;
    }
}
