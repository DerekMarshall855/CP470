package com.example.androidassignments;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Button button;
    private Button chatButton;
    private Button testToolbar;
    private Button weatherButton;

    protected static final String ACTIVITY_NAME = "MainActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.i(ACTIVITY_NAME, "In onCreate()");

        button = findViewById(R.id.button);
        chatButton = findViewById(R.id.start_chat);
        testToolbar = findViewById(R.id.test_toolbar);
        weatherButton = findViewById(R.id.weather_button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextActivity();
            }
        });
        chatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(ACTIVITY_NAME, "User clicked Start Chat");
                Intent intent = new Intent(MainActivity.this, ChatWindow.class);
                startActivity(intent);
            }
        });
        testToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(ACTIVITY_NAME, "User clicked Test Toolbar");
                Intent intent = new Intent(MainActivity.this, TestToolbar.class);
                startActivity(intent);
            }
        });

        weatherButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(ACTIVITY_NAME, "User clicked Weather Button");
                Intent intent = new Intent(MainActivity.this, WeatherForecast.class);
                startActivity(intent);
            }
        });
    }

    public void nextActivity() {
        Intent intent = new Intent(MainActivity.this, ListItemsActivity.class);
        startActivityForResult(intent, 10);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10) {
            Log.i(ACTIVITY_NAME, "Returned to MainActivity.onActivityResult");
        }
        if (resultCode == Activity.RESULT_OK) {
            String messagePassed = data.getStringExtra("Response");
            funToast(messagePassed);
        }
    }

    public void funToast(String message) {
        CharSequence text;
        int duration = Toast.LENGTH_LONG;

        Toast toast = Toast.makeText(this, message, duration);
        toast.show();
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

}