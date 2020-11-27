package com.example.androidassignments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MessageDetails extends AppCompatActivity {

    private Button delete;
    private Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_details);

        bundle = getIntent().getExtras();

        MessageFragment fragment = new MessageFragment();

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.frame_layout, fragment);
        ft.commit();

        delete = findViewById(R.id.delete_button);

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Should only be accessible when using phone
                int id = bundle.getInt("keyID");
                Intent resultIntent = new Intent();
                resultIntent.putExtra("ID", id);
                setResult(2, resultIntent);
                finish();
            }
        });

    }

}