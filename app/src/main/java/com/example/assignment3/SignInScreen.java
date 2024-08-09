package com.example.assignment3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class SignInScreen extends AppCompatActivity {
    TextView entryId, entryPw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in_screen);

        entryId = findViewById(R.id.signInIdEntry);
        entryPw = findViewById(R.id.signInPasswordEntry);

        SharedPreferences sharedPreferences = getSharedPreferences(KeyStore.FILE_NAME, MODE_PRIVATE);
        entryId.setText(sharedPreferences.getString(KeyStore.KEY_USER_ID, ""));
    }

    public void onClickSignInButton(View view){
        String idEntered = entryId.getText().toString();
        String passwordEntered = entryPw.getText().toString();

        // validate id, pw and sign in
        if (idEntered.equals("")){
            Toast.makeText(this, "Please enter your ID.", Toast.LENGTH_SHORT).show();
        }
        else if (passwordEntered.equals("")){
            Toast.makeText(this, "Please enter your password.", Toast.LENGTH_SHORT).show();
        }

        // validate with stored id, pw
        SharedPreferences sharedPreferences = getSharedPreferences(KeyStore.FILE_NAME, MODE_PRIVATE);

        String userIdRestored = sharedPreferences.getString(KeyStore.KEY_USER_ID, "Default");
        String userPwRestored = sharedPreferences.getString(KeyStore.KEY_USER_PASSWORD, "Default");

        if (idEntered.equals(userIdRestored) && passwordEntered.equals(userPwRestored)) {
            String message = String.format("Welcome %s.", idEntered);
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            // start next activity
            Intent intent = new Intent(this, Dashboard.class);
            startActivity(intent);
        } else { Toast.makeText(this, "Authentication failure: Username or Password incorrect", Toast.LENGTH_SHORT).show(); }
    }

    public void onClickBackToSignUpButton(View view){
        finish();
    }
}