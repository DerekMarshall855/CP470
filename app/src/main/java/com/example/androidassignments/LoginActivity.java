package com.example.androidassignments;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.content.Context;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {

    private EditText loginText;
    private Button loginButton;

    protected static final String ACTIVITY_NAME = "LoginActivity";
    public static final String SHARED_PREFS = "sharedPrefs";

    private String logLoad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Log.i(ACTIVITY_NAME, "In onCreate()");
        loginText = (EditText) findViewById(R.id.login_text);
        loginButton = (Button) findViewById(R.id.login_button);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveData();
                nextActivity();
            }
        });

        loadData();
        updateViews();
    }

    public void nextActivity() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
    }

    public void saveData() {
        SharedPreferences prefs = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor edit = prefs.edit();

        Log.i(ACTIVITY_NAME, loginText.getText().toString());
        edit.putString("DefaultEmail", loginText.getText().toString());
        edit.apply();
    }

    public void loadData() {
        SharedPreferences prefs = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        logLoad = prefs.getString("DefaultEmail", "email@domain.com");
    }

    public void updateViews() {
        loginText.setText(logLoad);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(ACTIVITY_NAME, "In onResume()");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(ACTIVITY_NAME, "In onStart()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(ACTIVITY_NAME, "In onPause()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(ACTIVITY_NAME, "In onStop()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(ACTIVITY_NAME, "In onDestroy()");
    }

    public void loginSucc(View view) {
        Log.i(ACTIVITY_NAME, "In loginSucc()");

    }
}