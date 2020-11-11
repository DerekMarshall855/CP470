package com.example.androidassignments;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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

import java.util.ArrayList;

public class ChatWindow extends AppCompatActivity {

    private Button send_button;
    private ListView chat_window;
    private EditText chat_edit_text;
    private ChatAdapter messageAdapter;
    private ArrayList<String> stored_messages;
    private ChatDatabaseHelper db_helper;
    private SQLiteDatabase db;
    private ContentValues values;
    private String[] columns = {db_helper.KEY_ID,
            db_helper.KEY_MESSAGE};

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
        values = new ContentValues();
        db_helper = new ChatDatabaseHelper(this);
        db = db_helper.getWritableDatabase();
        send_button = findViewById(R.id.button2);
        chat_window = findViewById(R.id.chatView);
        chat_edit_text = findViewById(R.id.message_text);
        stored_messages = new ArrayList<String>();
        messageAdapter = new ChatAdapter(this);
        chat_window.setAdapter(messageAdapter);

        Cursor cursor = db.query(db_helper.TABLE_NAME, columns, null, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            while(!cursor.isAfterLast()) {
                Log.i(ACTIVITY_NAME, "SQL MESSAGE:" + cursor.getString(cursor.getColumnIndex(db_helper.KEY_MESSAGE))); //Log each message
                Log.i(ACTIVITY_NAME, cursor.getColumnName(cursor.getColumnIndex(db_helper.KEY_MESSAGE)));  //Col where message was retrieved from
                stored_messages.add(cursor.getString(cursor.getColumnIndex(db_helper.KEY_MESSAGE)));  //Add messages to list
                cursor.moveToNext();  //Move to next row
            }
        }


        for(int i = 0; i < cursor.getColumnCount(); i++) {
            Log.i(ACTIVITY_NAME, cursor.getColumnName(i));  //Print of all col names
        }



        send_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stored_messages.add(chat_edit_text.getText().toString());
                values.put(db_helper.KEY_MESSAGE, chat_edit_text.getText().toString());
                db.insert(db_helper.TABLE_NAME, null, values);
                messageAdapter.notifyDataSetChanged();
                chat_edit_text.setText("");
            }
        });
    }

    @Override
    protected void onDestroy(){
        Log.i(ACTIVITY_NAME, "In onDestroy()");
        super.onDestroy();
        db.close();
    }

}