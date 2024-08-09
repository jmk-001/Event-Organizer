package com.example.assignment3.provider;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {EventCategory.class, Event.class}, version = 1)
public abstract class EventManagementDatabase extends RoomDatabase {

    public static final String EVENTMANAGEMENT_DATABASE = "eventmanagement_database";
    public abstract EventManagementDAO eventManagementDAO();

    private static volatile EventManagementDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    static EventManagementDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (EventManagementDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    EventManagementDatabase.class, EVENTMANAGEMENT_DATABASE)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

}