package com.example.androidassignments;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class ChatWindow extends AppCompatActivity {

    private Button send_button;
    private ListView chat_window;
    private EditText chat_edit_text;
    private ChatAdapter messageAdapter;
    private ArrayList<String> stored_messages;

    protected static final String ACTIVITY_NAME = "ChatWindow";

    public class ChatAdapter extends ArrayAdapter<String> {
        public ChatAdapter (Context ctx) {
            super(ctx, 0);
        }

        public int getCount() {
            return stored_messages.size();
        }

        public String getItem(int position) {
            return stored_messages.get(position);
        }

        public View getView(int position, View covertView, ViewGroup parent) {
            LayoutInflater inflater = ChatWindow.this.getLayoutInflater();
            View result = null;
            if (position % 2 == 0) {
                result = inflater.inflate(R.layout.chat_row_incoming, null);
            } else {
                result = inflater.inflate(R.layout.chat_row_outgoing, null);
            }

            TextView message = (TextView)result.findViewById(R.id.message_text);
            message.setText(getItem(position));
            return result;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_window);

        send_button = findViewById(R.id.button2);
        chat_window = findViewById(R.id.chatView);
        chat_edit_text = findViewById(R.id.message_text);
        stored_messages = new ArrayList<String>();
        messageAdapter = new ChatAdapter(this);
        chat_window.setAdapter(messageAdapter);


        send_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stored_messages.add(chat_edit_text.getText().toString());
                messageAdapter.notifyDataSetChanged();
                chat_edit_text.setText("");
            }
        });
    }

}