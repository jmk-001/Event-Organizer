package com.example.assignment3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClickSignUpButton(View view){
        TextView entryUsername = findViewById(R.id.signUpUsernameEntry);
        TextView entryPassword = findViewById(R.id.signUpPasswordEntry);
        TextView entryConfirmPassword = findViewById(R.id.signUpConfirmPasswordEntry);

        String usernameEntered = entryUsername.getText().toString();
        String passwordEntered = entryPassword.getText().toString();
        String confirmPasswordEntered = entryConfirmPassword.getText().toString();

        if (usernameEntered.length() == 0){
            Toast.makeText(this, "Passwords entered do not match.", Toast.LENGTH_SHORT).show();
        }
        else if (!passwordEntered.equals(confirmPasswordEntered)){
            Toast.makeText(this, "Passwords entered do not match.", Toast.LENGTH_SHORT).show();
        }
        // Sign-up successful, save data and log in
        else {
            saveDataToSharedPreference(usernameEntered, passwordEntered);
            String message = String.format("Sign Up Successful. Welcome %s.", usernameEntered);
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

            // start new activity
            Intent intent = new Intent(this, SignInScreen.class);
            startActivity(intent);
        }
    }

    private void saveDataToSharedPreference(String name, String id){
        SharedPreferences sharedPreferences = getSharedPreferences(KeyStore.FILE_NAME, MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(KeyStore.KEY_USER_ID, name);
        editor.putString(KeyStore.KEY_USER_PASSWORD, id);

        editor.apply();
    }

    public void onClickSignInActivityButton(View view){
        Intent intent = new Intent(this, SignInScreen.class);
        startActivity(intent);
    }
}