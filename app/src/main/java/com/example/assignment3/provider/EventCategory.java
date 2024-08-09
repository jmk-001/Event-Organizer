package com.example.assignment3.provider;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = EventCategory.TABLE_NAME)
public class EventCategory {

    public static final String TABLE_NAME = "EventCategory";

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;
    @ColumnInfo(name = "categoryId")
    private String categoryId;
    @ColumnInfo(name = "categoryName")
    private String categoryName;
    @ColumnInfo(name = "eventCount")
    private int eventCount;
    @ColumnInfo(name = "eventLocation")
    private String eventLocation;
    @ColumnInfo(name = "isActive")
    private boolean isActive;

    public EventCategory(String categoryId, String categoryName, int eventCount, String eventLocation, boolean isActive){
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.eventCount = eventCount;
        this.eventLocation = eventLocation;
        this.isActive = isActive;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public int getEventCount() {
        return eventCount;
    }

    public String getEventLocation() {
        return eventLocation;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setEventCount(int eventCount) {
        this.eventCount = eventCount;
    }
}
