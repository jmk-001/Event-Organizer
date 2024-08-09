package com.example.assignment3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GestureDetectorCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import com.example.assignment3.provider.Event;
import com.example.assignment3.provider.EventManagementViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Dashboard extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    FrameLayout frameLayout;
    FragmentListEvent eventFragment;
    FragmentListCategory eventCategoryFragment;
    CategoryRecyclerAdapter categoryRecyclerAdapter;
    boolean categoryExists;
    boolean isValid = true;
    FloatingActionButton fab;
    EventManagementViewModel emViewModel;
    Handler uiHandler = new Handler(Looper.getMainLooper());
    ArrayList<Event> dataToBeDeleted;

    // Entries
    private TextView tvNewEventIdEntry, tvCategoryIdEntry, tvNewEventNameEntry, tvTicketsAvailableEntry;
    Switch tvNewEventIsActiveSwitch;

    public DrawerLayout drawerLayout;
    Toolbar toolbar;

    // Gestures
    private TextView tvGesture;
    private GestureDetectorCompat mDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_layout);

        tvNewEventIdEntry = findViewById(R.id.newEventIdEntry);
        tvCategoryIdEntry = findViewById(R.id.catIdEntry);
        tvNewEventNameEntry = findViewById(R.id.newEventNameEntry);
        tvTicketsAvailableEntry = findViewById(R.id.ticketsAvailableEntry);
        tvNewEventIsActiveSwitch = findViewById(R.id.newEventIsActiveSwitch);
        tvGesture = findViewById(R.id.tvGesture);

        frameLayout = findViewById(R.id.dash_frame);
        eventCategoryFragment = new FragmentListCategory();
        eventFragment = new FragmentListEvent();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.dash_frame, eventFragment).addToBackStack("f1").commit();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.dash_frame, eventCategoryFragment).addToBackStack("f2").commit();

        categoryRecyclerAdapter = new CategoryRecyclerAdapter();

        drawerLayout = findViewById(R.id.drawer_layout);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        emViewModel = new ViewModelProvider(this).get(EventManagementViewModel.class);

        emViewModel.getAllEvent().observe(this, newData -> {
            dataToBeDeleted = new ArrayList<Event>(newData);
        });

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickSaveEventButton(view);
            }
        });

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // initialise new instance of CustomGestureDetector class
        CustomGestureDetector customGestureDetector = new CustomGestureDetector();

        // register GestureDetector and set listener as CustomGestureDetector
        mDetector = new GestureDetectorCompat(this, customGestureDetector);

        mDetector.setOnDoubleTapListener(customGestureDetector);

        View touchPad = findViewById(R.id.touchPad);
        touchPad.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mDetector.onTouchEvent(event);

