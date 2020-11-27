package com.example.androidassignments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

public class MessageFragment extends Fragment {
    private ChatWindow chat;

    public MessageFragment() {
        super(R.layout.activity_message_fragment);
        this.chat = null;
    }

    public MessageFragment(ChatWindow chat) {
        super(R.layout.activity_message_fragment);
        this.chat = chat;
    }

    @Override
    public void setArguments(@Nullable Bundle args) {
        super.setArguments(args);
        //chat.message.setText(args.getString("message"));
        //chat.id.setText(args.getLong("id").toString());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_message_fragment, container, false);
    }
}