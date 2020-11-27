package com.example.androidassignments;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class ChatWindow extends AppCompatActivity {

    private TextView message;
    private TextView id;
    private Button send_button;
    private ListView chat_window;
    private EditText chat_edit_text;
    private FrameLayout frame_layout;
    private ChatAdapter messageAdapter;
    private ArrayList<String> stored_messages;
    private ChatDatabaseHelper db_helper;
    private Boolean flag;
    private SQLiteDatabase db;
    private ContentValues values;
    private Cursor cursor;
    private String[] columns = {db_helper.KEY_ID,
            db_helper.KEY_MESSAGE};

    protected static final String ACTIVITY_NAME = "ChatWindow";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_window);

        message = findViewById(R.id.message_text);
        id = findViewById(R.id.id_number);

        flag = false;
        values = new ContentValues();
        db_helper = new ChatDatabaseHelper(this);
        db = db_helper.getWritableDatabase();
        frame_layout = findViewById(R.id.frame_layout);
        if (frame_layout != null) {
            flag = true;
        }
        send_button = findViewById(R.id.button2);
        chat_window = findViewById(R.id.chatView);
        chat_edit_text = findViewById(R.id.message_text);
        stored_messages = new ArrayList<String>();
        messageAdapter = new ChatAdapter(this);
        chat_window.setAdapter(messageAdapter);

        cursor = db.query(db_helper.TABLE_NAME, columns, null, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            while(!cursor.isAfterLast()) {
                Log.i(ACTIVITY_NAME, "SQL MESSAGE:" + cursor.getString(cursor.getColumnIndex(db_helper.KEY_MESSAGE))); //Log each message
                Log.i(ACTIVITY_NAME, cursor.getColumnName(cursor.getColumnIndex(db_helper.KEY_MESSAGE)));  //Col where message was retrieved from
                stored_messages.add(cursor.getString(cursor.getColumnIndex(db_helper.KEY_MESSAGE)));  //Add messages to list
                cursor.moveToNext();  //Move to next row
            }
        }

        chat_window.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.i(ACTIVITY_NAME, "In setOnItemClickListener");
                Bundle bundle = new Bundle();
                bundle.putLong("keyID", l);
                bundle.putString("message", cursor.getString(cursor.getColumnIndex(db_helper.KEY_MESSAGE)));

                if (flag) {
                    //Tablet stuff
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    MessageFragment fragment = new MessageFragment(ChatWindow.this);
                    fragment.setArguments(bundle);
                    ft.add(R.id.frame_layout, fragment);
                    ft.commit();
                } else {
                    //Phone stuff
                    Intent intent = new Intent(ChatWindow.this, MessageDetails.class);
                    startActivityForResult(intent, 5, bundle);
                }
            }
        });


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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 5) {
            Log.i(ACTIVITY_NAME, "Returned to ChatWindow.onActivityResult");
        }
        if (resultCode == 2) { //Delete selected
            //Delete ID
            int id = data.getIntExtra("ID", 0);
            stored_messages.remove(id);
            db.delete(db_helper.TABLE_NAME, "WHERE " + db_helper.KEY_ID + " = " + id, null);
        }
    }

    @Override
    protected void onDestroy(){
        Log.i(ACTIVITY_NAME, "In onDestroy()");
        super.onDestroy();
        db.close();
    }

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

        public long getItemId(int position) {
            long item_position;

            cursor.moveToPosition(position);
            item_position = cursor.getInt(cursor.getColumnIndex(db_helper.KEY_ID));

            return item_position;
        }
    }

}