//                // get the type of Motion Event detected which is represented by a pre-defined integer value
//                int action = event.getAction();
//
//                // compare the detected event type against pre-defined values
//                if (action == MotionEvent.ACTION_DOWN){
//                    tvEventType.setText("ACTION_DOWN");
//                } else if (action == MotionEvent.ACTION_UP){
//                    tvEventType.setText("ACTION_UP");
//                } else if (action == MotionEvent.ACTION_MOVE){
//                    tvEventType.setText("ACTION_MOVE");
//                }
                return true;
            }
        });
    }

    public void onClickSaveEventButton(View view){
        String idGenerated = generateEventId();
        tvNewEventIdEntry.setText(idGenerated);

        String newEventId = tvNewEventIdEntry.getText().toString();
        String categoryId = tvCategoryIdEntry.getText().toString();
        String newEventName = tvNewEventNameEntry.getText().toString();
        int ticketsAvailable;
        if (tvTicketsAvailableEntry.getText().toString().equals("")){
            ticketsAvailable = 0;
        } else { ticketsAvailable = Integer.parseInt(tvTicketsAvailableEntry.getText().toString()); }
        boolean newEventIsActive = tvNewEventIsActiveSwitch.isChecked();

        // Validations
        if (ticketsAvailable < 0) {
            Toast.makeText(this, "Invalid Tickets Available.", Toast.LENGTH_SHORT).show();
            tvTicketsAvailableEntry.setText(0);
            isValid = false;
        }
        if (isNumeric(newEventName) || newEventName.isEmpty() || !isAlphanumeric(newEventName)) {
            Toast.makeText(this, "Invalid Event Name", Toast.LENGTH_SHORT).show();
            isValid = false;
        }
        if (isValid) {
            // Check whether EventCategory exits and save
            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.execute(() -> {
                categoryExists = emViewModel.categoryExists(categoryId);
                if (categoryExists) {
                    emViewModel.insert(new Event(newEventId, categoryId, newEventName, ticketsAvailable, newEventIsActive));
                }
                uiHandler.post(() -> {
                    String message;
                    if (categoryExists) {
                        emViewModel.incrementCategoryEventCount(categoryId);
                        message = "Event: " + tvNewEventIdEntry.getText() + " has been added.";
                        Snackbar snackbar = Snackbar
                                .make(view, message, Snackbar.LENGTH_LONG)
                                .setAction("UNDO", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        emViewModel.deleteEventById(newEventId);
                                        emViewModel.decrementCategoryEventCount(categoryId);
                                        Snackbar mSnackbar = Snackbar.make(view, "Save action has been reverted.", Snackbar.LENGTH_SHORT);
                                        mSnackbar.show();
                                    }
                                });
                        snackbar.show();
                    } else {
                        message = "Event Category does not exist.";
                    }
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                });
            });
        }
    }

    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            int i = Integer.parseInt(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    private boolean isAlphanumeric(String s){
        boolean result = true;
        boolean atLeastOneString = false;
        for (int i = 0; i < s.length(); ++i) {
            final int c = s.codePointAt(i);
            if (Character.isAlphabetic(c)){
                atLeastOneString = true;
            }
            if ((!Character.isAlphabetic(c) && !Character.isDigit(c)) && !(c == 32)) {
                result = false;
                break;
            }
        }
        return result && atLeastOneString;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        if (item.getItemId() == R.id.refresh){
            eventCategoryFragment.categoryRecyclerAdapter.notifyDataSetChanged();
        } else if (item.getItemId() == R.id.clear_event_form){
            clearEventForm();
        } else if (item.getItemId() == R.id.delete_all_categories){
            //eventCategoryFragment.resetData();
            emViewModel.deleteAll();
            eventCategoryFragment.categoryRecyclerAdapter.notifyDataSetChanged();
        } else if (item.getItemId() == R.id.delete_all_events){
            // Start a thread to delete all events and decrement category event count
            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.execute(() -> {
                for (Event e: dataToBeDeleted){
                    emViewModel.deleteEventById(e.getEventId());
                    emViewModel.decrementCategoryEventCount(e.getCategoryId());
                }
                eventFragment.eventRecyclerAdapter.notifyDataSetChanged();
            });
        }
        return true;
    }

    public void clearEventForm(){
        tvNewEventIdEntry.setText("");
        tvCategoryIdEntry.setText("");
        tvNewEventNameEntry.setText("");
        tvTicketsAvailableEntry.setText("");
        tvNewEventIsActiveSwitch.setChecked(false);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Intent i;
        // get the id of the selected item
        int id = item.getItemId();
        if (id == R.id.view_all_categories) {
            i = new Intent(this, ListCategoryActivity.class);
            startActivity(i);
        } else if (id == R.id.add_category) {
            i = new Intent(this, CreateNewEventCategory.class);
            startActivity(i);
        } else if (id == R.id.view_all_events) {
            i = new Intent(this, ListEventActivity.class);
            startActivity(i);
        } else if (id == R.id.logout) {
            i = new Intent(this, SignInScreen.class);
            startActivity(i);
            finish();
        }
        drawerLayout.closeDrawers();
        return true;
    }

    private String generateEventId(){
        Random rand = new Random();
        int randInt = rand.nextInt(100000);
        char c1 = (char)(rand.nextInt(26) + 'a');
        c1 = Character.toUpperCase(c1);
        char c2 = (char)(rand.nextInt(26) + 'a');
        c2 = Character.toUpperCase(c2);

        return "E" + c1+c2 + "-" + randInt;
    }

    class CustomGestureDetector extends GestureDetector.SimpleOnGestureListener{
//        @Override
//        public boolean onDown(@NonNull MotionEvent e) {
//            tvGesture.setText("onDown");
//            return super.onDown(e);
//        }

        @Override
        public void onLongPress(@NonNull MotionEvent e) {
            tvGesture.setText("onLongPress");
            fab.callOnClick();
            super.onLongPress(e);
        }

        @Override
        public boolean onDoubleTap(@NonNull MotionEvent e) {
            tvGesture.setText("onDoubleTap");
            clearEventForm();
            return super.onDoubleTap(e);
        }
    }
}