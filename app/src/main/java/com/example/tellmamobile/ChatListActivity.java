package com.example.tellmamobile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ListView;

public class ChatListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);

        ListView listView = (ListView) findViewById(R.id.chat_list_recycler_view);

        String[] chatListDataSet = { "Conversa 1", "Conversa 2", "Conversa 3", "Conversa 4", "Conversa 5", "Conversa 6", "Conversa 7", "Conversa 8", "Conversa 9", "Conversa 10", "Conversa 11", "Conversa 12" };

        ChatListAdapter chatListAdapter = new ChatListAdapter(chatListDataSet, this);
        listView.setAdapter(chatListAdapter);
    }
}