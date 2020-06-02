package com.example.tellmamobile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

public class ChatListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.chat_list_recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        String[] chatListDataSet = { "Conversa 1", "Conversa 2", "Conversa 3", "Conversa 4", "Conversa 5", "Conversa 6", "Conversa 7", "Conversa 8", "Conversa 9", "Conversa 10", "Conversa 11", "Conversa 12" };

        ChatListAdapter chatListAdapter = new ChatListAdapter(chatListDataSet);
        recyclerView.setAdapter(chatListAdapter);



    }
}