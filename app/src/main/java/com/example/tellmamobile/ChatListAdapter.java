package com.example.tellmamobile;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ChatListAdapter extends BaseAdapter {
    private String[] chatListDataSet;
    private final Activity act;

    public ChatListAdapter(String[] myChatListDataSet, Activity act) {
        chatListDataSet = myChatListDataSet;
        this.act = act;
    }

    @Override public int getCount() { return chatListDataSet.length; }

    @Override public Object getItem(int position) { return chatListDataSet[position]; }

    @Override public long getItemId(int position) { return 0; }

    @Override public View getView(int position, View convertView, ViewGroup parent) {
        // create a new view
        View view = act.getLayoutInflater().inflate(R.layout.card_chat_list, parent, false);

        String chat = chatListDataSet[position];

        TextView textView = (TextView) view.findViewById(R.id.chat_text);
        textView.setText(chat);

        return view;

    }
}

