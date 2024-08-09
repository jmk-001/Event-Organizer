package com.example.assignment3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.assignment3.provider.EventCategory;
import com.example.assignment3.provider.EventManagementViewModel;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CreateNewEventCategory extends AppCompatActivity {

    TextView tvNewCatIdEntry, tvNewCatNameEntry, tvNewCatEventCountEntry, tvNewCatLocationEntry;
    Switch tvNewCatIsActiveSwitch;
    private EventManagementViewModel emViewModel;
    Handler uiHandler = new Handler(Looper.getMainLooper());

    private boolean categoryExists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_event_category);

        tvNewCatIdEntry = findViewById(R.id.newCatIdEntry);
        tvNewCatNameEntry = findViewById(R.id.newCatNameEntry);
        tvNewCatEventCountEntry = findViewById(R.id.newEventCountEntry);
        tvNewCatLocationEntry = findViewById(R.id.tvNewEventCategoryLocationEntry);
        tvNewCatIsActiveSwitch = findViewById(R.id.newCatIsActiveSwitch);

        emViewModel = new ViewModelProvider(this).get(EventManagementViewModel.class);

        //ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.SEND_SMS, android.Manifest.permission.RECEIVE_SMS, Manifest.permission.READ_SMS}, 0);

        //MyBroadCastReceiver myBroadCastReceiver = new MyBroadCastReceiver();

        //registerReceiver(myBroadCastReceiver, new IntentFilter(SMSReceiver.SMS_FILTER_NEW_EVENT_CAT));
    }

    public void onClickSaveCategoryButton(View view){
        String idGenerated = generateCategoryId();
        tvNewCatIdEntry.setText(idGenerated);

        String newCatId = tvNewCatIdEntry.getText().toString();
        String newCatName = tvNewCatNameEntry.getText().toString();
        int newCatEventCount;
        if (tvNewCatEventCountEntry.getText().toString().equals("")){
            newCatEventCount = 0;
        } else { newCatEventCount = Integer.parseInt(tvNewCatEventCountEntry.getText().toString()); }
        String newCatLocation = tvNewCatLocationEntry.getText().toString();

        boolean newCatIsActive = tvNewCatIsActiveSwitch.isChecked();

        if (validateEventCategoryInput(newCatName, newCatEventCount)) {
            EventCategory category = new EventCategory(newCatId, newCatName, newCatEventCount, newCatLocation, newCatIsActive);

            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.execute(() -> {
                categoryExists = eventCategoryExists(category);
                if (!categoryExists) {
                    emViewModel.insert(category);
                }
                uiHandler.post(()-> {
                    String message;
                    if (categoryExists){
                        message = "Category already exists.";
                    }
                    else {
                        message = String.format("Category saved successfully: %s", newCatId);
                    }
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                });
                finish();
            });
        }
    }

    private boolean validateEventCategoryInput(String name, int eventCount) {
        boolean isValid = true;
        if (eventCount < 0) {
            Toast.makeText(this, "Invalid Tickets Available.", Toast.LENGTH_SHORT).show();
            tvNewCatEventCountEntry.setText(0);
            isValid = false;
        }
        if (isNumeric(name) || name.isEmpty() || !isAlphanumeric(name)) {
            Toast.makeText(this, "Invalid Category Name", Toast.LENGTH_SHORT).show();
            isValid = false;
        }
        return isValid;
    }

    private boolean eventCategoryExists(EventCategory category){
        return emViewModel.categoryExists(category.getCategoryId());
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

    private String generateCategoryId(){
        Random rand = new Random();
        int randInt = rand.nextInt(10000);
        char c1 = (char)(rand.nextInt(26) + 'a');
        c1 = Character.toUpperCase(c1);
        char c2 = (char)(rand.nextInt(26) + 'a');
        c2 = Character.toUpperCase(c2);

        return "C" + c1+c2 + "-" + randInt;
    }

//    private void saveDataToSharedPreference(String catId, String catName, int eventCount, boolean isActive){
//        SharedPreferences sharedPreferences = getSharedPreferences(KeyStore.FILE_NAME, MODE_PRIVATE);
//
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//
//        editor.putString(KeyStore.KEY_CATEGORY_ID, catId);
//        editor.putString(KeyStore.KEY_CATEGORY_NAME, catName);
//        editor.putInt(KeyStore.KEY_EVENT_COUNT, eventCount);
//        editor.putBoolean(KeyStore.KEY_CATEGORY_IS_ACTIVE, isActive);
//
//        editor.apply();
//    }
//
//    class MyBroadCastReceiver extends BroadcastReceiver {
//        String[] categoryLine;
//        String categoryName, eventCount;
//        boolean isActive;
//
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            String msg = intent.getStringExtra(SMSReceiver.SMS_KEY_NEW_EVENT_CAT);
//
//            StringTokenizer sT = new StringTokenizer(msg, ";");
//            if (validateInput(sT)) {
//                tvNewCatNameEntry.setText(categoryName);
//                tvNewCatEventCountEntry.setText(eventCount);
//                tvNewCatIsActiveSwitch.setChecked(isActive);
//            } else { Toast.makeText(getApplicationContext(), "Unknown or Invalid Command", Toast.LENGTH_SHORT).show(); }
//        }
//
//        private boolean validateInput(StringTokenizer sT) {
//            try {
//                categoryLine = sT.nextToken().split(":");
//                categoryName = categoryLine[1];
//
//                int ec;
//                String eventCountString;
//                try {
//                    eventCountString = sT.nextToken();
//                }
//                catch (NoSuchElementException e){
//                    eventCountString = "0";
//                }
//                ec = Integer.parseInt(eventCountString);
//                eventCount = eventCountString;
//
//                boolean ia;
//                String isActiveString;
//                try {
//                    isActiveString = sT.nextToken();
//                }
//                catch (NoSuchElementException e){
//                    isActiveString = "FALSE";
//                }
//                ia = Boolean.parseBoolean(isActiveString);
//                isActive = ia;
//
//            } catch (Exception e) {
//                return false;
//            }
//            return true;
//        }
//    }
}